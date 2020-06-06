package urjc.master.jenkins.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
public class Post {
	
	public interface Standard {
	}

	public interface CommentAtt {
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonView(Standard.class)
	private long id;
		
	@JsonView(Standard.class)
	private String title;
	
	@JsonView(Standard.class)
	private String content;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy="post")
	@JsonView(CommentAtt.class)
	private List<Comment> comments = new ArrayList<>();
		
	public Post() {

	}

	public Post(String title, String content) {
		super();
		this.title = title;
		this.content = content;
		this.comments = new ArrayList<>(); 
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	
	public void addComment(Comment comment) {
		this.comments.add(comment);
	}
	
	public void removeComment(int position) {
		this.comments.remove(position); 
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Post [titulo=" + title + ", content=" + content + ", comments=" + /*comments.toString() +*/ "]";
	}

}
