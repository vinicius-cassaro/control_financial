package control;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
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

import control.application.model.TagModel;
import control.application.repository.TagRepository;
import control.application.service.TagService;
import control.security.model.UserModel;
import control.security.model.dto.TokenDTO;
import control.security.model.dto.UserDTO;
import control.security.repository.UserRepository;
import control.security.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TagControllerTest {
	
	@Autowired 
	public MockMvc mockMvc;
	
	@Autowired
	public UserRepository userRepository;
	@Autowired
	public UserService userService;
	
	@Autowired
	public TagRepository tagRepository;
	@Autowired
	public TagService tagService;
	
	TokenDTO token;
	UserDTO userDefault;
	UserDTO userSecond;

	@BeforeAll
	public void SetUp() throws Exception{
		userDefault = userService.save(new UserModel("Pedro","pedro@hotmail.com","123456"));
		userSecond = userService.save(new UserModel("Eduardo","eduardo@hotmail.com","123456"));
		
		var jsonToken = mockMvc.perform(MockMvcRequestBuilders
				.post("/auth/login")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{ \"email\": \""+userDefault.getEmail()+"\", \"password\": \"123456\" }"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andReturn().getResponse().getContentAsString();


        ObjectMapper objectMapper = new ObjectMapper();
		
		token =   objectMapper.readValue(jsonToken, TokenDTO.class);
	}
	
	@AfterAll
	public void cleanUp() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.post("/auth/logout")
				.header("Authorization", "Bearer " + token.getToken()))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print());
		
		tagRepository.deleteAll();
		userRepository.deleteAll();
	}
	
	@BeforeEach
	public void cleanUpEach(){
		tagRepository.deleteAll();
	}
	
	@Test
	public void testFindAllTags() throws Exception {
		tagService.save(new TagModel("FindAll", "find all test"), userDefault.getId());
		
		mockMvc.perform(MockMvcRequestBuilders
				.get("/api/tag/")
				.header("Authorization", "Bearer " + token.getToken()))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").isString())
		.andExpect(MockMvcResultMatchers.jsonPath("$.content[0].description").isString())
		.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void testFindTagById() throws Exception {
		var tag =  tagService.save(new TagModel("FindAll", "find all test"), userDefault.getId());
		
		mockMvc.perform(MockMvcRequestBuilders
				.get("/api/tag/"+tag.getId())
				.header("Authorization", "Bearer " + token.getToken()))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.name").value(tag.getName()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.description").value(tag.getDescription()))
		.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void testFindTagByIdNoExistent() throws Exception {		
		mockMvc.perform(MockMvcRequestBuilders
				.get("/api/tag/"+1)
				.header("Authorization", "Bearer " + token.getToken()))
		.andExpect(MockMvcResultMatchers.status().isNotFound())
		.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void testSaveTag() throws Exception {
		var tag = new TagModel("Save", "save test");
		
		mockMvc.perform(MockMvcRequestBuilders
				.post("/api/tag/save")
				.header("Authorization", "Bearer " + token.getToken())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{ \"name\": \""+tag.getName()+"\", \"description\": \""+tag.getDescription()+"\"}"))
		.andExpect(MockMvcResultMatchers.status().isCreated())
		.andExpect(MockMvcResultMatchers.jsonPath("$.name").value(tag.getName()))
		.andExpect(MockMvcResultMatchers.jsonPath("$.description").value(tag.getDescription()))
		.andDo(MockMvcResultHandlers.print());
		
		Assertions.assertFalse(tagRepository.findAll().isEmpty());
	}
	
	@Test
	public void testSaveTagWithoutArguments() throws Exception{
		var tag = new TagModel("", "");
		
		mockMvc.perform(MockMvcRequestBuilders
				.post("/api/tag/save")
				.header("Authorization", "Bearer " + token.getToken())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{ \"name\": \""+tag.getName()+"\", \"description\": \""+tag.getDescription()+"\"}"))
		.andExpect(MockMvcResultMatchers.status().isBadRequest())
		.andDo(MockMvcResultHandlers.print());
		
		Assertions.assertTrue(tagRepository.findAll().isEmpty());
	}
	
	@Test
	public void testUpdateTag() throws Exception {
		var tag = tagService.save(new TagModel("Update", "update test"), userDefault.getId());
		tag.setName("Update2");
		tag.setDescription("update test2");
		
		mockMvc.perform(MockMvcRequestBuilders.put("/api/tag/update/"+tag.getId())
				.header("Authorization", "Bearer " + token.getToken())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{ \"name\": \""+tag.getName()+"\", \"description\": \""+tag.getDescription()+"\"}"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print());
		
		var tagById = tagRepository.findById(tag.getId()).get();
		Assertions.assertEquals(tagById.getName(), tag.getName());
		Assertions.assertEquals(tagById.getDescription(), tag.getDescription());
	}
	
	@Test
	public void testUpdateNoExistentTag() throws Exception{
		var tag = tagService.save(new TagModel("Update", "update test"), userDefault.getId());
		tag.setId(tag.getId()+1);
		tag.setName("Update2");
		tag.setDescription("update test2");
		
		mockMvc.perform(MockMvcRequestBuilders.put("/api/tag/update/"+tag.getId())
				.header("Authorization", "Bearer " + token.getToken())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{ \"name\": \""+tag.getName()+"\", \"description\": \""+tag.getDescription()+"\"}"))
		.andExpect(MockMvcResultMatchers.status().isNotFound())
		.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void testUpdateTagUnauthorized() throws Exception{
		var tag = tagService.save(new TagModel("Update", "update test"), userSecond.getId());
		tag.setName("Update2");
		tag.setDescription("update test2");
		
		mockMvc.perform(MockMvcRequestBuilders.put("/api/tag/update/"+tag.getId())
				.header("Authorization", "Bearer " + token.getToken())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{ \"name\": \""+tag.getName()+"\", \"description\": \""+tag.getDescription()+"\"}"))
		.andExpect(MockMvcResultMatchers.status().isUnauthorized())
		.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void testUpdateTagWithoutArguments() throws Exception{
		var tag = tagService.save(new TagModel("Update", "update test"), userDefault.getId());
		tag.setName("");
		tag.setDescription("");
		
		mockMvc.perform(MockMvcRequestBuilders.put("/api/tag/update/"+tag.getId())
				.header("Authorization", "Bearer " + token.getToken())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{ \"name\": \""+tag.getName()+"\", \"description\": \""+tag.getDescription()+"\"}"))
		.andExpect(MockMvcResultMatchers.status().isBadRequest())
		.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void testDeleteTagById() throws Exception {
		var tag = tagService.save(new TagModel("Delete", "delete test"), userDefault.getId());
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/tag/delete/"+tag.getId())
				.header("Authorization", "Bearer " + token.getToken()))
		.andExpect(MockMvcResultMatchers.status().isNoContent())
		.andDo(MockMvcResultHandlers.print());
		
		var tagById = tagRepository.findById(tag.getId());
		Assertions.assertFalse(tagById.isPresent());
	}
	
	@Test
	public void testDeleteNoExistentTagById() throws Exception {
		var tag = tagService.save(new TagModel("Delete", "delete test"), userDefault.getId());
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/tag/delete/"+tag.getId()+1)
				.header("Authorization", "Bearer " + token.getToken()))
		.andExpect(MockMvcResultMatchers.status().isNotFound())
		.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void testDeleteTagByIdUnauthorized() throws Exception {
		var tag = tagService.save(new TagModel("Delete", "delete test"), userSecond.getId());
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/tag/delete/"+tag.getId())
				.header("Authorization", "Bearer " + token.getToken()))
		.andExpect(MockMvcResultMatchers.status().isUnauthorized())
		.andDo(MockMvcResultHandlers.print());
	}
}
