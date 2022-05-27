package control.application.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import control.application.model.TagModel;

@Repository
public interface TagRepository extends JpaRepository<TagModel, Long>{

	Page<TagModel> findAll(Pageable pageable);
}
