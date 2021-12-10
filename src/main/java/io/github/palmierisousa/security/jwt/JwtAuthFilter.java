package io.github.palmierisousa.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.palmierisousa.api.ApiErrors;
import io.github.palmierisousa.exception.ElementNotFoundException;
import io.github.palmierisousa.exception.JwtTokenInvalidException;
import io.github.palmierisousa.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthFilter extends OncePerRequestFilter {
    private JwtService jwtService;
    private UserService userService;

    public JwtAuthFilter(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            FilterChain filterChain) throws ServletException, IOException {

        try {
            String authorization = httpServletRequest.getHeader("Authorization");

            if (authorization != null && authorization.startsWith("Bearer")) {

                String[] authItens = authorization.split(" ");

                if (authItens.length != 2) {
                    throw new ElementNotFoundException("Token JWT não encontrado");
                }

                String token = authItens[1];
                boolean isValid = jwtService.validToken(token);

                if (!isValid) {
                    throw new JwtTokenInvalidException("Token JWT inválido ou expirado");
                }

                String login = jwtService.getLogin(token);
                UserDetails loggedUser = userService.loadUserByUsername(login);

                UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(loggedUser,
                        null, loggedUser.getAuthorities());
                user.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(user);
            }

            filterChain.doFilter(httpServletRequest, httpServletResponse);

        } catch (ElementNotFoundException | UsernameNotFoundException | JwtTokenInvalidException e) {
            logger.error(e.getMessage(), e);

            int statusCode = e instanceof ElementNotFoundException
                    ? HttpStatus.NOT_FOUND.value()
                    : HttpStatus.UNAUTHORIZED.value();

            httpServletResponse.setContentType("application/json");
            httpServletResponse.setStatus(statusCode);
            httpServletResponse.getWriter().write(new ObjectMapper().writeValueAsString(new ApiErrors(e.getMessage())));

        }
    }
}
