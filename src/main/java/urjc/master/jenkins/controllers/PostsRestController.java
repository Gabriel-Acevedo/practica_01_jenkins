package urjc.master.jenkins.controllers;

import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import urjc.master.jenkins.models.Author;
import urjc.master.jenkins.models.Comment;
import urjc.master.jenkins.models.Post;
import urjc.master.jenkins.repositories.AuthorRepository;
import urjc.master.jenkins.repositories.CommentRepository;
import urjc.master.jenkins.repositories.PostRepository;

@RestController
public class PostsRestController {

	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private AuthorRepository authorRepository;
	
	@PostConstruct
	public void init() {
		postRepository.save(new Post("Games", "Best videogames ever"));
		postRepository.save(new Post("News", "Breaking news"));
		
		// save a Post
		Author a1 = new Author("Alfredo",25);
		authorRepository.save(a1);
		Author a2 = new Author("Gumersindo",30);
		authorRepository.save(a2);

		Post p1 = new Post("New Post", "Test post");
		postRepository.save(p1);
		
		Comment c1 = new Comment(a1,"First Comment");
		c1.setPost(p1);
		commentRepository.save(c1);
		Comment c2 = new Comment(a2,"Second Comment");
		c2.setPost(p1);
		commentRepository.save(c2);
		
		p1.addComment(c1);
		p1.addComment(c2);	
	}

	interface PostListView extends Post.Elemental, Post.CommentAtt, Comment.Elemental {}

	interface CommentView extends Comment.Elemental, Comment.PostAtt, Post.Elemental {}

	@JsonView(PostListView.class)
	@GetMapping("/posts")
	public List<Post> dashboard() {
		return postRepository.findAll();
	}
	
	@JsonView(PostListView.class)
	@GetMapping("/post/{id}")
	public ResponseEntity<Post> showPost(@PathVariable long id) {
		Optional<Post> op = postRepository.findById(id);
		if (op.isPresent()) {
			Post post = op.get();
			return new ResponseEntity<>(post, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/post")
	@ResponseStatus(HttpStatus.CREATED)
	public Post createPost(@RequestBody Post post) {
		postRepository.save(post);
		return post;
	}
	
	@GetMapping("/authors")
	public List<Author> authors() {
		return authorRepository.findAll();
	}
	
	
	@PostMapping("/author")
	@ResponseStatus(HttpStatus.CREATED)
	public Author createAuthor(@RequestBody Author author) {
		authorRepository.save(author);
		return author;
	}
	
	@JsonView(CommentView.class)
	@GetMapping("/comments")
	public List<Comment> comments() {
		return commentRepository.findAll();
	}
	
	@JsonView(CommentView.class)
	@GetMapping("/comments/{authorId}")
	public ResponseEntity<List<Comment>> commentsByAuthor(@PathVariable long authorId) {
		
		Author author = authorRepository.findById(authorId).get();
		List<Comment> comment = commentRepository.findByAuthor(author);
		
		return new ResponseEntity<>(comment, HttpStatus.OK);
	}

/*
	@JsonView(CommentView.class)
	@PostMapping("/post/{id}/new_comment")
	public Comment newComment(@PathVariable int id, @RequestBody Comment comment) {
		
		commentRepository.save(comment);
		
		Optional<Post> p = postRepository.findById((long)id);
		
		Post post = p.get();
		comment.setPost(post);
		post.addComment(comment);
		postRepository.save(post);
		
		return comment;
	}
*/
	
	@JsonView(CommentView.class)
	@PostMapping("/post/{id}/new_comment/{authorId}")
	public Comment newComment(@PathVariable int id, @PathVariable int authorId, @RequestBody Comment comment) {
		
		Optional<Author> a = authorRepository.findById((long)authorId);
		Author author = a.get();
		
		commentRepository.save(comment);
		comment.setAuthor(author);
		
		Optional<Post> p = postRepository.findById((long)id);
		
		Post post = p.get();
		comment.setPost(post);
		post.addComment(comment);
		postRepository.save(post);
		
		return comment;
	}
	
	@JsonView(CommentView.class)
	@DeleteMapping("/post/{id}")
	public Post deletePost(@PathVariable long id) {
		Post post = postRepository.findById(id).get();
		postRepository.deleteById(id);
		return post;
	}
	
	@JsonView(CommentView.class)
	@DeleteMapping("/comments/{id}")
	public Comment deleteComment(@PathVariable Long id) {
		Comment comment = commentRepository.findById(id).get();
		commentRepository.deleteById(id);
		return comment;
	}

}
