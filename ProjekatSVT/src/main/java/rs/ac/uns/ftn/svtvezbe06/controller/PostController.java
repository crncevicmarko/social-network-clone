package rs.ac.uns.ftn.svtvezbe06.controller;

import java.security.Principal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.svtvezbe06.model.dto.PostDTO;
import rs.ac.uns.ftn.svtvezbe06.model.dto.SearchQueryDTO;
import rs.ac.uns.ftn.svtvezbe06.model.entity.*;
import rs.ac.uns.ftn.svtvezbe06.service.*;

@RestController
@RequestMapping("posts")
public class PostController {
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private GroupService groupService;
	@Autowired
	private PostIndexingService indexingService;

	@Autowired
	private GroupIndexingService groupIndexingService;

	@Autowired
	private SearchPostService searchService;
	
	Date createdAt = new Date(System.currentTimeMillis());

	@GetMapping
	public ResponseEntity<List<PostDTO>>getAll(){
		Group group = new Group();
		List<Post> lista = postService.findAll();
		List<PostDTO> listaPostova = new ArrayList<PostDTO>();
		for (Post post : lista) {
			PostDTO postDTO = new PostDTO();
			postDTO.setId(post.getId());
			postDTO.setContent(post.getContent());
			postDTO.setNumberOfLikes(post.getNumberOfLikes());
			postDTO.setGroup_id(post.getGroup().getId());
			group = groupService.findById(post.getGroup().getId());
			postDTO.setGroupName(group.getName());
			postDTO.setUser_id(post.getUser().getId());
			listaPostova.add(postDTO);
		}
		return new ResponseEntity<List<PostDTO>>(listaPostova, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	@GetMapping("/{id}")
	public ResponseEntity<PostDTO> getOne(@PathVariable int id){
		Post post = new Post();
		PostDTO postDTO = new PostDTO();
		try {
			post = postService.findById(id);
		} catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		postDTO = new PostDTO();
		postDTO.setId(post.getId());
		postDTO.setContent(post.getContent());
		postDTO.setNumberOfLikes(post.getNumberOfLikes());
		postDTO.setGroup_id(post.getGroup().getId());
		Group group = groupService.findById(post.getGroup().getId());
		postDTO.setGroupName(group.getName());
		postDTO.setUser_id(post.getUser().getId());
		// update number of likes for post by postId in elastic search
		indexingService.updateNumOfLikes(post.getNumberOfLikes(), post.getId());
        return new ResponseEntity<>(postDTO, HttpStatus.OK);
	}

	@PostMapping("/search/simple")
	public Page<PostIndex> simpleSearch(@RequestBody SearchQueryDTO simpleSearchQuery, Pageable pageable) {
		return searchService.simpleSearch(simpleSearchQuery.keywords(), pageable);
	}

	@PostMapping("/search/advanced")
	public Page<PostIndex> advancedSearch(@RequestBody SearchQueryDTO advancedSearchQuery, Pageable pageable) {
		return searchService.advancedSearch(advancedSearchQuery.keywords(), pageable);
	}

	@PostMapping("/search/byNumOfLikes")
	public Page<PostIndex> searchByNumOfLikesChoice(@RequestParam("lower") String lowerBound,  @RequestParam("upper") String upperBound, Pageable pageable) {
		if(lowerBound == "" || upperBound == ""){
			return null;
		}
		return searchService.numOfLikesSearch(Integer.valueOf(lowerBound), Integer.valueOf(upperBound), pageable);
	}
	
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	@PostMapping("/{groupId}")
	public ResponseEntity<PostDTO> saveOne(Principal user, @RequestParam("post") String postJson, @RequestParam("file") MultipartFile file, @PathVariable int groupId) throws JsonProcessingException {
		Post post1 = new ObjectMapper().readValue(postJson, Post.class);
		User user1 = userService.findByUsername(user.getName());
		Group group = groupService.findById(groupId);
		PostDTO x = new PostDTO();
		post1.setCreationDate(createdAt);
		post1.setUser(user1);
		post1.setGroup(group);
		Post post = postService.save(post1);
		x.setId(post.getId());
		x.setContent(post.getContent());
		x.setNumberOfLikes(1);
		x.setGroup_id(post.getGroup().getId());
		x.setGroupName(post.getGroup().getName());
		x.setUser_id(post.getUser().getId());
		if(post == null) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		else {
			// checking if post can be indexed if not we delete created post in db and return BAD_REQUEST
			var newIdex = new PostIndex();
			try {
				System.out.println("Post: "+post.getId());
				newIdex.setPostId(post.getId());
				newIdex.setContent(post.getContent());
				newIdex.setNumberOfLikes(post.getNumberOfLikes());
				System.out.println("Document: "+ file);
				indexingService.indexDocument(file, newIdex);
			}catch (Exception e){
				postService.delete(post.getId());
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
			List<Post> allPostsByGroupId =  postService.findAllByGroupId(groupId);
			int numOfGroupPosts = allPostsByGroupId.size();
			System.out.println("Posts: "+ numOfGroupPosts);
			groupIndexingService.updateNumOfPosts(numOfGroupPosts, groupId);
			return new ResponseEntity<>(x, HttpStatus.OK);
		}
	}
	
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	@PutMapping("update/{id}")
	public ResponseEntity<PostDTO> update(@PathVariable Integer id, @RequestBody Post post){
		try {
			postService.update(post.getContent(), post.getNumberOfLikes(), id);
		} catch (Exception e) {
			System.out.println(e);
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		return getOne(id);
	}
	
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	@DeleteMapping("delete/{id}")
	public ResponseEntity<PostDTO> delete(Principal user,@PathVariable int id){
		User logedUser = userService.findByUsername(user.getName());
		Post post = postService.findById(id);
		if(post.getUser().getId() == logedUser.getId() || logedUser.getRole() == Roles.ADMIN) {
			try {
				postService.delete(id);
			} catch (Exception e) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}	
		}
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	@GetMapping("/date/{date}")
	public ResponseEntity<List<PostDTO>> findByDate(Principal user,@PathVariable String date){
		List<PostDTO> lis = new ArrayList<>();
		List<Post> list = postService.findAll();
		for(Post post : list) {
			if(post.getCreationDate().toString().equals(date)) {
				Group group = new Group();
				PostDTO postDTO = new PostDTO();
				postDTO.setId(post.getId());
				postDTO.setContent(post.getContent());
				postDTO.setNumberOfLikes(post.getNumberOfLikes());
				postDTO.setGroup_id(post.getGroup().getId());
				group = groupService.findById(post.getGroup().getId());
				postDTO.setGroupName(group.getName());
				postDTO.setUser_id(post.getUser().getId());
				lis.add(postDTO);
			}
		}
		return new ResponseEntity<>(lis, HttpStatus.OK);
	}
	
	@GetMapping("/numberOfLikes/{numberOfLikes}")
	public ResponseEntity<List<PostDTO>> findByDate(Principal user,@PathVariable int numberOfLikes){
		List<PostDTO> lis = new ArrayList<>();
		List<Post> list = postService.findAll();
		for(Post post : list) {
			if(numberOfLikes >= post.getNumberOfLikes()) {
				Group group = new Group();
				PostDTO postDTO = new PostDTO();
				postDTO.setId(post.getId());
				postDTO.setContent(post.getContent());
				postDTO.setNumberOfLikes(post.getNumberOfLikes());
				postDTO.setGroup_id(post.getGroup().getId());
				group = groupService.findById(post.getGroup().getId());
				postDTO.setGroupName(group.getName());
				postDTO.setUser_id(post.getUser().getId());
				lis.add(postDTO);
			}
		}
		return new ResponseEntity<>(lis, HttpStatus.OK);
	}
	
}
