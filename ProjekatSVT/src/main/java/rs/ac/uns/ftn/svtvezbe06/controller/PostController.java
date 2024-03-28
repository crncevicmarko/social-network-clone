package rs.ac.uns.ftn.svtvezbe06.controller;

import java.security.Principal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.svtvezbe06.model.dto.PostDTO;
import rs.ac.uns.ftn.svtvezbe06.model.entity.Group;
import rs.ac.uns.ftn.svtvezbe06.model.entity.Post;
import rs.ac.uns.ftn.svtvezbe06.model.entity.Roles;
import rs.ac.uns.ftn.svtvezbe06.model.entity.User;
import rs.ac.uns.ftn.svtvezbe06.service.GroupService;
import rs.ac.uns.ftn.svtvezbe06.service.PostService;
import rs.ac.uns.ftn.svtvezbe06.service.UserService;

@RestController
@RequestMapping("posts")
public class PostController {
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private GroupService groupService;
	
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
        return new ResponseEntity<>(postDTO, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	@PostMapping("/{groupId}")
	public ResponseEntity<PostDTO> saveOne(Principal user, @PathVariable int groupId, @RequestBody Post post1){
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
		return new ResponseEntity<>(x, HttpStatus.OK);
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
