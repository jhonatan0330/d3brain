package d3;

import java.util.Date;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import d3.accounting.application.StackAccountProccessService;
import d3.shared.domain.ServerException;
import d3.mail.application.MailReleaseMessageQueueService;
import d3.multitenancy.TenantIteratorService;
import d3.process.application.ProcesoTransicionAutomaticaSvc;
import d3.report.ReporteServlet;
import d3.report.application.ReporteBaseSvc;
import d3.webservice.application.WebServiceEjecucionSvc;

import jakarta.servlet.http.HttpServlet;

@Configuration
@EnableTransactionManagement
@EnableScheduling
@ConfigurationPropertiesScan
@MapperScan(basePackages = {
		"d3.*.infrastructure" }, annotationClass = D3SqlConnMapper.class, sqlSessionFactoryRef = "sqlSessionFactory")
public class D3Configuration {

	private final Environment env;

	private MailReleaseMessageQueueService releaseQueueService;
	private ProcesoTransicionAutomaticaSvc transicionservice;
	private WebServiceEjecucionSvc apiService;
	private StackAccountProccessService accountService;
	private ReporteBaseSvc reporteBaseService;

	private final TenantIteratorService tenantIteratorService;
	private final AutowireCapableBeanFactory beanFactory;

	public D3Configuration(@Lazy MailReleaseMessageQueueService mail, @Lazy ProcesoTransicionAutomaticaSvc auto,
			@Lazy WebServiceEjecucionSvc apis, @Lazy ReporteBaseSvc report, @Lazy StackAccountProccessService stack,
			Environment env, @Lazy TenantIteratorService tenantIteratorService,
			AutowireCapableBeanFactory beanFactory) {
		this.releaseQueueService = mail;
		this.transicionservice = auto;
		this.apiService = apis;
		this.reporteBaseService = report;
		this.accountService = stack;
		this.env = env;
		this.tenantIteratorService = tenantIteratorService;
		this.beanFactory = beanFactory;
	}

	@Bean(name = "sqlSessionFactory")
	SqlSessionFactory sqlSessionFactory(@Lazy @Qualifier("dataSource") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(dataSource);
		factoryBean.setTypeAliasesPackage("d3.*.domain");// Specify base package
		factoryBean.setMapperLocations(
				new PathMatchingResourcePatternResolver().getResources("classpath*:/d3/*/*.xml"));//
		return factoryBean.getObject();
	}

	@Scheduled(fixedDelayString = "${cron.fixedDelayMail}")
	public void sendMail() throws ServerException {

		if (!"true".equals(env.getProperty("cron.enabled")))
			return;
		tenantIteratorService.executeForAllTenants(tenantId -> {
			System.out.println(
					"******* CORREOS tenant=" + tenantId + " (" + releaseQueueService.call() + ") ***" + new Date());
		});

	}

	@Scheduled(fixedDelayString = "${cron.fixedDelayTask}")
	public void sendTemporizer() throws ServerException {

		if (!"true".equals(env.getProperty("cron.task")))
			return;
		tenantIteratorService.executeForAllTenants(tenantId -> {
			System.out.println("*******TAREAS tenant=" + tenantId + " ("
					+ transicionservice.lanzarTransaccionesTemporizadas() + ") ***" + new Date());
			System.out.println("*******TAREAS PROGRAMADAS tenant=" + tenantId + " (" + transicionservice.programateAll()
					+ ") ***" + new Date());
		});

	}

	String executeAPITask;

	@Scheduled(fixedDelayString = "${cron.fixedDelayApi}")
	public void sendAPI() throws ServerException {
		if (!"true".equals(env.getProperty("cron.api")))
			return;
		tenantIteratorService.executeForAllTenants(tenantId -> apiService.apiToTransaction());

	}

	@Scheduled(fixedDelayString = "${cron.fixedDelayAccount}")
	public void sendAccount() throws ServerException {
		if (!"true".equals(env.getProperty("cron.account")))
			return;
		tenantIteratorService.executeForAllTenants(tenantId -> System.out.println(
				"******* ACUMULADOR tenant=" + tenantId + " (" + accountService.call() + ") ***" + new Date()));

	}

	@Bean
	ServletRegistrationBean<HttpServlet> reporteServlet() {
		ServletRegistrationBean<HttpServlet> servRegBean = new ServletRegistrationBean<>();
		final ReporteServlet servlet = new ReporteServlet(reporteBaseService);
		beanFactory.autowireBean(servlet);
		servRegBean.setServlet(servlet);
		servRegBean.addUrlMappings("/reporte/*", "/r/*");
		servRegBean.setLoadOnStartup(1);
		return servRegBean;
	}

}