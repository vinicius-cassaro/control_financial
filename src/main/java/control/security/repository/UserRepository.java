package control.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import control.security.model.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long>{
	
	Optional<UserModel> findUserByEmail(String email);
}
