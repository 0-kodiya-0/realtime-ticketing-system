package org.backend.configuration;

import jakarta.annotation.PostConstruct;
import org.bson.Document;
import org.backend.model.Admin;
import org.backend.model.Contact;
import org.backend.model.Credentials;
import org.backend.model.Customer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.CompoundIndexDefinition;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.index.PartialIndexFilter;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

@Component
public class DbIndexCreation {

    private final MongoTemplate adminMongoTemplate;
    private final MongoTemplate customerMongoTemplate;

    public DbIndexCreation(@Qualifier("adminMongoTemplate") MongoTemplate adminMongoTemplate, @Qualifier("customerMongoTemplate") MongoTemplate customerMongoTemplate) {
        this.adminMongoTemplate = adminMongoTemplate;
        this.customerMongoTemplate = customerMongoTemplate;
    }

    @PostConstruct
    public void adminIndexesInit() {
        initIndex(adminMongoTemplate, Admin.class);
    }

    @PostConstruct
    public void customerIndexesInit() {
        initIndex(customerMongoTemplate, Customer.class);
    }

    private void initIndex(MongoTemplate mongoTemplate, Class<?> userTypeClass) {
        IndexOperations adminIndexOps = mongoTemplate.indexOps(userTypeClass);
        IndexOperations credentialsIndexOps = mongoTemplate.indexOps(Credentials.class);
        IndexOperations contactIndexOps = mongoTemplate.indexOps(Contact.class);
        adminIndexOps.ensureIndex(new Index().on("nic", org.springframework.data.domain.Sort.Direction.ASC).unique().partial(PartialIndexFilter.of(Criteria.where("nic").exists(true))));
        adminIndexOps.ensureIndex(new CompoundIndexDefinition(new Document().append("name", 1).append("surname", 1)).unique());
        credentialsIndexOps.ensureIndex(new Index().on("userName", org.springframework.data.domain.Sort.Direction.ASC).unique());
        contactIndexOps.ensureIndex(new Index().on("email", org.springframework.data.domain.Sort.Direction.ASC).unique().on("phoneNumber", org.springframework.data.domain.Sort.Direction.ASC));
    }
}