package com.softure.logisticpymes;

import com.softure.logisticpymes.services.MensajeSvc;
import com.softure.logisticpymes.services.ProcesoTransicionAutomaticaSvc;
import com.softure.logisticpymes.servlet.DownloaderServlet;
import com.softure.logisticpymes.servlet.ReporteServlet;
import com.softure.logisticpymes.servlet.UploaderServlet;
import com.softure.java.dto.exception.ServerException;

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
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;


@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
@MapperScan(basePackages = "com.softure.logisticpymes.persistence")
public class Sw42WebApplication  extends SpringBootServletInitializer {

	@Autowired private Environment env;
	@Autowired private MensajeSvc mensajeService;
	@Autowired private ProcesoTransicionAutomaticaSvc transicionservice;
	
	@Autowired private AutowireCapableBeanFactory beanFactory;

	public static void main(String[] args) {
		SpringApplication.run(Sw42WebApplication.class, args);
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

	@Scheduled(fixedDelay = 180000)
	public void sendMail() throws ServerException {
		if(env.getProperty("cron.enabled").compareTo("true")==0) mensajeService.tareaCorreoElectronico();
		if(env.getProperty("cron.task").compareTo("true")==0) {
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
}