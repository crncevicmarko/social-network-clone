package rs.ac.uns.ftn.svtvezbe06.controller;

import java.security.Principal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rs.ac.uns.ftn.svtvezbe06.model.entity.Reaction;


import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.svtvezbe06.model.dto.ReactionDTO;
import rs.ac.uns.ftn.svtvezbe06.model.entity.Comment;
import rs.ac.uns.ftn.svtvezbe06.model.entity.Post;
import rs.ac.uns.ftn.svtvezbe06.model.entity.Reaction;
import rs.ac.uns.ftn.svtvezbe06.model.entity.User;
import rs.ac.uns.ftn.svtvezbe06.repository.ReactionRepository;
import rs.ac.uns.ftn.svtvezbe06.service.CommentService;
import rs.ac.uns.ftn.svtvezbe06.service.PostService;
import rs.ac.uns.ftn.svtvezbe06.service.ReactionService;
import rs.ac.uns.ftn.svtvezbe06.service.UserService;

@RestController
@RequestMapping("reactions")
public class ReactionController {
	
	@Autowired
	private ReactionService reactionService;
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ReactionRepository reactionRepository;
		
	@PutMapping("/{commentId}/{postId}")
	public ResponseEntity<ReactionDTO> save(Principal user, @PathVariable int commentId, @PathVariable int postId, @RequestBody Reaction reaction){
		User logedUser = userService.findByUsername(user.getName());
		Comment comment = commentService.findOneById(commentId);
		Post post = postService.findById(postId);
		reaction.setUser(logedUser);
		reaction.setComments(comment);
		reaction.setPost(post);
		reaction.setTimeStamp(LocalDate.now());
		try {
			Reaction  reaction1 = reactionService.save(reaction);			
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(null, HttpStatus.CREATED);
	}
	
	@GetMapping()
	public ResponseEntity<List<ReactionDTO>> getAll(){
		List<Reaction> list = new ArrayList<>();
		List<ReactionDTO> list1 = new ArrayList<>();
		try {
			 list = reactionRepository.findAll();			
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		for(Reaction reaction1 : list) {
			ReactionDTO reaction = new ReactionDTO();
			reaction.setId(reaction1.getId());
			reaction.setReaction(reaction1.getReaction().toString());
			reaction.setUser_id(reaction1.getUser().getId());
			reaction.setComment_id(reaction.getComment_id());
			reaction.setPost_id(reaction1.getPost().getId());
			reaction.setTimeStamp(reaction1.getTimeStamp().toString());		
			list1.add(reaction);
		}
		return new ResponseEntity<>(list1, HttpStatus.OK);
	}
	
	@GetMapping("/user/{userId}")
	public ResponseEntity<List<ReactionDTO>> getByUserId(@PathVariable int userId){
		List<Reaction> list = new ArrayList<>();
		List<ReactionDTO> list1 = new ArrayList<>();
		try {
			 list = reactionRepository.findOneByUserId(userId);			
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		for(Reaction reaction1 : list) {
			ReactionDTO reaction = new ReactionDTO();
			reaction.setId(reaction1.getId());
			reaction.setReaction(reaction1.getReaction().toString());
			reaction.setUser_id(reaction1.getUser().getId());
			reaction.setComment_id(reaction.getComment_id());
			reaction.setPost_id(reaction1.getPost().getId());
			reaction.setTimeStamp(reaction1.getTimeStamp().toString());		
			list1.add(reaction);
		}
		return new ResponseEntity<>(list1, HttpStatus.OK);
	}
	
	@GetMapping("/post/{postId}")
	public ResponseEntity<List<ReactionDTO>> getByPostId(@PathVariable int postId){
		List<Reaction> list = new ArrayList<>();
		List<ReactionDTO> list1 = new ArrayList<>();
		try {
			 list = reactionRepository.findOnByPostId(postId);			
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		for(Reaction reaction1 : list) {
			ReactionDTO reaction = new ReactionDTO();
			reaction.setId(reaction1.getId());
			reaction.setReaction(reaction1.getReaction().toString());
			reaction.setUser_id(reaction1.getUser().getId());
			reaction.setComment_id(reaction.getComment_id());
			reaction.setPost_id(reaction1.getPost().getId());
			reaction.setTimeStamp(reaction1.getTimeStamp().toString());		
			list1.add(reaction);
		}
		return new ResponseEntity<>(list1, HttpStatus.OK);
	}
}
