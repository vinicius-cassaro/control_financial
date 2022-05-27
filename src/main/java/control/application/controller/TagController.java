package control.application.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import control.application.model.TagModel;
import control.application.model.dto.TagDTO;
import control.application.service.TagServiceImpl;
import control.security.service.TokenService;

@RestController
@RequestMapping("/api/tag")
public class TagController {

	public final TagServiceImpl tagService;
	private final TokenService tokenService;

	@Autowired
	private HttpServletRequest request;
	
	public TagController(TagServiceImpl tagService, TokenService tokenService) {
		this.tagService = tagService;
		this.tokenService = tokenService;
	}
	
	@GetMapping("/")
	public ResponseEntity<Page<TagDTO>> findAllTags(Pageable pageable) {return ResponseEntity.ok(tagService.findAllTags(tokenService.getTokenId(tokenService.getToken(request)), pageable));}
	
	@GetMapping(path = "/{id}")
	public ResponseEntity<TagDTO> findTagById(@PathVariable("id") Long id) throws Exception {return ResponseEntity.ok(tagService.findTagById(id, tokenService.getTokenId(tokenService.getToken(request))));}
	
	@PostMapping(path = "/save")
	public ResponseEntity<TagDTO> saveTag(@RequestBody TagModel tag) {return ResponseEntity.status(HttpStatus.CREATED).body(tagService.save(tag, tokenService.getTokenId(tokenService.getToken(request))));}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<TagDTO> updateTag(@PathVariable("id") Long id, @RequestBody TagModel tag) throws Exception {return ResponseEntity.ok(tagService.update(id, tag, tokenService.getTokenId(tokenService.getToken(request))));}
	
	@DeleteMapping(path = "/delete/{id}")
	public ResponseEntity<?> deleteTagById(@PathVariable("id") Long id) throws Exception {
		tagService.delete(id, tokenService.getTokenId(tokenService.getToken(request)));
		return ResponseEntity.noContent().build();
	}
}
