package org.gobiiproject.gobiiweb.spring;

import org.gobiiproject.gobidomain.security.TokenManager;
import org.gobiiproject.gobidomain.security.impl.TokenManagerSingle;
import org.gobiiproject.gobidomain.services.AuthenticationService;
import org.gobiiproject.gobidomain.services.impl.AuthenticationServiceDefault;
import org.gobiiproject.gobidomain.services.impl.UserDetailsServiceImpl;
import org.gobiiproject.gobiiapimodel.types.ControllerType;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.types.GobiiAuthenticationType;
import org.gobiiproject.gobiiweb.security.TokenAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.GenericFilterBean;
import sun.net.www.content.text.Generic;


/**
 * Created by Phil on 3/22/2017.
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private static ConfigSettings CONFIG_SETTINGS = new ConfigSettings();

    @Override
    @Bean(name = "restAuthenticationManager")
    public AuthenticationManager authenticationManagerBean() throws Exception {

        AuthenticationManager returnVal = super.authenticationManagerBean();
        return returnVal;
    }


    // allow requests for static pages
    // see http://stackoverflow.com/questions/30366405/how-to-disable-spring-security-for-particular-url
    @Override
    public void configure(WebSecurity web) throws Exception {
        String configSettingsUrl = ServiceRequestId.URL_CONFIGSETTINGS.getRequestUrl(null, ControllerType.GOBII);
        web.ignoring().antMatchers( configSettingsUrl,
                "/login",
                "/index.html",
                "/css/**",
                "/images/**",
                "/js/**");

        if(! CONFIG_SETTINGS.isAuthenticateBrapi() ) {
            String allBrapiUrls = ServiceRequestId.getControllerPath(ControllerType.BRAPI) + "**";
            web.ignoring().antMatchers(allBrapiUrls );
        }
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // intellij complains about the derivation of BasicAuthenticationFilter.class,
        // but it is WRONG; this works fine
        String allGobiimethods = ServiceRequestId.SERVICE_PATH_GOBII + "/**";
        http.addFilterAfter(this.filterBean(), BasicAuthenticationFilter.class);
        http.
                csrf().disable().
                sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
                and().
                authorizeRequests().
                antMatchers(allGobiimethods).permitAll().
                anyRequest().authenticated().
                and().
                anonymous().disable();

    }

    // for debug output from open ldap, execute: C:\OpenLDAP\slapd.exe -d -1

    // in the server configuration, the ldap url _must_ be fully qualified with the path to the group
    // that contains the user (e.g., ldap://localhost:389/ou=People,dc=maxcrc,dc=com); I have tried
    // numerous ways to configure the group path in Java but there does not appear to be a way to do this;
    // In xml configuration you set the base property of context-source, but that does not appear to be an
    // option here.
    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {

        ConfigSettings configSettings = new ConfigSettings();
        GobiiAuthenticationType gobiiAuthenticationType = configSettings.getGobiiAuthenticationType();
        if (gobiiAuthenticationType.equals(GobiiAuthenticationType.TEST)) {

            authenticationManagerBuilder.userDetailsService(this.userDetailService());

        } else {

            String dnPattern = configSettings.getLdapUserDnPattern();
            String managerUser = configSettings.getLdapBindUser();
            String managerPassword = configSettings.getLdapBindPassword();
            String url = configSettings.getLdapUrl();

            if (gobiiAuthenticationType.equals(GobiiAuthenticationType.LDAP_CONNECT_WITH_MANAGER)) {

                authenticationManagerBuilder
                        .ldapAuthentication()
                        .userSearchFilter(dnPattern)
                        .contextSource()
                        .managerDn(managerUser)
                        .managerPassword(managerPassword)
                        .url(url);

            } else if (gobiiAuthenticationType.equals(GobiiAuthenticationType.LDAP)) {

                authenticationManagerBuilder
                        .ldapAuthentication()
                        .userSearchFilter(dnPattern)
                        .contextSource()
                        .url(url);
            }
        }
    } // configure()


    @Bean(name = "userDetailService")
    UserDetailsService userDetailService() {
        return new UserDetailsServiceImpl();
    }

    @Bean(name = "tokenManager")
    public TokenManager tokenManager() {
        return new TokenManagerSingle();
    }

    @Bean(name = "authenticationService")
    public AuthenticationService authenticationService() throws Exception {
        return new AuthenticationServiceDefault(this.authenticationManager(), this.tokenManager());
    }

    @Bean(name = "restAuthenticationFilter")
    public GenericFilterBean filterBean() throws Exception {
        return new TokenAuthenticationFilter(this.authenticationService(), "");
    }

}

