package org.example.opp_cw.configuration;

import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "org.example.opp_cw.repository.customer" , mongoTemplateRef = "customerMongoTemplate")
public class CustomerDbConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "spring.data.mongodb.customer")
    public MongoProperties customerMongoProperties() {
        return new MongoProperties();
    }

    @Bean
    public MongoTemplate customerMongoTemplate() {
        return new MongoTemplate(customerMongoDbFactory());
    }

    @Bean
    public MongoDatabaseFactory customerMongoDbFactory() {
        return new SimpleMongoClientDatabaseFactory(customerMongoProperties().getUri());
    }
}
