package org.example.opp_cw.services;

import org.bson.types.ObjectId;
import org.example.opp_cw.model.Admin;
import org.example.opp_cw.model.Contact;
import org.example.opp_cw.model.Credentials;
import org.example.opp_cw.repository.admin.AdminContactRepository;
import org.example.opp_cw.repository.admin.AdminCredentialsRepository;
import org.example.opp_cw.repository.admin.AdminRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    private final AdminRepository adminRepository;
    private final AdminCredentialsRepository adminCredentialsRepository;
    private final AdminContactRepository adminContactRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AdminService(AdminRepository adminRepository, AdminCredentialsRepository adminCredentialsRepository, AdminContactRepository adminContactRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.adminRepository = adminRepository;
        this.adminCredentialsRepository = adminCredentialsRepository;
        this.adminContactRepository = adminContactRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void saveAdmin(Admin admin, Credentials credentials, Contact contact) {
        ObjectId id = ObjectId.get();
        admin.set_id(id);
        credentials.setPassword(bCryptPasswordEncoder.encode(credentials.getPassword()));
        credentials.set_id(id);
        contact.set_id(id);
        adminCredentialsRepository.insert(credentials);
        adminContactRepository.insert(contact);
        adminRepository.insert(admin);
    }

    public List<Admin> findAll() {
        return adminRepository.findAll();
    }
}
