package org.example.opp_cw.services;

import org.example.opp_cw.model.Admin;
import org.example.opp_cw.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;

    public void saveAdmin(Admin entity) {
        adminRepository.save(entity);
    }

    public List<Admin> findAll() {
        return adminRepository.findAll();
    }
}
