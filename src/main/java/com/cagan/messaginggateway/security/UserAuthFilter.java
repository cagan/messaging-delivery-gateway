package com.cagan.messaginggateway.security;

import com.cagan.messaginggateway.domain.UserAuthorization;
import com.cagan.messaginggateway.repository.UserAuthorizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

@Order(1)
@Component
public class UserAuthFilter extends OncePerRequestFilter {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    @Autowired
    private ClientUserDetailService clientUserDetailService;
    @Autowired
    private UserAuthorizationRepository userAuthorizationRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken(request);

        if (StringUtils.hasText(token) && validateToken(token)) {
            userAuthorizationRepository.findByAuthorizationToken(token)
                    .ifPresent((userAuthorization) -> {
                        UserDetails userDetails = clientUserDetailService.loadUserByUsername(userAuthorization.getUser().getUsername());
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    });
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private boolean validateToken(String token) {
        Optional<UserAuthorization> authorization = userAuthorizationRepository.findByAuthorizationToken(token);
        if (authorization.isEmpty()) {
            return false;
        }

        if (authorization.get().getUser() == null) {
            return false;
        }

        if (authorization.get().getExpireTime().compareTo(Instant.now()) < 0) {
            userAuthorizationRepository.delete(authorization.get());
            return false;
        }

        return true;
    }
}
