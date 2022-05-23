package control.security.service;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import control.security.model.UserModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenServiceImpl implements TokenService{

	@Value("${jwt.expiration}")
	private String expiration;

	@Value("${jwt.secret}")
	private String secret;

	@Override
	public String generateToken(Authentication authentication) {
		UserModel usuario = (UserModel) authentication.getPrincipal();

		Date now = new Date();
		Date exp = new Date(now.getTime() + Long.parseLong(expiration));

		return Jwts.builder().setIssuer("ControlFinancial")
                             .setSubject(usuario.getId().toString())
                             .setIssuedAt(new Date())
				             .setExpiration(exp)
                             .signWith(SignatureAlgorithm.HS256, secret).compact();
	}

	@Override
	public String getToken(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		if(token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
			return null;
		}else {
			return token.substring(7, token.length());
		}
	}

	@Override
	public boolean isTokenValid(String token) {
		try {
			Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public Long getTokenId(String token) {
		Claims body = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		return Long.valueOf(body.getSubject());
	}

	@Override
	public Date getTokenExpiration(String token) {
		Claims body = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    	return body.getExpiration();
	}
}