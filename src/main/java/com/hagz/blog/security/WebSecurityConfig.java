package com.hagz.blog.security;

import com.hagz.blog.security.jwt.AuthEntryPointJwt;
import com.hagz.blog.security.jwt.AuthTokenFilter;
import com.hagz.blog.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/*
Web Security configuration class that enlist specific security actions
when called upon by Spring
*/
@Configuration
@EnableGlobalMethodSecurity(
        // securedEnabled = true,
        // jsr250Enabled = true,
        prePostEnabled = true)
public class WebSecurityConfig  {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    /**
     * Returns Authentication JwtToken Filter
     * @return new AuthTokenFilter object to be used by the application
     */
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    /**
     * Returns Authentication Provider with user details and password encoder, to match
     * password encryption used in the user entity
     * @return new Authentication Provider with user details
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    /**
     * Returns Authentication Manager associated with the given AuthenticationConfiguration
     * @param authConfiguration  a given authConfiguration object
     * @return Authentication Manager associated with the given AuthenticationConfiguration
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception {
        return authConfiguration.getAuthenticationManager();
    }

    /**
     * Returns Password Encoder used the by application which uses the BCyrpt standard
     * @return new BCryptPassword Encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Returns Website configuration for the application using a httpSecurity Object
     * @param http httpSecurity Object
     * @return HttpSecurity Object
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).
                and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
                and()
                    .authorizeRequests()
                        .antMatchers("/api/auth/**").permitAll()
                        .antMatchers("/api/test/**").permitAll()
                        .antMatchers("/api/posts/all").permitAll()
                        .antMatchers("/api/tags/**").permitAll()
                        .antMatchers("/api/topics/all/**").permitAll()
                        .antMatchers("/api/posts/get/**").permitAll()
                        .antMatchers("/api/comment/get/**").permitAll()
                        .antMatchers("/api/user/get/**").permitAll()
                    .anyRequest().authenticated();



        http.authenticationProvider(authenticationProvider());

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
