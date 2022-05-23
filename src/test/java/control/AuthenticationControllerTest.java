package control;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import control.security.model.UserModel;
import control.security.model.dto.TokenDTO;
import control.security.repository.LogOutRepository;
import control.security.repository.UserRepository;
import control.security.service.LogoutServiceImpl;
import control.security.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {

	@Autowired 
	public MockMvc mockMvc;
	
	@Autowired 
	public LogOutRepository logOutRepsitory;
	
	@Autowired
	public UserRepository repository;
	@Autowired
	public UserService userService;
	
	@Autowired 
	public LogoutServiceImpl logoutServiceImpl;
	
	
	@Test
	public void testLogin() throws Exception {
		
		var user = userService.save(new UserModel("Pedro","pedro@hotmail.com","123456"));
		
		mockMvc.perform(MockMvcRequestBuilders
					.post("/auth/login")
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content("{ \"email\": \""+user.getEmail()+"\", \"password\": \"123456\" }"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.type").value("Bearer"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.token").isString())
				.andDo(MockMvcResultHandlers.print());
		
		repository.deleteAll();
	}
	
	
	@Test
	public void testLogout() throws Exception {
		
		var user = userService.save(new UserModel("Pedro","pedro@hotmail.com","123456"));
		
		var jsonToken = mockMvc.perform(MockMvcRequestBuilders
				.post("/auth/login")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{ \"email\": \""+user.getEmail()+"\", \"password\": \"123456\" }"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn().getResponse().getContentAsString();


        ObjectMapper objectMapper = new ObjectMapper();
		
		var token =   objectMapper.readValue(jsonToken, TokenDTO.class);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/auth/logout")
				.header("Authorization", "Bearer " + token.getToken()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print());	
		
		repository.deleteAll();
	}
}
