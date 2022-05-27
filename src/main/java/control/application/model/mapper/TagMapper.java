package control.application.model.mapper;

import control.application.model.TagModel;
import control.application.model.dto.TagDTO;

public class TagMapper {
	
	public static TagDTO convertModelDto(TagModel tagModel) {
		var tagDto = new TagDTO();
		tagDto.setId(tagModel.getId());
		tagDto.setName(tagModel.getName());
		tagDto.setDescription(tagModel.getDescription());
		tagDto.setUserModel(tagModel.getUserModel());
		return tagDto;
	}
	
	public static TagModel convertDtoModel(TagDTO tagDto) {
		var tagModel = new TagModel();
		tagModel.setName(tagDto.getName());
		tagModel.setDescription(tagDto.getDescription());
		tagModel.setUserModel(tagDto.getUserModel());
		return tagModel;
	}
}
