package com.accounting;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.init.ScriptException;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = "com.accounting.*.infrastructure", annotationClass= AccountingConnMapper.class, sqlSessionFactoryRef = "accountingSqlSessionFactory")
public class AccountingConfiguration {

	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() {
		// Obtengo version actual
		DataSource ds = dynamicDataSource();
		String actualString = getActualDate(ds);
		System.out.println("Fecha actual = " + actualString);
		if (actualString == null) {
			System.out.println("*********************************************************");
			System.out.println("*******              INDICADORES                 ********");
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
			System.out.println("*******               INDICADORES                ********");
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
		System.out.println("******      COMIENZA A ACTUALIZAR  INDICADORES   ********");
		System.out.println("*********************************************************");
		String sqlName;
		boolean error = false;
		while (iterador.getTime().getTime() < new Date().getTime() && !error) {
			sqlName = "static/accounting/" + String.valueOf(iterador.get(Calendar.YEAR));
			sqlName = sqlName + "/" + String.valueOf(iterador.get(Calendar.YEAR))
					+ to2String(iterador.get(Calendar.MONTH) + 1);
			sqlName = sqlName + "/" + String.valueOf(iterador.get(Calendar.YEAR))
					+ to2String(iterador.get(Calendar.MONTH) + 1) + to2String(iterador.get(Calendar.DAY_OF_MONTH))
					+ ".sql";
			// System.out.println("Buscando Script = " + sqlName );
			Resource fileSql = new ClassPathResource(sqlName);
			if (fileSql.exists()) {
				System.out.println("**************Ejecutando Script = " + sqlName);
				error = new TransactionTemplate(transactionManager(ds)).execute((ts) -> {
					Connection conn = null;
					boolean fallaScript = true;
					try {
						conn = ds.getConnection();
						conn.setAutoCommit(false);
						ScriptUtils.executeSqlScript(conn, new EncodedResource(fileSql, "UTF-8"));
						conn.commit();
						fallaScript = false;
					} catch (ScriptException | SQLException e) {
						System.out.println(e.getMessage());
						try {
							conn.rollback();
						} catch (SQLException e1) {
							System.out.println(e1.getMessage());
						}
					} finally {
						if (conn != null) {
							try {
								conn.close();
							} catch (SQLException e1) {
								System.out.println(e1.getMessage());
							}
						}
						;
					}
					return fallaScript;
				});
			}
			iterador.add(Calendar.DAY_OF_MONTH, 1);
		}

		if (!error) {
			System.out.println("*******OKOKOKOKOKOKOKOKOKOKOKOKOKOKOKOKOOKOKOKOKO********");
			System.out.println("*******                                          ********");
			System.out.println("*******     LO HEMOS LOGRADO TODO INDICADORES    ********");
			System.out.println("*******                                          ********");
			System.out.println("****************:)****:)***:)***:)***:)******************");
			System.out.println("*********************************************************");
		} else {
			System.out.println("*********************************************************");
			System.out.println("*******     ERROR       INDICADORES     ERROR    ********");
			System.out.println("*******     LLAMA YA AL SOFTWARE PARA TI .COM    ********");
			System.out.println("*******                                          ********");
			System.out.println("********!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!********");
			System.out.println("********XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX*********");
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

	public String to2String(int number) {
		if (number < 10) {
			return "0" + String.valueOf(number);
		}
		return String.valueOf(number);
	}

	@Bean(name = "accountingDataSource")
	@ConfigurationProperties(prefix = "db.accounting")
	DataSource dynamicDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "accountingSqlSessionFactory")
	SqlSessionFactory sqlSessionFactory(@Qualifier("accountingDataSource") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(dataSource);
		factoryBean.setMapperLocations(
				new PathMatchingResourcePatternResolver().getResources("classpath*:/com/accounting/*/*.xml"));
		factoryBean.setTypeAliasesPackage("com.accounting.*.domain");
		return factoryBean.getObject();
	}

    @Bean(name = "accountingSQLSessionTemplate")
    SqlSessionTemplate accountingSqlSessionTemplate(
            @Qualifier("accountingSqlSessionFactory") SqlSessionFactory accountingSessionTemplate) {
		return new SqlSessionTemplate(accountingSessionTemplate);
	}

	@Bean(name = "accountingTransactionManager")
	DataSourceTransactionManager transactionManager(@Qualifier("accountingDataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
}