package service.user_service.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class JwtTokenService {
	@Value("${jwt.secret:123}")
	private String secretKey;

	private long validity = 5;


	@Autowired
	UserDetailsService userDetailsService;

	public String createToken(String username, List<String> authority){
		Claims claims = Jwts.claims().setSubject(username);
		claims.put("authorities", authority);
		Date now = new Date();
		Date exp = new Date(now.getTime() + validity * 60 * 1000);
		//validity * 60 * 1000 = 5 * 60 * 1000 = 300,000 milliseconds = 300 seconds = 5 minutes.

		return Jwts.builder().setClaims(claims).setIssuedAt(now)
				.setExpiration(exp)
				.signWith(SignatureAlgorithm.HS256, secretKey)
				.compact();
	}

	public boolean validateToken(String token) {
		try {
		Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token); // check token còn hạn hay không
		return true;
		} catch (Exception e) {
			return false;
		}
	}


	public String getUsername(String token){
		try{
			return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
					.getBody().getSubject();
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public Authentication getAuthentication(String username) {
		UserDetails userDetails = userDetailsService.
				loadUserByUsername((username));
		return new UsernamePasswordAuthenticationToken(userDetails, "",
				userDetails.getAuthorities());
	}
	public String getSecretKey() {
		return secretKey;
	}
}
