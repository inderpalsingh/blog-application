package com.blogapi.config.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private JwtHelper jwtHelper;
    private UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtHelper jwtHelper, UserDetailsService userDetailsService) {
        this.jwtHelper = jwtHelper;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String authorizationHeader = request.getHeader("Authorization");
        logger.info("Header : {} ", authorizationHeader);
        String username = null;
        String token = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

            try {
                token = authorizationHeader.substring(7); // remove "Bearer " prefix
                username = jwtHelper.getUsernameFromToken(token);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    if (jwtHelper.isValidToken(token, userDetails)) {

                        // add data SecurityContext
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));


                        SecurityContextHolder.getContext().setAuthentication(authentication);

                    }
                }

            } catch (IllegalArgumentException ex) {
                logger.error("Unable to get jwt token", ex);
                ex.printStackTrace();
            } catch (ExpiredJwtException jwt) {
                logger.error("Token expired ", jwt);
                jwt.printStackTrace();
            } catch (MalformedJwtException mal) {
                logger.error("Invalid JWT Token ", mal);
                mal.printStackTrace();
            } catch (Exception e) {
                logger.error("Invalid Token ", e);
                e.printStackTrace();
            }


            // token validate


        } else {
            System.out.println("Invalid Authorization Header");
        }
        filterChain.doFilter(request, response);
    }
}
