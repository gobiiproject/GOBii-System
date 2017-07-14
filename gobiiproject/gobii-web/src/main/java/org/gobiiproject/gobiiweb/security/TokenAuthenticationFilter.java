// ************************************************************************
// (c) 2016 GOBii Projects
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobiiweb.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.gobiiproject.gobidomain.security.TokenInfo;
import org.gobiiproject.gobidomain.services.AuthenticationService;


import org.gobiiproject.gobiimodel.tobemovedtoapimodel.HeaderAuth;
import org.gobiiproject.gobiiweb.CropRequestAnalyzer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.gobiiproject.gobiimodel.types.GobiiHttpHeaderNames;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.StringTokenizer;

/**
 * Takes care of HTTP request/response pre-processing for login/logout and token check.
 * Login can be performed on any URL, logout only on specified {@link #logoutLink}.
 * All the interaction with Spring Security should be performed via {@link AuthenticationService}.
 * <p>
 * {@link SecurityContextHolder} is used here only for debug outputs. While this class
 * is configured to be used by Spring Security (configured filter on FORM_LOGIN_FILTER position),
 * but it doesn't really depend on it at all.
 */
public final class TokenAuthenticationFilter extends GenericFilterBean {

    Logger LOGGER = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

    private final String logoutLink;
    private final AuthenticationService authenticationService;


    public TokenAuthenticationFilter(AuthenticationService authenticationService,
                                     String logoutLink) {

        this.authenticationService = authenticationService;
        this.logoutLink = logoutLink;

    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {


        HttpServletRequest httpRequest = null;
        HttpServletResponse httpResponse = null;

        try {
            httpRequest = (HttpServletRequest) request;
            httpResponse = (HttpServletResponse) response;


            String gobiiCropType = CropRequestAnalyzer.getGobiiCropType(httpRequest);
            if (gobiiCropType != null) {

                String tokenHeaderVal = httpRequest.getHeader(GobiiHttpHeaderNames.HEADER_TOKEN);
                boolean hasValidToken = authenticationService.checkToken(tokenHeaderVal);

                if (hasValidToken) {

                    //header data
                    this.addHeadersToValidRequest(httpResponse,null,gobiiCropType,tokenHeaderVal);
                    chain.doFilter(request, response);
                } else {

                    TokenInfo tokenInfo = null;
                    String userName = httpRequest.getHeader(GobiiHttpHeaderNames.HEADER_USERNAME);
                    String password = httpRequest.getHeader(GobiiHttpHeaderNames.HEADER_PASSWORD);
                    String authorization = httpRequest.getHeader("Authorization");

                    // we assume that the DataSource selector will have done the right thing with the response
                    // we are just echoing back to the client (the web client needs this)

                    if (null == authorization) {

                        // we're doing HTTP post authentication
                        tokenInfo = authenticationService.authenticate(userName, password);

                    } else {
                        tokenInfo = checkBasicAuthorization(authorization, httpResponse);

                    } // if else we're going basic authentication

                    if (null != tokenInfo) {

                        this.addHeadersToValidRequest(httpResponse,userName,gobiiCropType,tokenInfo.getToken());
                        chain.doFilter(request, response);

                    } else {
                        httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    } // if-else the user authenticated
                }

            } else {

                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                LOGGER.error("Unable to proceed with authentication: no crop type could be derived from the request");

            } // if-else crop type could not be found

        } catch (Exception e) {

            LOGGER.error("Error in authentication filter", e);

            if( httpResponse != null ) {
                httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }

    } // doFilter()


    private void addHeadersToValidRequest(HttpServletResponse httpResponse,
                                          String userName,
                                          String gobiiCropType,
                                          String token) throws Exception {


        httpResponse.setHeader(GobiiHttpHeaderNames.HEADER_TOKEN, token);
        httpResponse.setHeader(GobiiHttpHeaderNames.HEADER_GOBII_CROP, gobiiCropType);
        httpResponse.setHeader(GobiiHttpHeaderNames.HEADER_USERNAME, userName);



    }

    private TokenInfo checkBasicAuthorization(String authorization, HttpServletResponse httpResponse) throws IOException {

        TokenInfo returnVal = null;

        StringTokenizer tokenizer = new StringTokenizer(authorization);
        if ((tokenizer.countTokens() >= 2)
                && tokenizer.nextToken().equalsIgnoreCase("Basic")) {

            String base64 = tokenizer.nextToken();
            String loginPassword = new String(Base64.decode(base64.getBytes(StandardCharsets.UTF_8)));

            System.out.println("loginPassword = " + loginPassword);
            tokenizer = new StringTokenizer(loginPassword, ":");
            System.out.println("tokenizer = " + tokenizer);
            String userName = tokenizer.nextToken();
            String password = tokenizer.nextToken();
            returnVal = authenticationService.authenticate(userName, password);
        }

        return returnVal;
    }

    private void checkLogout(HttpServletRequest httpRequest) {
        if (currentLink(httpRequest).equals(logoutLink)) {
            String token = httpRequest.getHeader(GobiiHttpHeaderNames.HEADER_TOKEN);
            // we go here only authenticated, token must not be null
            authenticationService.logout(token);
        }
    }

    // or use Springs util instead: new UrlPathHelper().getPathWithinApplication(httpRequest)
    // shame on Servlet API for not providing this without any hassle :-(
    private String currentLink(HttpServletRequest httpRequest) {
        if (httpRequest.getPathInfo() == null) {
            return httpRequest.getServletPath();
        }
        return httpRequest.getServletPath() + httpRequest.getPathInfo();
    }

}
