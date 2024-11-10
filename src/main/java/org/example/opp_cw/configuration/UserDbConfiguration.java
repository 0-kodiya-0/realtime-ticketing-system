package org.example.opp_cw.configuration;

import org.example.opp_cw.repository.AdminRepository;
import org.example.opp_cw.repository.ContactRepository;
import org.example.opp_cw.repository.CredentialsRepository;
import org.example.opp_cw.repository.CustomerRepository;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackageClasses = {CustomerRepository.class, AdminRepository.class, ContactRepository.class, CredentialsRepository.class})
@EnableConfigurationProperties
public class UserDbConfiguration extends AbstractMongoClientConfiguration {

    @Override
    protected String getDatabaseName() {
        return null;
    }

    @Primary
    @Bean(name = "usersDBProperties")
    @ConfigurationProperties(prefix = "spring.data.mongodb.user")
    public MongoProperties getUsersProps() throws Exception {
        return new MongoProperties();
    }

    @Override
    @Bean
    public MongoDatabaseFactory mongoDbFactory() {
        try {
            return new SimpleMongoClientDatabaseFactory(
                    getUsersProps().getUri()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected boolean autoIndexCreation() {
        return true;
    }
}

