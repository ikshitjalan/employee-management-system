package com.team.ghana.authJwt;

// JWT security code is taken from https://dzone.com/articles/spring-boot-security-json-web-tokenjwt-hello-world

// to gain access to the systems endpoints we must make a POST request at /auth with the user's username and password
// the request body must be like:
// {
//	"username":"XXX"
//	,"password":"YYY"
// }
// the /auth endpoint is handled by JwtAuthController

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.team.ghana.enums.UserRole.ADMIN;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class JwtWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthEntryPoint jwtAuthEntryPoint;
    @Autowired
    private JwtRequestFilter jwtRequestFilter;
    @Autowired
    private JwtUserDetailsService uDetailsServiceJWT;  // class that implements UserDetailsService, to confirm UserDetails

    @Bean   // this is needed to authenticate a user in AuthController
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // customize auth using UserDetailsService interface
        auth.userDetailsService(uDetailsServiceJWT).passwordEncoder(encodePass());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // allow H2 https://stackoverflow.com/questions/43794721/spring-boot-h2-console-throws-403-with-spring-security-1-5-2
        http.csrf().disable().headers().frameOptions().sameOrigin();

        http.authorizeRequests().antMatchers("/auth").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/h2-console/**").permitAll();

        // all other requests need to be authenticated
        http.authorizeRequests()

                // business units
                .antMatchers(HttpMethod.GET, "/businessUnits/**").authenticated()
                .antMatchers(HttpMethod.POST, "/businessUnits/**").hasRole(String.valueOf(ADMIN))
                .antMatchers(HttpMethod.PUT, "/businessUnits/**").hasRole(String.valueOf(ADMIN))
                .antMatchers(HttpMethod.PATCH, "/businessUnits/**").hasRole(String.valueOf(ADMIN))

                // departments
                .antMatchers(HttpMethod.GET,"/departments/**").authenticated()
                .antMatchers(HttpMethod.POST, "/departments/**").hasRole(String.valueOf(ADMIN))
                .antMatchers(HttpMethod.PUT, "/departments/**").hasRole(String.valueOf(ADMIN))
                .antMatchers(HttpMethod.PATCH, "/departments/**").hasRole(String.valueOf(ADMIN))

                // units
                .antMatchers(HttpMethod.GET,"/units/**").authenticated()
                .antMatchers(HttpMethod.POST, "/units/**").hasRole(String.valueOf(ADMIN))
                .antMatchers(HttpMethod.PUT, "/units/**").hasRole(String.valueOf(ADMIN))
                .antMatchers(HttpMethod.PATCH, "/units/**").hasRole(String.valueOf(ADMIN))

                // employees
                .antMatchers(HttpMethod.GET,"/employees/**").authenticated()
                .antMatchers(HttpMethod.POST, "/employees/**").hasRole(String.valueOf(ADMIN))
                .antMatchers(HttpMethod.PUT, "/employees/**").hasRole(String.valueOf(ADMIN))
                .antMatchers(HttpMethod.PATCH, "/employees/**").hasRole(String.valueOf(ADMIN))
                .antMatchers(HttpMethod.DELETE, "/employees/**").hasRole(String.valueOf(ADMIN))

                // tasks
                .antMatchers(HttpMethod.GET,"/tasks/**").authenticated()
                .antMatchers(HttpMethod.POST, "/tasks/**").hasRole(String.valueOf(ADMIN))
                .antMatchers(HttpMethod.PUT, "/tasks/**").hasRole(String.valueOf(ADMIN))
                .antMatchers(HttpMethod.PATCH, "/tasks/**").hasRole(String.valueOf(ADMIN))
                .antMatchers(HttpMethod.DELETE, "/tasks/**").hasRole(String.valueOf(ADMIN))

                .anyRequest().authenticated().and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthEntryPoint).and()

                /* make sure we use stateless session; session won't be used to store user's state. */

                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Add a filter to validate the tokens with every request

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public BCryptPasswordEncoder encodePass() { return new BCryptPasswordEncoder(); }
}
