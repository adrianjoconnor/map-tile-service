package com.adrianoc.maptileservice.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AdminBasicAuthEntryPoint extends BasicAuthenticationEntryPoint {
    @Value("admin.realm")
    private String realm;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.addHeader("WWW-Authenticate", "Basic realm=\"" + getRealmName() + "\"");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        PrintWriter writer = response.getWriter();
        writer.println("401 Unauthorized - " + authException.getMessage());
    }

    @Override
    public void afterPropertiesSet() {
        setRealmName(realm);
        super.afterPropertiesSet();
    }
}
