package urjc.master.jenkins.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import urjc.master.jenkins.models.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {

	List<Author> findByName(String string);

}