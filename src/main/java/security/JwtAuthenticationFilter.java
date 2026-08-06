package com.example.realestate.security;

import com.example.realestate.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;


    public JwtAuthenticationFilter(
            JwtService jwtService,
            CustomUserDetailsService userDetailsService) {

        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {


// Allow CORS preflight request
        if (request.getMethod().equals("OPTIONS")) {
            filterChain.doFilter(request, response);
            return;
        }


        String path = request.getServletPath();
        System.out.println("Request PATH=" + path);

        // Skip JWT authentication for Swagger and public APIs
        if (path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-ui.html")
                || path.startsWith("/webjars")
                || path.startsWith("/api/auth")
                || path.startsWith("/api/properties/serarch")
                || path.startsWith("/api/images")) {

            System.out.println("JWT SKIPPED FOR : " + path);


            filterChain.doFilter(request, response);
            return;
        }


        // Get Authorization header
        final String authHeader = request.getHeader("Authorization");


        // No JWT token, continue request
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }


        // Extract JWT token

        String jwt = authHeader.substring(7).trim();

        System.out.println("AUTH HEADER = [" + authHeader + "]");
        System.out.println("JWT TOKEN = [" + jwt + "]");

        String username = jwtService.extractUsername(jwt);


        // Validate user
        if (username != null
                && SecurityContextHolder.getContext().getAuthentication() == null) {


            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(username);


            if (jwtService.isTokenValid(jwt, userDetails)) {


                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );


                authToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );


                SecurityContextHolder.getContext()
                        .setAuthentication(authToken);


                System.out.println(
                        "Authenticated User: "
                                + userDetails.getUsername()
                );

                System.out.println(
                        "Roles: "
                                + userDetails.getAuthorities()
                );
            }
        }


        // Continue request
        filterChain.doFilter(request, response);
    }
}