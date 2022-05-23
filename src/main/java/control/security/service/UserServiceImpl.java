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

@Service @Transactional
public class UserServiceImpl implements UserService{
	
private final UserRepository userRepository;
	
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDTO save(UserModel user) {
		Assert.hasLength(user.getUserName(), "Campo nome não pode estar em branco");
		Assert.hasLength(user.getEmail(), "Campo email não pode estar em branco");
		Assert.hasLength(user.getPassword(), "Campo senha não pode estar em branco");
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		user.setPassword(encoder.encode(user.getPassword()));
		return UserMapper.convertModelDto(userRepository.save(user));
	}

	@Override
	public UserDTO update(Long id, UserModel user, Long idToken) throws Exception {
		if(id == idToken) {//ou o usuario seja admin
			var updatedUser = userRepository.findById(id).orElseThrow(() ->  new EntityNotFoundException("Usuario não encontrado"));
			Assert.hasLength(user.getUserName(), "Campo nome não pode estar em branco");
			Assert.hasLength(user.getEmail(), "Campo email não pode estar em branco");
			Assert.hasLength(user.getPassword(), "Campo senha não pode estar em branco");
			updatedUser.setUserName(user.getUserName());
			updatedUser.setEmail(user.getEmail());
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			updatedUser.setPassword(encoder.encode(user.getPassword()));
			
			return UserMapper.convertModelDto(userRepository.save(updatedUser));
		} else {
			throw new HttpUnauthorizedException("Você não possui autorização para tal operação");
		}
	}

	@Override
	public void delete(Long id, Long idToken) throws Exception {
		if(id == idToken) {//ou o usuario seja admin
			userRepository.findById(id).orElseThrow(() ->  new EntityNotFoundException("Usuario não encontrado"));
			userRepository.deleteById(id);
		} else {
			throw new HttpUnauthorizedException("Você não possui autorização para tal operação");
		}
	} 
}
