package com.softure;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServlet;
import javax.sql.DataSource;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

import com.softure.java.cons.ConstantesGenerales;
import com.softure.java.dto.exception.ServerException;
import com.softure.mail.application.MailReleaseMessageQueueService;
import com.softure.process_designer.application.ProcesoTransicionAutomaticaSvc;
import com.softure.report.infrastructure.ReporteServlet;
import com.softure.upload.infrastructure.DownloaderServlet;
import com.softure.upload.infrastructure.UploaderServlet;
import com.softure.webservice.application.WebServiceEjecucionSvc;


@Configuration
@EnableTransactionManagement
@EnableScheduling
@MapperScan(basePackages = "com.softure.*.infrastructure", annotationClass= SoftureSqlConnMapper.class, sqlSessionFactoryRef="sqlSessionFactory")
public class SoftureConfiguration {

	@Autowired private Environment env;
	@Autowired private MailReleaseMessageQueueService releaseQueueService;
	@Autowired private ProcesoTransicionAutomaticaSvc transicionservice;
	@Autowired private WebServiceEjecucionSvc apiService;
	
	@Autowired private AutowireCapableBeanFactory beanFactory;

	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() {
		System.out.println("*********************************************************");
		System.out.println("BD = " + env.getProperty("db.url"));
		System.out.println("BD IND = " + env.getProperty("db.accounting.jdbc-url"));
		System.out.println("Correos Activos = " + env.getProperty("cron.enabled"));
		System.out.println("Tareas Activas = " + env.getProperty("cron.task"));
		System.out.println("*********************************************************");
		// Obtengo version actual
		PooledDataSource ds = dynamicDataSource();
		String actualString = getActualDate(ds);
		System.out.println("Fecha actual = " + actualString);
		
		if(actualString==null) {
			System.out.println("*********************************************************");
			System.out.println("*******                SOFTURE                   ********");
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
			System.out.println("*******               SOFTURE                    ********");
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
			System.out.println("*******                SOFTURE                   ********");
			System.out.println("*******     LO HEMOS LOGRADO TODO ACTUALIZADO    ********");
			System.out.println("*******                                          ********");
			System.out.println("****************:)****:)***:)***:)***:)******************");
			System.out.println("*********************************************************");			
		} else {
			System.out.println("*********************************************************");
			System.out.println("*******     ERROR       SOFTURE     ERROR        ********");
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
	PooledDataSource dynamicDataSource() {
		PooledDataSource dataSource = new PooledDataSource();
		dataSource.setDriver(env.getProperty("db.driver"));
		dataSource.setUrl(env.getProperty("db.url"));
		dataSource.setUsername(env.getProperty("db.username"));
		dataSource.setPassword(env.getProperty("db.password"));
		return dataSource;
	}

	@Bean(name = "transactionManager")
	DataSourceTransactionManager transactionManager(PooledDataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean(name = "sqlSessionFactory")
	SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") PooledDataSource dataSource) throws Exception {
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(dataSource);// Specify the data source (this must exist, otherwise an error will occur)
		// The next two sentences are for *.xml files only, if the XML file is not needed for the entire persistence layer operation (only annotations will do), they are not added
		factoryBean.setTypeAliasesPackage("com.softure.*.domain");//Specify base package
		factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:/com/softure/*/*.xml"));//
		return factoryBean.getObject();
	}

	@Scheduled(fixedDelayString = "${fixedDelayMail.in.milliseconds}")
	public void sendMail() throws ServerException {
		if(env.getProperty("cron.enabled").compareTo("true")==0) {
			System.out.println("*******CORREOS****" + new Date().toString());
			releaseQueueService.call();
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
	
	String executeAPITask;
	
	@Scheduled(fixedDelayString = "${fixedDelayApi.in.milliseconds}")
	public void sendAPI() throws ServerException {
		if(executeAPITask==null) {
			if(apiService.hasPropertiesAsync()) {
				executeAPITask = ConstantesGenerales.OK;
			}else {
				executeAPITask = ConstantesGenerales.NO_STRING;
			}
		}
		if(executeAPITask.compareTo(ConstantesGenerales.OK)==0) {
			System.out.println("*******APIS ASYNC****" + new Date().toString());
			apiService.apiToTransaction();
		}
	}
	
	private String getActualDate(DataSource ds) {
		String result = null;
		try {
			Connection conn = ds.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs;
			rs = stmt.executeQuery(
					"select description from pg_description join pg_class on pg_description.objoid = pg_class.oid join pg_namespace on pg_class.relnamespace = pg_namespace.oid where relname = 'catalogo_ctg';");
			while (rs.next()) {
				result = rs.getString("description");
			}
			conn.close();
		} catch (Exception e) {
			System.err.println("Got an exception! ");
			System.err.println(e.getMessage());
		}
		return result;
	}
	
	@Bean	
	ServletRegistrationBean<HttpServlet> reporteServlet() {
		ServletRegistrationBean<HttpServlet> servRegBean = new ServletRegistrationBean<>();
		final ReporteServlet servlet = new ReporteServlet();
		beanFactory.autowireBean(servlet);
		servRegBean.setServlet(servlet);
		servRegBean.addUrlMappings("/reporte/*");
		servRegBean.setLoadOnStartup(1);
		return servRegBean;
	}

	@Bean	
	ServletRegistrationBean<HttpServlet> uploadServlet() {
		ServletRegistrationBean<HttpServlet> servRegBean = new ServletRegistrationBean<>();
		final UploaderServlet servlet = new UploaderServlet();
		beanFactory.autowireBean(servlet);
		servRegBean.setServlet(servlet);
		servRegBean.addUrlMappings("/loader/*");
		servRegBean.setLoadOnStartup(1);
		return servRegBean;
	}
	
	@Bean	
	ServletRegistrationBean<HttpServlet> downloadServlet() {
		ServletRegistrationBean<HttpServlet> servRegBean = new ServletRegistrationBean<>();
		final DownloaderServlet servlet = new DownloaderServlet();
		beanFactory.autowireBean(servlet);
		servRegBean.setServlet(servlet);
		servRegBean.addUrlMappings("/resource/*");
		servRegBean.setLoadOnStartup(1);
		return servRegBean;
	}
	
}