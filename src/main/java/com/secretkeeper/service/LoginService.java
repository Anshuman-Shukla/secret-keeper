package com.secretkeeper.service;

import com.secretkeeper.constants.SecurityConstants;
import com.secretkeeper.entity.Authority;
import com.secretkeeper.entity.CustomerDetail;
import com.secretkeeper.model.LoginDetail;
import com.secretkeeper.model.LoginResponse;
import com.secretkeeper.repository.CustomerDetailRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class LoginService {
    @Autowired
    private CustomerDetailRepository repo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Transactional
    public CustomerDetail saveCustomerDetail(CustomerDetail detail) {
        String hashPwd = passwordEncoder.encode(detail.getPwd());
        detail.setPwd(hashPwd);
        // Set customer reference in each authority
        Set<Authority> authorities = detail.getAuthorities();
        if (authorities != null) {
            authorities.forEach(authority -> authority.setCustomer(detail));
        }
        try {

            return repo.save(detail);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public LoginResponse getToken(LoginDetail detail) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(detail.getEmail()
                , detail.getPassword());
        Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
        if (null != authentication) {
            return getJwtToken(authentication);
        }
        return null;
    }

    private LoginResponse getJwtToken(Authentication authentication) {
        LoginResponse response = new LoginResponse();
        Date currentDate = new Date();
        long futureTimeInMillis = currentDate.getTime() + 30000000L;
        Date futureDate = new Date(futureTimeInMillis);
        SecretKey key = Keys.hmacShaKeyFor(SecurityConstants.SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        String jwt = Jwts.builder().issuer(SecurityConstants.ISSUER).subject(SecurityConstants.SUBJECT)
                .claim(SecurityConstants.USERNAME, authentication.getName())
                .claim(SecurityConstants.AUTHORITIES, populateAuthorities(authentication.getAuthorities()))
                .issuedAt(new Date())
                .expiration(futureDate)
                .signWith(key).compact();
        response.setUser(authentication.getName());
        response.setTokenIssuedTime(currentDate);
        response.setTokenExpirationTime(futureDate);
        response.setToken(jwt);
        response.setUserId(getUserId(authentication.getName()));
        return response;
    }
private Integer getUserId(String userName){
    int id=0;
    List<CustomerDetail> customerDetails=repo.findByEmail(userName);
    if(customerDetails.size()>0){
        id = customerDetails.get(0).getId();
    }
    return id;
}
    private String populateAuthorities(Collection<? extends GrantedAuthority> collection) {
        Set<String> authoritiesSet = new HashSet<>();
        for (GrantedAuthority authority : collection) {
            authoritiesSet.add(authority.getAuthority());
        }
        return String.join(",", authoritiesSet);
    }
}
