package com.gooood.GooooCoin.conf;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Configuration
public class Neo4jConfig {
	@Value("${spring.neo4j.url}")
	private String url;

	@Value("${spring.neo4j.username}")
	private String username;

	@Value("${spring.neo4j.password}")
	private String password;

	/**
	 * 图数据库驱动模式
	 */

	@Bean
	public Driver neo4jDriver() {
		return GraphDatabase.driver(url, AuthTokens.basic(username, password));
	}



}