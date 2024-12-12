package service.user_service.jwt;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;
import service.user_service.config.UserDetailSecurity;

import java.io.IOException;

@Component
@Slf4j// log
public class JwtTokenFilter extends OncePerRequestFilter {

	@Autowired
	private JwtTokenService jwtTokenService;

	@Autowired
	UserDetailSecurity userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String bearerToken = request.getHeader("Authorization");
		log.info(bearerToken);

		if(bearerToken != null && bearerToken.startsWith("Bearer")){
			String token = bearerToken.substring(7);
			String username = jwtTokenService.getUsername(token);
			if(username != null){
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

				//gia lap security
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
		filterChain.doFilter(request, response);
	}

	private String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);//cắt "Bearer " để lấy token xác minh
		}
		return null;
	}
}
