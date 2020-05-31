package urjc.master.jenkins.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonView;


@Entity
public class Comment{
	
	public interface Standard {
	}

	public interface PostAtt {
	}
	
	public Comment(long id, Author author, String comment) {
		super();
		this.id = id;
		this.author = author;
		this.comment = comment;
	}
	
	public Comment(Author author, String comment) {
		super();
		this.author = author;
		this.comment = comment;
	}
	
	public Comment() {

	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonView(Standard.class)
	private long id;

	@OneToOne
	@JsonView(Standard.class)
	private Author author;
	
	@JsonView(Standard.class)
	private String comment;
	
	@ManyToOne
	@JsonView(PostAtt.class)
	private Post post; 
	

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Author getAuthor() {
		return author; 
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	@Override
	public String toString() {
		return "Comment [name=" + author.getName() + ", comment=" + comment + "]";
	}
}
