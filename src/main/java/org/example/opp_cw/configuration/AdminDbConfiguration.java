package org.example.opp_cw.configuration;

import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "org.example.opp_cw.repository.admin", mongoTemplateRef = "adminMongoTemplate")
public class AdminDbConfiguration {
    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.data.mongodb.admin")
    public MongoProperties adminMongoProperties() {
        return new MongoProperties();
    }

    @Primary
    @Bean
    public MongoTemplate adminMongoTemplate() {
        return new MongoTemplate(adminMongoDbFactory());
    }

    @Primary
    @Bean
    public MongoDatabaseFactory adminMongoDbFactory() {
        return new SimpleMongoClientDatabaseFactory(adminMongoProperties().getUri());
    }
}
