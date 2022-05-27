package control.security.service;

import javax.transaction.Transactional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import control.security.model.UserModel;
import control.security.model.dto.UserDTO;
import control.security.model.mapper.UserMapper;
import control.security.repository.UserRepository;
import control.security.service.exceptions.EntityNotFoundException;
import control.security.service.exceptions.HttpUnauthorizedException;

@Service
public class UserServiceImpl implements UserService{
	
private final UserRepository userRepository;
	
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	@Transactional
	public UserDTO save(UserModel user) {
		Assert.hasLength(user.getUserName(), "Campo nome não pode estar em branco");
		Assert.hasLength(user.getEmail(), "Campo email não pode estar em branco");
		Assert.hasLength(user.getPassword(), "Campo senha não pode estar em branco");
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		user.setPassword(encoder.encode(user.getPassword()));
		return UserMapper.convertModelDto(userRepository.save(user));
	}

	@Override
	@Transactional
	public UserDTO update(Long id, UserModel updatedUser, Long idToken) throws Exception {
		if(id == idToken) {//ou o usuario seja admin
			var user = userRepository.findById(id).orElseThrow(() ->  new EntityNotFoundException("Usuario não encontrado"));
			Assert.hasLength(updatedUser.getUserName(), "Campo nome não pode estar em branco");
			Assert.hasLength(updatedUser.getEmail(), "Campo email não pode estar em branco");
			Assert.hasLength(updatedUser.getPassword(), "Campo senha não pode estar em branco");
			user.setUserName(updatedUser.getUserName());
			user.setEmail(updatedUser.getEmail());
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			user.setPassword(encoder.encode(updatedUser.getPassword()));
			
			return UserMapper.convertModelDto(userRepository.save(user));
		} else {
			throw new HttpUnauthorizedException("Você não possui autorização para tal operação");
		}
	}

	@Override
	@Transactional
	public void delete(Long id, Long idToken) throws Exception {
		if(id == idToken) {//TODO: ou o usuario seja admin
			userRepository.findById(id).orElseThrow(() ->  new EntityNotFoundException("Usuario não encontrado"));
			userRepository.deleteById(id);
		} else {
			throw new HttpUnauthorizedException("Você não possui autorização para tal operação");
		}
	} 
}
