package org.backend.services;

import com.mongodb.client.result.UpdateResult;
import org.bson.types.ObjectId;
import org.backend.enums.AccessLevel;
import org.backend.model.Admin;
import org.backend.model.Contact;
import org.backend.model.Credentials;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MongoTemplate adminMongoTemplate;

    public AdminService(BCryptPasswordEncoder bCryptPasswordEncoder, @Qualifier("adminMongoTemplate") MongoTemplate adminMongoTemplate) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.adminMongoTemplate = adminMongoTemplate;
    }

    public ObjectId saveAdmin(Admin admin) {
        ObjectId id = ObjectId.get();
        admin.set_id(id);
        adminMongoTemplate.insert(admin);
        return id;
    }

    public void saveAdminCredentials(ObjectId objectId, Credentials credentials) {
        credentials.set_id(objectId);
        credentials.setAuthority(List.of(new SimpleGrantedAuthority(AccessLevel.ADMIN.name())));
        credentials.setPassword(bCryptPasswordEncoder.encode(credentials.getPassword()));
        adminMongoTemplate.insert(credentials);
    }

    public void saveAdminContact(ObjectId objectId, Contact contact) {
        contact.set_id(objectId);
        adminMongoTemplate.insert(contact);
    }

    public boolean verifyAdmin(ObjectId objectId) {
        UpdateResult updateResult = adminMongoTemplate.updateFirst(
                new Query(
                        new Criteria()
                                .andOperator(
                                        Criteria.where("_id").is(objectId),
                                        Criteria.where("isSystemAuthorized").is(false),
                                        Criteria.where("isVisible").is(false)
                                )
                ),
                new Update()
                        .set("isSystemAuthorized", true)
                        .set("isVisible", true),
                Admin.class);
        return updateResult.getMatchedCount() == 1;
    }

    public boolean isAdmin(ObjectId id) {
        return adminMongoTemplate.findById(id, Admin.class) != null && adminMongoTemplate.findById(id, Credentials.class) != null && adminMongoTemplate.findById(id, Contact.class) != null;
    }

    public boolean isAdminVerified(ObjectId id) {
        return adminMongoTemplate.exists(
                new Query(
                        new Criteria()
                                .andOperator(
                                        Criteria.where("_id").is(id),
                                        Criteria.where("isSystemAuthorized").is(true),
                                        Criteria.where("isVisible").is(true)
                                )
                ), Admin.class);
    }

    public Credentials isAdmin(String userName) {
        return adminMongoTemplate.findOne(new Query(Criteria.where("userName").is(userName)), Credentials.class);
    }

    public List<Admin> findAll() {
        return adminMongoTemplate.findAll(Admin.class);
    }
}
