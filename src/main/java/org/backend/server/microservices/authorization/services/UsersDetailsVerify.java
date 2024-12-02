package org.backend.server.microservices.authorization.services;

import javax.security.auth.login.AccountException;

public interface UsersDetailsVerify {
    boolean exists(String username);

    boolean exists(Long id);

    boolean isVerified(String username) throws AccountException;

    boolean isVerified(Long id) throws AccountException;

    void verifyUser(String username) throws AccountException;

    void verifyUser(Long id) throws AccountException;
}
