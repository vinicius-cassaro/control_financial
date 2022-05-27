package control.application.service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import control.application.model.TagModel;
import control.application.model.dto.TagDTO;
import control.application.model.mapper.TagMapper;
import control.application.repository.TagRepository;
import control.security.repository.UserRepository;
import control.security.service.exceptions.EntityNotFoundException;
import control.security.service.exceptions.HttpUnauthorizedException;

@Service
@Transactional
public class TagServiceImpl implements TagService {

	private final TagRepository tagRepository;
	private final UserRepository userRepository;
	private final EntityManager entityManager;

	public TagServiceImpl(TagRepository tagRepository, EntityManager entityManager, UserRepository userRepository) {
		this.tagRepository = tagRepository;
		this.entityManager = entityManager;
		this.userRepository = userRepository;
	}

	@Override
	public Page<TagDTO> findAllTags(Long idToken, Pageable pageable) {
		Session session = entityManager.unwrap(Session.class);
		Filter filter = session.enableFilter("userId");
		filter.setParameter("user_id", idToken);
		var tags = tagRepository.findAll(pageable).map(TagMapper::convertModelDto);
		session.disableFilter("userId");
		return tags;
	}

	@Override
	public TagDTO findTagById(Long id, Long idToken) throws Exception {
		var tagModel = tagRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Etiqueta não encontrada"));
		if (tagModel.getUserModel().getId() == idToken) {
			return TagMapper.convertModelDto(tagModel);
		} else {
			throw new HttpUnauthorizedException("Você não possui autorização para tal operação");
		}
	}

	@Override
	@Transactional
	public TagDTO save(TagModel tagModel, Long idToken) {
		Assert.hasLength(tagModel.getName(), "Campo nome não pode estar em branco");
		Assert.hasLength(tagModel.getDescription(), "Campo descrição não pode estar em branco");
		tagModel.setUserModel(userRepository.findById(idToken).get());
		return TagMapper.convertModelDto(tagRepository.save(tagModel));
	}

	@Override
	@Transactional
	public TagDTO update(Long id, TagModel tagModel, Long idToken) throws Exception {
		Assert.hasLength(tagModel.getName(), "Campo nome não pode estar em branco");
		Assert.hasLength(tagModel.getDescription(), "Campo descrição não pode estar em branco");
		var tagSave = tagRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Etiqueta não encontrada"));
		if (tagSave.getUserModel().getId() == idToken) {
			tagSave.setName(tagModel.getName());
			tagSave.setDescription(tagModel.getDescription());
			tagSave.setUserModel(tagModel.getUserModel());
			return TagMapper.convertModelDto(tagRepository.save(tagSave));
		} else {
			throw new HttpUnauthorizedException("Você não possui autorização para tal operação");
		}
	}

	@Override
	@Transactional
	public void delete(Long id, Long idToken) throws Exception {
		var tagModel = tagRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Etiqueta não encontrada"));
		if (tagModel.getUserModel().getId() == idToken) {
			tagRepository.deleteById(id);
		} else {
			throw new HttpUnauthorizedException("Você não possui autorização para tal operação");
		}
	}

}
