package org.example.opp_cw.configuration;

import org.example.opp_cw.repository.AdminRepository;
import org.example.opp_cw.repository.CustomerRepository;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackageClasses = {CustomerRepository.class, AdminRepository.class}, mongoTemplateRef = "usersMongoTemplate")
@EnableConfigurationProperties
public class UserDbConfiguration {

    @Primary
    @Bean(name = "usersDBProperties")
    @ConfigurationProperties(prefix = "spring.data.mongodb.user")
    public MongoProperties getUsersProps() throws Exception {
        return new MongoProperties();
    }

    @Bean
    public MongoDatabaseFactory usersMongoDatabaseFactory(MongoProperties mongo) throws Exception {
        return new SimpleMongoClientDatabaseFactory(
                mongo.getUri()
        );
    }

    @Bean(name = "usersMongoTemplate")
    public MongoTemplate usersMongoTemplate() throws Exception {
        return new MongoTemplate(usersMongoDatabaseFactory(getUsersProps()));
    }
}

