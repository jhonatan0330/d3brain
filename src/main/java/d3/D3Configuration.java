package d3;

import java.util.Date;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import d3.accounting.application.StackAccountProccessService;
import d3.authentication.application.UsuarioSesionSvc;
import d3.authentication.infrastructure.SessionFilter;
import d3.mail.application.MailReleaseMessageQueueService;
import d3.multitenancy.application.TenantIteratorService;
import d3.process.application.ProcesoTransicionAutomaticaSvc;
import d3.report.ReporteServlet;
import d3.report.application.ReporteBaseSvc;
import d3.shared.application.SessionContext;
import d3.shared.application.SharedTokenService;
import d3.usage.application.ConsumoUnidadProcesoService;
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
	private final SharedTokenService sharedTokenService;

	private final TenantIteratorService tenantIteratorService;
	private final AutowireCapableBeanFactory beanFactory;

	private final ConsumoUnidadProcesoService consumoProcesoService;
	private final UsuarioSesionSvc autenticacionService;

	public D3Configuration(@Lazy MailReleaseMessageQueueService mail, @Lazy ProcesoTransicionAutomaticaSvc auto,
			@Lazy WebServiceEjecucionSvc apis, @Lazy ReporteBaseSvc report, @Lazy StackAccountProccessService stack,
			Environment env, @Lazy TenantIteratorService tenantIteratorService, AutowireCapableBeanFactory beanFactory,
			@Lazy ConsumoUnidadProcesoService consumoProcesoService, SharedTokenService sharedTokenService,
			@Lazy UsuarioSesionSvc autenticacionService) {
		this.releaseQueueService = mail;
		this.transicionservice = auto;
		this.apiService = apis;
		this.reporteBaseService = report;
		this.accountService = stack;
		this.env = env;
		this.tenantIteratorService = tenantIteratorService;
		this.beanFactory = beanFactory;
		this.consumoProcesoService = consumoProcesoService;
		this.sharedTokenService = sharedTokenService;
		this.autenticacionService = autenticacionService;
	}

	@Bean(name = "sqlSessionFactory")
	SqlSessionFactory sqlSessionFactory(@Lazy @Qualifier("dataSource") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(dataSource);
		factoryBean.setTypeAliasesPackage("d3.*.domain");// Specify base package
		factoryBean
				.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:/d3/*/*.xml"));//
		return factoryBean.getObject();
	}

	@Scheduled(fixedDelayString = "${cron.fixedDelayMail}")
	public void sendMail() {

		if (!"true".equals(env.getProperty("cron.enabled")))
			return;
		tenantIteratorService.executeForAllTenants(tenantId -> {
			String token = autenticacionService.generateAdministratorToken().getLlaveTabla();
			SessionContext.setCurrent(autenticacionService.getUserToken(token));

			System.out.println(
					"******* CORREOS tenant=" + tenantId + " (" + releaseQueueService.call() + ") ***" + new Date());
		});

	}

	@Scheduled(fixedDelayString = "${cron.fixedDelayTask}")
	public void sendTemporizer() {

		if (!"true".equals(env.getProperty("cron.task")))
			return;
		tenantIteratorService.executeForAllTenants(tenantId -> {
			String token = autenticacionService.generateAdministratorToken().getLlaveTabla();
			SessionContext.setCurrent(autenticacionService.getUserToken(token));

			System.out.println("*******TAREAS tenant=" + tenantId + " ("
					+ transicionservice.lanzarTransaccionesTemporizadas() + ") ***" + new Date());
			System.out.println("*******TAREAS PROGRAMADAS tenant=" + tenantId + " (" + transicionservice.programateAll()
					+ ") ***" + new Date());
		});

	}

	String executeAPITask;

	@Scheduled(fixedDelayString = "${cron.fixedDelayApi}")
	public void sendAPI() {
		if (!"true".equals(env.getProperty("cron.api")))
			return;
		tenantIteratorService.executeForAllTenants(tenantId -> {
			String token = autenticacionService.generateAdministratorToken().getLlaveTabla();
			SessionContext.setCurrent(autenticacionService.getUserToken(token));
	
				apiService.apiToTransaction();
		});

	}

	@Scheduled(fixedDelayString = "${cron.fixedDelayAccount}")
	public void sendAccount() {
		if (!"true".equals(env.getProperty("cron.account")))
			return;
		tenantIteratorService.executeForAllTenants(tenantId -> {
			String token = autenticacionService.generateAdministratorToken().getLlaveTabla();
			SessionContext.setCurrent(autenticacionService.getUserToken(token));
			System.out.println(
				"******* ACUMULADOR tenant=" + tenantId + " (" + accountService.call() + ") ***" + new Date());
		});

	}

	@Scheduled(cron = "${cron.cronHistorico}")
	public void sendHistorico() {
		if (!"true".equals(env.getProperty("cron.historico")))
			return;
		tenantIteratorService.executeForAllTenants(tenantId -> {
			String token = autenticacionService.generateAdministratorToken().getLlaveTabla();
			SessionContext.setCurrent(autenticacionService.getUserToken(token));
			System.out.println("******* HISTORICO tenant=" + tenantId
				+ " (" + transicionservice.moverDatosHistoricos() + ") ***" + new Date());
		});
			

	}

	@Scheduled(cron = "${cron.cronConsumo}")
	public void sendConsumo() {
		if (!"true".equals(env.getProperty("cron.consumo")))
			return;
		tenantIteratorService.executeForAllTenants(tenantId -> {
			String token = autenticacionService.generateAdministratorToken().getLlaveTabla();
			SessionContext.setCurrent(autenticacionService.getUserToken(token));
			System.out.println("******* CONSUMO UNIDADES tenant="
				+ tenantId + " (" + consumoProcesoService.procesarConsumoHorario() + ") ***" + new Date());
		});

	}

	@Scheduled(cron = "${cron.cronDiario}")
	public void sendConsumoDiario() {
		if (!"true".equals(env.getProperty("cron.diario")))
			return;
		tenantIteratorService.executeForAllTenants(tenantId -> {
			String token = autenticacionService.generateAdministratorToken().getLlaveTabla();
			SessionContext.setCurrent(autenticacionService.getUserToken(token));
			System.out.println("******* CONSUMO DIARIO tenant="
				+ tenantId + " (" + consumoProcesoService.procesarIncrementoDiario() + ") ***" + new Date());
		});
	}

	@Bean
	ServletRegistrationBean<HttpServlet> reporteServlet() {
		ServletRegistrationBean<HttpServlet> servRegBean = new ServletRegistrationBean<>();
		final ReporteServlet servlet = new ReporteServlet(reporteBaseService, sharedTokenService);
		beanFactory.autowireBean(servlet);
		servRegBean.setServlet(servlet);
		servRegBean.addUrlMappings("/reporte/*", "/r/*");
		servRegBean.setLoadOnStartup(1);
		return servRegBean;
	}

	@Bean
	SessionFilter sessionFilter(@Lazy SharedTokenService sharedTokenService) {
		return new SessionFilter(sharedTokenService);
	}

	@Bean
	FilterRegistrationBean<SessionFilter> sessionFilterRegistration(SessionFilter sessionFilter) {
		FilterRegistrationBean<SessionFilter> reg = new FilterRegistrationBean<>();
		reg.setFilter(sessionFilter);
		reg.addUrlPatterns("/*");
		reg.setOrder(Ordered.HIGHEST_PRECEDENCE + 11);
		return reg;
	}

}