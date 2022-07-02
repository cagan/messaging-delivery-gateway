package com.cagan.messaginggateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
@Import(SecurityProblemSupport.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final SecurityProblemSupport problemSupport;
    private final ClientUserDetailService clientUserDetailService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationProvider authTokenAuthenticationProvider;
    private final UserAuthFilter userAuthFilter;

    @Autowired
    public SecurityConfiguration(SecurityProblemSupport problemSupport,
                                 ClientUserDetailService authTokenUserDetailService,
                                 PasswordEncoder passwordEncoder,
                                 AuthenticationProvider authTokenAuthenticationProvider,
                                 UserAuthFilter userAuthFilter
    ) {
        this.problemSupport = problemSupport;
        this.clientUserDetailService = authTokenUserDetailService;
        this.passwordEncoder = passwordEncoder;
        this.authTokenAuthenticationProvider = authTokenAuthenticationProvider;
        this.userAuthFilter = userAuthFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.parentAuthenticationManager(authenticationManagerBean())
                .authenticationProvider(authTokenAuthenticationProvider)
                .userDetailsService(clientUserDetailService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    @Bean
    @Primary
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(problemSupport)
                .accessDeniedHandler(problemSupport)
                .and()
                .authorizeRequests()
                .antMatchers("/api/v1/auth").permitAll()
                .antMatchers("/api/v1/users").hasAuthority("ROLE_ADMIN")
                .anyRequest()
                .authenticated();

        http.addFilterBefore(userAuthFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
