package control.security.model.dto;

public class TokenDTO {
	
	private String type;
	private String token;
	
	public String getType() {
		return type;
	}

	public String getToken() {
		return token;
	}
	
	public TokenDTO(){
	}
	
	public TokenDTO(String type, String token){
		this.type = type;
		this.token = token;
	}
	
	public static BuilderTokenDTO builder() {
		return new BuilderTokenDTO();
	}
	
	public static class BuilderTokenDTO {
		private String type;
		private String token;
		
		BuilderTokenDTO(){
		}
		
		public BuilderTokenDTO type(String type) {
			this.type = type;
			return this;
		}

		public BuilderTokenDTO token(String token) {
			this.token = token;
			return this;
		}
		
		public TokenDTO build() {
			return new TokenDTO(type, token);
		}
	}

	
}