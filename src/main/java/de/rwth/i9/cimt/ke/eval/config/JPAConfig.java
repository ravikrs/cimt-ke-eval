package de.rwth.i9.cimt.ke.eval.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan({ "de.rwth.i9.cimt.**" })
@PropertySource("classpath:opennlp.properties")
@Lazy(true)
@EnableJpaRepositories(basePackages = { "de.rwth.i9.cimt.ke.eval.**" })
public class JPAConfig {
	@Bean
	public DataSource dataSource(Environment env) {

		DriverManagerDataSource ds = new DriverManagerDataSource(env.getProperty("spring.datasource.url"),
				env.getProperty("spring.datasource.username"), env.getProperty("spring.datasource.password"));
		return ds;
	}

}
