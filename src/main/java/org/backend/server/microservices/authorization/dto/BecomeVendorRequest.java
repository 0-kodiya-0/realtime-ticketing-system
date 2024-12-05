package org.backend.server.microservices.authorization.dto;

import lombok.Data;
import org.backend.server.microservices.authorization.models.Vendor;

@Data
public class BecomeVendorRequest {
    private Long customerId;
    private Vendor vendor;
}
