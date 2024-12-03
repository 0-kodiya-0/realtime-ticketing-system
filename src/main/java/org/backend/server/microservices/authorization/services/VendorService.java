package org.backend.server.microservices.authorization.services;

import org.backend.server.microservices.authorization.enums.AccessLevel;
import org.backend.server.microservices.authorization.models.Vendor;
import org.backend.server.microservices.authorization.repository.VendorRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountException;
import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

@Service
public class VendorService implements UsersDetailsVerify {
    private final VendorRepository vendorRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public VendorService(VendorRepository vendorRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.vendorRepository = vendorRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void save(Vendor vendor) {
        vendor.getCredentials().setAuthority(List.of(new SimpleGrantedAuthority(AccessLevel.VENDOR.name())));
        vendor.getCredentials().setPassword(bCryptPasswordEncoder.encode(vendor.getCredentials().getPassword()));
        vendorRepository.save(vendor);
    }

    public Vendor findVendor(String username) {
        return vendorRepository.findByCredentialsUserNameContaining(username);
    }

    @Override
    public boolean exists(String username) {
        return vendorRepository.existsByCredentialsUserNameContaining(username);
    }

    @Override
    public boolean exists(Long id) {
        return vendorRepository.existsById(id);
    }

    @Override
    public boolean isVerified(String username) throws AccountException {
        Vendor vendor = vendorRepository.findByCredentialsUserNameContaining(username);
        if (vendor == null) {
            throw new AccountNotFoundException("Verified vendor not found");
        }
        return vendor.isVisible() && vendor.isSystemAuthorized() && vendor.isDeleted();
    }

    @Override
    public boolean isVerified(Long id) throws AccountException {
        Vendor vendor = vendorRepository.findById(id).orElse(null);
        if (vendor == null) {
            throw new AccountNotFoundException("Verified vendor not found");
        }
        return vendor.isVisible() && vendor.isSystemAuthorized() && vendor.isDeleted();
    }

    @Override
    public void verifyUser(String username) throws AccountException {
        Vendor vendor = vendorRepository.findByCredentialsUserNameContaining(username);
        verifyUser(vendor);
    }

    @Override
    public void verifyUser(Long id) throws AccountException {
        Vendor vendor = vendorRepository.findById(id).orElse(null);
        verifyUser(vendor);
    }

    private void verifyUser(Vendor vendor) throws AccountException {
        if (vendor == null) {
            throw new AccountNotFoundException("Vendor not found");
        }
        if (vendor.isDeleted() || vendor.isSystemAuthorized() || vendor.isVisible()) {
            throw new AccountException("Vendor already verified");
        }
        vendor.setVisible(true);
        vendor.setSystemAuthorized(true);
        vendorRepository.save(vendor);
    }
}
