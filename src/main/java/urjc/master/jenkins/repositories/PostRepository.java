package urjc.master.jenkins.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import urjc.master.jenkins.models.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

	List<Post> findByTitle(String string);
	
}