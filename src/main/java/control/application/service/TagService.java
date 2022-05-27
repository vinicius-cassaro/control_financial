package control.application.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import control.application.model.TagModel;
import control.application.model.dto.TagDTO;

public interface TagService {
	
	public Page<TagDTO> findAllTags(Long idToken, Pageable pageable);
	
	public TagDTO findTagById(Long id, Long idToken) throws Exception;
	
	public TagDTO save(TagModel tagModel, Long idToken);
	
	public TagDTO update(Long id, TagModel tagModel, Long idToken) throws Exception;
	
	public void delete(Long id, Long idToken) throws Exception;
}
