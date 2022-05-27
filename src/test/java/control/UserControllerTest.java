package control;

import java.io.UnsupportedEncodingException;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
import control.security.repository.UserRepository;
import control.security.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerTest {

	@Autowired 
	public MockMvc mockMvc;
	
	@Autowired
	public UserRepository repository;
	@Autowired
	public UserService userService;
	
	@AfterAll
	public void cleanUp() throws Exception{
		repository.deleteAll();
	}
	
	@BeforeEach
	public void cleanUpEach(){
		repository.deleteAll();
	}
	
	@Test
	public void testSaveUser() throws Exception{
		var user = new UserModel("Pedro","pedro@hotmail.com","123456");
		
		mockMvc.perform(MockMvcRequestBuilders
				.post("/api/user/save")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{ \"userName\": \""+user.getUserName()+"\", \"email\": \""+user.getEmail()+"\", \"password\": \""+user.getPassword()+"\"}"))
		.andExpect(MockMvcResultMatchers.status().isCreated())
		.andExpect(MockMvcResultMatchers.jsonPath("$.userName").value("Pedro"))
		.andExpect(MockMvcResultMatchers.jsonPath("$.email").isString())
		.andDo(MockMvcResultHandlers.print());
		
		Assertions.assertFalse(repository.findAll().isEmpty());
		repository.deleteAll();
	}
	
	@Test
	public void testSaveUserWithoutArguments() throws Exception{
		
		mockMvc.perform(MockMvcRequestBuilders
				.post("/api/user/save")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{ \"userName\": \"\", \"email\": \"\", \"password\": \"\"}"))
		.andExpect(MockMvcResultMatchers.status().isBadRequest())
		.andDo(MockMvcResultHandlers.print());
		
		Assertions.assertTrue(repository.findAll().isEmpty());
	}
	
	@Test
	public void testUpdateUser() throws UnsupportedEncodingException, Exception{
		
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
		
		user.setUserName("Pedro Santos");
		user.setEmail("pedro_santos@hotmail.com");
		
		mockMvc.perform(MockMvcRequestBuilders.put("/api/user/update/"+user.getId())
				.header("Authorization", "Bearer " + token.getToken())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{ \"userName\": \""+user.getUserName()+"\", \"email\": \""+user.getEmail()+"\", \"password\": \""+user.getPassword()+"\"}"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(MockMvcResultHandlers.print());
		
		var userById = repository.findById(user.getId()).get();
		Assertions.assertEquals(userById.getUserName(), user.getUserName());
		Assertions.assertEquals(userById.getEmail(), user.getEmail());
	}
	
	@Test
	public void testUpdateNoExistentUser() throws Exception{
		
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
		
		var noExistentUser = new UserModel("Eduardo","eduardo@hotmail.com","123456");
		
		mockMvc.perform(MockMvcRequestBuilders.put("/api/user/update/"+(user.getId()+1))
				.header("Authorization", "Bearer " + token.getToken())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{ \"userName\": \""+noExistentUser.getUserName()+"\", \"email\": \""+noExistentUser.getEmail()+"\", \"password\": \""+noExistentUser.getPassword()+"\"}"))
			//.andExpect(MockMvcResultMatchers.status().isNotFound())
			.andExpect(MockMvcResultMatchers.status().isUnauthorized())
			.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void testUpdateUserUnauthorized() throws Exception{
		
		var user1 = userService.save(new UserModel("Eduardo","eduardo@hotmail.com","123456"));
		var user2 = userService.save(new UserModel("Pedro","pedro@hotmail.com","123456"));
		
		var jsonToken = mockMvc.perform(MockMvcRequestBuilders
				.post("/auth/login")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{ \"email\": \""+user1.getEmail()+"\", \"password\": \"123456\" }"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn().getResponse().getContentAsString();


        ObjectMapper objectMapper = new ObjectMapper();
		
		var token =   objectMapper.readValue(jsonToken, TokenDTO.class);
		
		user2.setUserName("Pedro Santos");
		user2.setEmail("pedro_santos@hotmail.com");
		
		mockMvc.perform(MockMvcRequestBuilders.put("/api/user/update/"+user2.getId())
				.header("Authorization", "Bearer " + token.getToken())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{ \"userName\": \""+user2.getUserName()+"\", \"email\": \""+user2.getEmail()+"\", \"password\": \""+user2.getPassword()+"\"}"))
			.andExpect(MockMvcResultMatchers.status().isUnauthorized())
			.andDo(MockMvcResultHandlers.print());
	}
	
	
	@Test
	public void testUpdateUserWithoutArguments() throws Exception{
		
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
		
		user.setUserName("");
		user.setEmail("");
		user.setPassword("");
		
		mockMvc.perform(MockMvcRequestBuilders.put("/api/user/update/"+user.getId())
				.header("Authorization", "Bearer " + token.getToken())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{ \"userName\": \"\", \"email\": \"\", \"password\": \"\"}"))
				//.content("{ \"userName\": \""+user.getUserName()+"\", \"email\": \""+user.getEmail()+"\", \"password\": \""+user.getPassword()+"\"}"))
			.andExpect(MockMvcResultMatchers.status().isBadRequest())
			.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void testDeleteUser() throws Exception{
		
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
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/delete/"+user.getId())
				.header("Authorization", "Bearer " + token.getToken()))
			.andExpect(MockMvcResultMatchers.status().isNoContent())
			.andDo(MockMvcResultHandlers.print());
		
		var userById = repository.findById(user.getId());
		Assertions.assertFalse(userById.isPresent());
	}
	
	@Test
	public void testDeleteNoExistentTagById() throws Exception {
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
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/delete/"+user.getId()+1)
				.header("Authorization", "Bearer " + token.getToken()))
		//.andExpect(MockMvcResultMatchers.status().isNotFound())
		.andExpect(MockMvcResultMatchers.status().isUnauthorized())
		.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void testDeleteTagByIdUnauthorized() throws Exception {
		var user1 = userService.save(new UserModel("Eduardo","eduardo@hotmail.com","123456"));
		var user2 = userService.save(new UserModel("Pedro","pedro@hotmail.com","123456"));
		
		var jsonToken = mockMvc.perform(MockMvcRequestBuilders
				.post("/auth/login")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{ \"email\": \""+user1.getEmail()+"\", \"password\": \"123456\" }"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn().getResponse().getContentAsString();


        ObjectMapper objectMapper = new ObjectMapper();
		
        
		var token =   objectMapper.readValue(jsonToken, TokenDTO.class);
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/delete/"+user2.getId())
				.header("Authorization", "Bearer " + token.getToken()))
		.andExpect(MockMvcResultMatchers.status().isUnauthorized())
		.andDo(MockMvcResultHandlers.print());

	}
}
