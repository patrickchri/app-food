package com.example.user_service.Filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtFilter extends GenericFilter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        ServletOutputStream pw = response.getOutputStream();
        //expects the token to come from the header
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            pw.println("Missing or Invalid Token");
        }
        // Parse and validate the token and set the user id from claims in the request header as an attribute.
        String token = authHeader.substring(7);
        Claims claims = Jwts.parser().setSigningKey("mysecret").parseClaimsJws(token).getBody();
        request.setAttribute("claims", claims);
        filterChain.doFilter(request, response);

    }
    }

