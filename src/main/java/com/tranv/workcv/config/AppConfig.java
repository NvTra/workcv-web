package com.tranv.workcv.config;

import java.beans.PropertyVetoException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.tranv.workcv")
@PropertySource("classPath:persistence-mysql.properties")
@EnableTransactionManagement
public class AppConfig implements WebMvcConfigurer {
	@Autowired
	private Environment env;
	@Autowired
	private DataSource myDataSource;
	private Logger logger = Logger.getLogger(getClass().getName());

	/*
	 * @Bean public ViewResolver viewResolver() { InternalResourceViewResolver
	 * viewResolver = new InternalResourceViewResolver();
	 * viewResolver.setPrefix("/WEB-INF/view/"); viewResolver.setSuffix(".jsp");
	 * return viewResolver; }
	 **/
//	Define Spring MVC view Thymeleaf

	@Bean
	public SpringResourceTemplateResolver templateResolver() {
		SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
		templateResolver.setPrefix("/WEB-INF/templates/");
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateResolver.setCacheable(true);
		return templateResolver;
	}

	@Bean
	public SpringTemplateEngine templateEngine() {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver());
		templateEngine.setEnableSpringELCompiler(true);
		return templateEngine;
	}

	@Bean
	public ThymeleafViewResolver viewResolver() {
		ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
		viewResolver.setTemplateEngine(templateEngine());
		viewResolver.setCharacterEncoding("UTF-8");
		return viewResolver;
	}

//	Step 1: Define Database DataSource / connection pool
	@Bean
	public DataSource dataSource() {
		// create connection pool
		ComboPooledDataSource securityDataSource = new ComboPooledDataSource();

		// set the jdbc driver class

		try {
			securityDataSource.setDriverClass(env.getProperty("jdbc.driver"));
		} catch (PropertyVetoException exc) {
			throw new RuntimeException(exc);
		}

		// log the connection props
		// for sanity's sake, log this info
		// just to make sure we are REALLY reading data from properties file

		logger.info(">>> jdbc.url=" + env.getProperty("jdbc.url"));
		logger.info(">>> jdbc.user=" + env.getProperty("jdbc.user"));

		// set database connection props

		securityDataSource.setJdbcUrl(env.getProperty("jdbc.url"));
		securityDataSource.setUser(env.getProperty("jdbc.user"));
		securityDataSource.setPassword(env.getProperty("jdbc.password"));

		// set connection pool props

		securityDataSource.setInitialPoolSize(getIntProperty("connection.pool.initialPoolSize"));

		securityDataSource.setMinPoolSize(getIntProperty("connection.pool.minPoolSize"));

		securityDataSource.setMaxPoolSize(getIntProperty("connection.pool.maxPoolSize"));

		securityDataSource.setMaxIdleTime(getIntProperty("connection.pool.maxIdleTime"));

		return securityDataSource;
	}
	// need a helper method
	// read environment property and convert to int

	private int getIntProperty(String propName) {

		String propVal = env.getProperty(propName);

		// now convert to int
		int intPropVal = Integer.parseInt(propVal);

		return intPropVal;
	}

	// Step 2: Setup Hibernate session factory
	@Bean
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(myDataSource);
		sessionFactory.setPackagesToScan("com.tranv.workcv");
		Properties hibernateProperties = new Properties();
		hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
		hibernateProperties.setProperty("hibernate.show_sql", "true");
		sessionFactory.setHibernateProperties(hibernateProperties);
		return sessionFactory;
	}

	// Step 3: Setup Hibernate transaction manager
	@Bean
	public HibernateTransactionManager myTransactionManager() {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
		transactionManager.setSessionFactory(sessionFactory().getObject());
		return transactionManager;
	}

	// Step 4: Enable configuration of transactional behavior based on annotations
	@Bean
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		resolver.setDefaultEncoding("utf-8");
		return resolver;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}

}
