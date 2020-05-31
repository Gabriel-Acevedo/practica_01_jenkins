package urjc.master.jenkins.mockmvc;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.concurrent.atomic.AtomicLong;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import urjc.master.jenkins.models.Post;

@SpringBootTest
@AutoConfigureMockMvc
public class ControllerIntegrationTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@Order(1)
	public void createPost() throws Exception {

		AtomicLong lastPostId = new AtomicLong();
		Post post = new Post("First Post", "Testing post with Integration Test.");
		post.setId(lastPostId.incrementAndGet());

		mvc.perform(post("/post").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(post))).andExpect(status().isCreated());
		mvc.perform(get("/post/" + post.getId()).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.title", equalTo("First Post")));
	}

}
