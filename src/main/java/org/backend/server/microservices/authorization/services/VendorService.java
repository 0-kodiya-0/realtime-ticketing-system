package org.backend.server.microservices.authorization.services;

import org.backend.server.microservices.authorization.models.Vendor;
import org.backend.server.microservices.authorization.repository.VendorRepository;
import org.springframework.stereotype.Service;

@Service
public class VendorService {
    private final VendorRepository vendorRepository;

    public VendorService(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    public void save(Vendor vendor) {
        vendor.setVisible(true);
        vendorRepository.save(vendor);
    }

    public Vendor findVendor(long id) {
        return vendorRepository.findById(id);
    }

    public boolean exists(long id) {
        return vendorRepository.existsById(id);
    }
}
