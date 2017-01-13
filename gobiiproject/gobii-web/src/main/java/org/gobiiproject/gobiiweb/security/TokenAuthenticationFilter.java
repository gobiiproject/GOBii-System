// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobiiweb.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.gobiiproject.gobidomain.security.TokenInfo;
import org.gobiiproject.gobidomain.services.AuthenticationService;
import org.gobiiproject.gobiimodel.dto.header.DtoHeaderAuth;
import org.gobiiproject.gobiimodel.types.GobiiCropType;
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

    public TokenAuthenticationFilter(AuthenticationService authenticationService, String logoutLink) {
        this.authenticationService = authenticationService;
        this.logoutLink = logoutLink;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // ordinary page GETs pass right through
        if (!httpRequest.getMethod().equals("GET")) {

            String tokenHeaderVal = httpRequest.getHeader(GobiiHttpHeaderNames.HEADER_TOKEN);
            boolean hasValidToken = authenticationService.checkToken(tokenHeaderVal);

            if (hasValidToken) {
                chain.doFilter(request, response);
            } else {

                TokenInfo tokenInfo = null;
                String userName = httpRequest.getHeader(GobiiHttpHeaderNames.HEADER_USERNAME);
                String password = httpRequest.getHeader(GobiiHttpHeaderNames.HEADER_PASSWORD);
                String authorization = httpRequest.getHeader("Authorization");

                // we assume that the DataSource selector will have done the right thing with the header
                // we are just echoing back to the client (the web client needs this)

                if (null == authorization) {

                    // we're doing HTTP post authentication
                    tokenInfo = authenticationService.authenticate(userName, password);

                } else {
                    tokenInfo = checkBasicAuthorization(authorization, httpResponse);

                } // if else we're going basic authentication

                if (null != tokenInfo) {

                    httpResponse.setHeader(GobiiHttpHeaderNames.HEADER_TOKEN, tokenInfo.getToken());

                    GobiiCropType gobiiCropType = CropRequestAnalyzer.getGobiiCropType(httpRequest);
                    DtoHeaderAuth dtoHeaderAuth = new DtoHeaderAuth();
                    dtoHeaderAuth.setToken(tokenInfo.getToken());
                    dtoHeaderAuth.setGobiiCropType(gobiiCropType);
                    dtoHeaderAuth.setUserName(userName);


                    ObjectMapper objectMapper = new ObjectMapper();
                    String dtoHeaderAuthString = objectMapper.writeValueAsString(dtoHeaderAuth);
                    httpResponse.getWriter()
                            .write(dtoHeaderAuthString);
                    httpResponse.getWriter().flush();
                    httpResponse.getWriter().close();

                } else {
                    httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                } // if-else the user authenticated
            }

        } else {
            chain.doFilter(request, response);
        } // if-else we're not doing a plain page get

    } // doFilter()

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
