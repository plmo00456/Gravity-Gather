package com.wooreal.gravitygather.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wooreal.gravitygather.dto.common.ErrorResponse;
import com.wooreal.gravitygather.utils.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import java.util.Objects;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class JwtRequestFilter extends HandlerInterceptorAdapter {

    private final JwtTokenUtil jwtTokenUtil;

    public JwtRequestFilter(JwtTokenUtil jwtTokenUtil){
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        LoginRequired loginRequired = handlerMethod.getMethodAnnotation(LoginRequired.class);
        if (loginRequired != null && loginRequired.value().equals("true")) {
            final String requestTokenHeader = request.getHeader("Authorization");

            String jwtToken;

            if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
                jwtToken = requestTokenHeader.substring(7);
                try {
                    int seq = jwtTokenUtil.getUserSeqFromToken(jwtToken);
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        seq, null, new ArrayList<>());
                    usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                } catch (SignatureException | IllegalArgumentException e) {
                    System.out.println("Unable to get JWT Token");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.getWriter().write(new ObjectMapper().writeValueAsString(new ErrorResponse("UnableToken", "Unable to get JWT Token")));
                    return false;
                } catch (ExpiredJwtException e) {
                    System.out.println("JWT Token has expired");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.getWriter().write(new ObjectMapper().writeValueAsString(new ErrorResponse("ExpiredToken", "JWT token is expired")));
                    return false;
                }
            } else {
                System.out.println("JWT Token does not begin with Bearer String");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write(new ObjectMapper().writeValueAsString(new ErrorResponse("NotAuthorization", "Not Authorization")));
                return false;
            }

            return true;
        }


        return true;
    }
}
