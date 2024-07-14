package com.secretkeeper.config;

import com.secretkeeper.entity.Authority;
import com.secretkeeper.entity.CustomerDetail;
import com.secretkeeper.repository.CustomerDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class UserNamePasswordAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private CustomerDetailRepository repository;

    @Autowired
    private PasswordEncoder encoder;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName=authentication.getName();
        String pwd=authentication.getCredentials().toString();
        List<CustomerDetail> customerDetails=repository.findByEmail(userName);
        if(customerDetails.size()>0){
            if(encoder.matches(pwd,customerDetails.get(0).getPwd())){
                return new UsernamePasswordAuthenticationToken(userName,pwd,getGrantedAuthorities(customerDetails.get(0).getAuthorities()));
            }else {
                throw new BadCredentialsException("Invalid UserName or Password");
            }
        }else{
            throw new BadCredentialsException("No User registered with this details");
        }

    }
    private List<GrantedAuthority> getGrantedAuthorities(Set<Authority> authorities) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (Authority authority : authorities) {
            grantedAuthorities.add(new SimpleGrantedAuthority(authority.getName()));
        }
        return grantedAuthorities;
    }
    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
