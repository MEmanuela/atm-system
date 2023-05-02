package org.internship.jpaonlinebanking.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.internship.jpaonlinebanking.exceptions.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MyBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {
    @Autowired
    private ObjectMapper objectMapper;
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        String error = authException.getMessage();
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, error);

        response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName());
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
    @Override
    public void afterPropertiesSet() {
        setRealmName("Internship");
        super.afterPropertiesSet();
    }
}
