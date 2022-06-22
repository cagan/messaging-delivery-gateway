package com.cagan.messaginggateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
@Import(SecurityProblemSupport.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    public CustomAuthenticationProvider customAuthenticationProvider;

    private final SecurityProblemSupport problemSupport;
    private final AuthTokenUserDetailService authTokenUserDetailService;
    private final CustomAuthenticationManager customAuthenticationManager;

    @Autowired
    public SecurityConfiguration(SecurityProblemSupport problemSupport, AuthTokenUserDetailService authTokenUserDetailService, CustomAuthenticationManager customAuthenticationManager) {
        this.problemSupport = problemSupport;
        this.authTokenUserDetailService = authTokenUserDetailService;
        this.customAuthenticationManager = customAuthenticationManager;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(customAuthenticationProvider)
//                .authenticationProvider(new DaoAuthenticationProvider())
//                .userDetailsService(authTokenUserDetailService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Other security-related configuration
        AuthTokenFilter authTokenFilter = new AuthTokenFilter();
        authTokenFilter.setAuthenticationManager(customAuthenticationManager);

        http.csrf()
                .disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(problemSupport)
                .accessDeniedHandler(problemSupport)
                .and().addFilter(authTokenFilter)
                .antMatcher("/**").authorizeRequests()
                .and()
                .authorizeRequests().anyRequest().authenticated();
    }
}
