package control.security.model;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash(value = "BlackistToken", timeToLive = 86400)
public class LogOutModel extends AbstractModel<Long>{
	@Indexed
	private String token;
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
}
