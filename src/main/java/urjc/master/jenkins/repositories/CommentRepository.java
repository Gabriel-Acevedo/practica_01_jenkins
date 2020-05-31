package urjc.master.jenkins.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import urjc.master.jenkins.models.Author;
import urjc.master.jenkins.models.Comment;


public interface CommentRepository extends JpaRepository<Comment, Long> {

	List<Comment> findByAuthor(Author author);
}