package org.backend.server.microservices.authorization.services;

public interface UsersDetailsVerify {
    boolean exists(String username);

    boolean exists(long id);

    boolean isVerified(String username);

    boolean isVerified(long id);
}
