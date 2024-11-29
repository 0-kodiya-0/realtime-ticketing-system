package org.backend.enums;

import lombok.Getter;

@Getter
public enum AccessLevel {
    ROOT, ADMIN, CUSTOMER, VENDOR, SIGNUP, LOGIN;
}
