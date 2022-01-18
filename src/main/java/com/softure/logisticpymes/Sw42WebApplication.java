package com.softure.logisticpymes;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServlet;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.init.ScriptException;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.services.MensajeSvc;
import com.softure.logisticpymes.services.ProcesoTransicionAutomaticaSvc;
import com.softure.logisticpymes.services.UsuarioAutenticacionSvc;
import com.softure.logisticpymes.servlet.DownloaderServlet;
import com.softure.logisticpymes.servlet.ReporteServlet;
import com.softure.logisticpymes.servlet.UploaderServlet;


@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
@MapperScan(basePackages = "com.softure.logisticpymes.persistence")
public class Sw42WebApplication  extends SpringBootServletInitializer implements WebMvcConfigurer {

	@Autowired private Environment env;
	@Autowired private MensajeSvc mensajeService;
	@Autowired private ProcesoTransicionAutomaticaSvc transicionservice;
	@Autowired private UsuarioAutenticacionSvc autService;
	
	@Autowired private AutowireCapableBeanFactory beanFactory;

	public static void main(String[] args) {
		SpringApplication.run(Sw42WebApplication.class, args);
		
	}

	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() {
		System.out.println("*********************************************************");
		System.out.println("*********************************************************");
		// Obtengo version actual 
		String actualString = autService.getFechaActualizacion();
		System.out.println("Fecha actual = " + actualString);
		if(actualString==null) {
			System.out.println("*********************************************************");
			System.out.println("*******                                          ********");
			System.out.println("*******     LLAMA YA AL SOFTWARE PARA TI .COM    ********");
			System.out.println("*******                                          ********");
			System.out.println("*********************************************************");
			System.out.println("*********************************************************");
			return;
		}
		Date actualDate;
		try {
			actualDate = new SimpleDateFormat("yyyy-MM-dd").parse(actualString);
		} catch (ParseException e1) {
			System.out.println("*********************************************************");
			System.out.println("*******                                          ********");
			System.out.println("*******     LLAMA YA AL SOFTWARE PARA TI .COM    ********");
			System.out.println("*******                                          ********");
			System.out.println("*********************************************************");
			System.out.println("*********************************************************");
			System.out.println(e1.getMessage());
			return;
		}
		System.out.println("Fecha actual en BD = " + actualDate.toString());
		Calendar iterador = Calendar.getInstance();
		iterador.setTime(actualDate);
		iterador.add(Calendar.DAY_OF_MONTH, 1);
		System.out.println("*********************************************************");
		System.out.println("************COMIENZA A ACTUALIZAR    ******************");
		System.out.println("*********************************************************");
		PooledDataSource ds = dynamicDataSource();
		String sqlName;
		boolean error = false;
		while (iterador.getTime().getTime() < new Date().getTime() && !error) {
			sqlName = "static/data/" + String.valueOf(iterador.get(Calendar.YEAR)); 
			sqlName = sqlName + "/" + String.valueOf(iterador.get(Calendar.YEAR)) + to2String(iterador.get(Calendar.MONTH)+1);
			sqlName = sqlName + "/" + String.valueOf(iterador.get(Calendar.YEAR)) + to2String(iterador.get(Calendar.MONTH)+1) + to2String(iterador.get(Calendar.DAY_OF_MONTH)) + ".sql"; 
			// System.out.println("Buscando Script = "  + sqlName );
			Resource fileSql = new ClassPathResource(sqlName);
			if(fileSql.exists()) {
				System.out.println("**************Ejecutando Script = "  + sqlName );
				error = new TransactionTemplate(transactionManager(ds)).execute((ts) -> {
					Connection conn = null;
					boolean fallaScript = true;
		            try {
		            	conn = ds.getConnection();
		            	conn.setAutoCommit(false);
		            	ScriptUtils.executeSqlScript(conn, new EncodedResource(fileSql, "UTF-8"));
		                conn.commit();
		                fallaScript = false;
		            } catch (ScriptException | SQLException  e) {
		    			
		    			System.out.println(e.getMessage());
		    			try {
							conn.rollback();
						} catch (SQLException e1) {
							System.out.println(e1.getMessage());
						}
		    		}  finally {
		    			if(conn!=null) { 
		            		try {
								conn.close();
							} catch (SQLException e1) {
				    			System.out.println(e1.getMessage());
							}
		            	} ;
					}
		            return fallaScript;
		        });
			}
			iterador.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		if(!error) {
			System.out.println("*******OKOKOKOKOKOKOKOKOKOKOKOKOKOKOKOKOOKOKOKOKO********");
			System.out.println("*******                                          ********");
			System.out.println("*******     LO HEMOS LOGRADO TODO ACTUALIZADO    ********");
			System.out.println("*******                                          ********");
			System.out.println("****************:)****:)***:)***:)***:)******************");
			System.out.println("*********************************************************");			
		} else {
			System.out.println("*********************************************************");
			System.out.println("*******     ERROR       ERROR       ERROR        ********");
			System.out.println("*******     LLAMA YA AL SOFTWARE PARA TI .COM    ********");
			System.out.println("*******                                          ********");
			System.out.println("********!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!********");
			System.out.println("********XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX*********");
		}
	}
	
	public String to2String(int number) {
		if(number < 10) {
			return "0" + String.valueOf(number);	
		}
		return String.valueOf(number);
	}
	

	@Bean(name = "dataSource")
	public PooledDataSource dynamicDataSource() {
		PooledDataSource dataSource = new PooledDataSource();
		dataSource.setDriver(env.getProperty("db.driver"));
		dataSource.setUrl(env.getProperty("db.url"));
		dataSource.setUsername(env.getProperty("db.username"));
		dataSource.setPassword(env.getProperty("db.password"));
		return dataSource;
	}

	@Bean(name = "transactionManager")
	public DataSourceTransactionManager transactionManager(PooledDataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean(name = "sqlSessionFactory")
	public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") PooledDataSource dataSource) throws Exception {
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(dataSource);// Specify the data source (this must exist, otherwise an error will occur)
		// The next two sentences are for *.xml files only, if the XML file is not needed for the entire persistence layer operation (only annotations will do), they are not added
		factoryBean.setTypeAliasesPackage("com.softure.logisticpymes.dto");//Specify base package
		factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:/com/softure/logisticpymes/persistence/*.xml"));//
		return factoryBean.getObject();
	}

	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver createMultipartResolver() {
		CommonsMultipartResolver resolver=new CommonsMultipartResolver();
		resolver.setMaxUploadSize(268435456);
		resolver.setDefaultEncoding("utf-8");
		return resolver;
	}

	@Scheduled(fixedDelayString = "${fixedDelayMail.in.milliseconds}")
	public void sendMail() throws ServerException {
		if(env.getProperty("cron.enabled").compareTo("true")==0) {
			System.out.println("*******CORREOS****" + new Date().toString());
			mensajeService.tareaCorreoElectronico();
		}
	}
	
	@Scheduled(fixedDelayString = "${fixedDelayTask.in.milliseconds}")
	public void sendTemporizer() throws ServerException {
		if(env.getProperty("cron.task").compareTo("true")==0) {
			System.out.println("*******TAREAS****" + new Date().toString());
			transicionservice.lanzarTransaccionesTemporizadas();
			transicionservice.programateAll();
		}
	}
	
	@Bean	
	public ServletRegistrationBean<HttpServlet> reporteServlet() {
		ServletRegistrationBean<HttpServlet> servRegBean = new ServletRegistrationBean<>();
		final ReporteServlet servlet = new ReporteServlet();
		beanFactory.autowireBean(servlet);
		servRegBean.setServlet(servlet);
		servRegBean.addUrlMappings("/reporte/*");
		servRegBean.setLoadOnStartup(1);
		return servRegBean;
	}

	@Bean	
	public ServletRegistrationBean<HttpServlet> uploadServlet() {
		ServletRegistrationBean<HttpServlet> servRegBean = new ServletRegistrationBean<>();
		final UploaderServlet servlet = new UploaderServlet();
		beanFactory.autowireBean(servlet);
		servRegBean.setServlet(servlet);
		servRegBean.addUrlMappings("/loader/*");
		servRegBean.setLoadOnStartup(1);
		return servRegBean;
	}
	
	@Bean	
	public ServletRegistrationBean<HttpServlet> downloadServlet() {
		ServletRegistrationBean<HttpServlet> servRegBean = new ServletRegistrationBean<>();
		final DownloaderServlet servlet = new DownloaderServlet();
		beanFactory.autowireBean(servlet);
		servRegBean.setServlet(servlet);
		servRegBean.addUrlMappings("/resource/*");
		servRegBean.setLoadOnStartup(1);
		return servRegBean;
	}
	
	//Soporta CORS
	@Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
        System.out.println("*******CORS****" + new Date().toString());
    }
	
	//Soporta que la SPA de angular funcione con solo el jar
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .resourceChain(true)
                .addResolver(new PathResourceResolver() {
                    @Override
                    protected Resource getResource(String resourcePath, Resource location) throws IOException {
                        Resource requestedResource = location.createRelative(resourcePath);
                         return requestedResource.exists() && requestedResource.isReadable() ? requestedResource
                                : new ClassPathResource("/static/index.html");
                    }
                });
    }
}