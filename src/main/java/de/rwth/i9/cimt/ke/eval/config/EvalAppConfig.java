package de.rwth.i9.cimt.ke.eval.config;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import de.tudarmstadt.ukp.wikipedia.api.DatabaseConfiguration;
import de.tudarmstadt.ukp.wikipedia.api.Wikipedia;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiInitializationException;

public class EvalAppConfig {

	private static final Logger log = LoggerFactory.getLogger(EvalAppConfig.class);
	private @Value("${cimt.wikipedia.en.sql.host}") String wikipediaSqlHost;
	private @Value("${cimt.wikipedia.en.sql.database}") String wikipediaSqlDatabase;
	private @Value("${cimt.wikipedia.en.sql.user}") String wikipediaSqlUser;
	private @Value("${cimt.wikipedia.en.sql.password}") String wikipediaSqlPassword;
	private @Value("${cimt.wikipedia.en.language}") String wikipediaSqlLanguage;

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}


	@Bean("simpleWikiDb")
	public Wikipedia getWikipedia() {
		// configure the database connection parameters
		DatabaseConfiguration dbConfig = new DatabaseConfiguration();
		dbConfig.setHost(wikipediaSqlHost);
		dbConfig.setDatabase(wikipediaSqlDatabase);
		dbConfig.setUser(wikipediaSqlUser);
		dbConfig.setPassword(wikipediaSqlPassword);
		dbConfig.setLanguage(de.tudarmstadt.ukp.wikipedia.api.WikiConstants.Language.valueOf(wikipediaSqlLanguage));

		Wikipedia wiki = null;
		try {
			wiki = new Wikipedia(dbConfig);
		} catch (WikiInitializationException e) {
			log.error(ExceptionUtils.getStackTrace(e));
		}

		return wiki;
	}



}
