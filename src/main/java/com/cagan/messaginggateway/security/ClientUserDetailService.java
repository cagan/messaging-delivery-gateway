package com.cagan.messaginggateway.security;

import com.cagan.messaginggateway.domain.User;
import com.cagan.messaginggateway.repository.UserAuthorizationRepository;
import com.cagan.messaginggateway.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ClientUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        return new ClientUserDetails(user);
    }
}
