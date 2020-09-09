package com.rodmontiel.ec.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private UserDetailsServiceImpl jwtUserDetailsService;
	@Autowired
	private Logger gLogger;

	private final JwtToken jwtTokenUtil;

	public JwtRequestFilter(JwtToken jwtTokenUtil) {
		this.jwtTokenUtil = jwtTokenUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		if (!request.getServletPath().equals("/swagger-ui.html")
				&& !request.getServletPath().startsWith("/webjars/springfox-swagger-ui")
				&& !request.getServletPath().startsWith("/csrf") && !request.getServletPath().startsWith("/v2/api-docs")
				&& !request.getServletPath().startsWith("/swagger-resources")
				&& !request.getServletPath().equals("/signin") && !request.getServletPath().equals("/signup")) {

			final String requestTokenHeader = request.getHeader("Authorization");

			String username = null;
			String jwtToken = null;

			if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {

				jwtToken = requestTokenHeader.substring(7);

				try {
					
					username = jwtTokenUtil.getUsernameFromToken(jwtToken);

					if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

						UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);

						if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {

							UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(

									userDetails, null, userDetails.getAuthorities());

							usernamePasswordAuthenticationToken

									.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

							SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
						} else {
							gLogger.error("-----> Error: Invalid token");
						}
					} else {
						gLogger.error("-----> Error: Invalid email");
					}

				} catch (IllegalArgumentException e) {
					gLogger.error("-----> Error: Unable to get JWT Token");
				} catch (ExpiredJwtException e) {
					gLogger.error("-----> Error: JWT Token has expired");
				} catch (Exception e) {
					gLogger.error("-----> Error: Invalid Token ===> " + e.getMessage());
				}

			} else {
				if (requestTokenHeader == null)
					gLogger.warn("-----> Error: Invalid token");
				else
					gLogger.warn("-----> Error: JWT Token does not begin with Bearer String");
			}
		}
		filterChain.doFilter(request, response);
	}

}
