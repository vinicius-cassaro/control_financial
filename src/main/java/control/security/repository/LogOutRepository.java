package control.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import control.security.model.LogOutModel;

@Repository
public interface LogOutRepository extends JpaRepository<LogOutModel, Long>{
	
	Optional<LogOutModel> findByToken(String token);
	
	void deleteByToken(String token);

}
