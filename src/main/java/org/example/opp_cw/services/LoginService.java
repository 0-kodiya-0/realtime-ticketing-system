package org.example.opp_cw.services;

import jakarta.servlet.http.HttpServletRequest;
import org.example.opp_cw.dto.requestbody.LoginRequest;
import org.example.opp_cw.dto.responsebody.ApiResponse;
import org.example.opp_cw.model.Credentials;
import org.example.opp_cw.repository.CredentialsRepository;
import org.example.opp_cw.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private CredentialsRepository credentialsRepository;
    @Autowired
    private CustomerRepository customerRepository;

    public ApiResponse login(LoginRequest loginRequest, HttpServletRequest request) {
        try {
            Credentials credentials = credentialsRepository.findByUserName(loginRequest.getCredentials().getUserName());
            if (credentials == null) {
                return new ApiResponse(HttpStatus.NOT_FOUND, "credentials not found", request);
            }
            if (!customerRepository.existsById(credentials.get_id())) {
                return new ApiResponse(HttpStatus.NOT_FOUND, "customer not found", request);
            }
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if (!encoder.matches(loginRequest.getCredentials().getPassword(), credentials.getPassword())) {
                return new ApiResponse(HttpStatus.UNAUTHORIZED, "password invalid", request);
            }
            return new ApiResponse(HttpStatus.OK, "login success full", request);
        } catch (IncorrectResultSizeDataAccessException e) {
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal db server error", request);
        }
    }
}
