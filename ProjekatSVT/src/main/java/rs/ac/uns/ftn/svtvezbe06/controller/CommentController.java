package rs.ac.uns.ftn.svtvezbe06.controller;

import java.security.Principal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.transaction.annotation.Transactional;

import rs.ac.uns.ftn.svtvezbe06.model.dto.CommentDTO;
import rs.ac.uns.ftn.svtvezbe06.model.entity.Comment;
import rs.ac.uns.ftn.svtvezbe06.model.entity.Post;
import rs.ac.uns.ftn.svtvezbe06.model.entity.User;
import rs.ac.uns.ftn.svtvezbe06.repository.CommentRepository;
import rs.ac.uns.ftn.svtvezbe06.service.CommentService;
import rs.ac.uns.ftn.svtvezbe06.service.PostService;
import rs.ac.uns.ftn.svtvezbe06.service.UserService;

@RestController
@RequestMapping("comments")
public class CommentController {
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PostService postService;
	
	@PersistenceContext
    private EntityManager em;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@GetMapping()
	public ResponseEntity<List<CommentDTO>>fidAll(){
		List<Comment> list = commentService.findAll();
		List<CommentDTO> listComment = new ArrayList<CommentDTO>();
		for(Comment comment : list) {
			CommentDTO commentDTO = new CommentDTO();
			commentDTO.setId(comment.getId());
			commentDTO.setText(comment.getText());
			commentDTO.setTimeStamp(comment.getTimeStamp().toString());
			commentDTO.setDeleted(comment.isIsDeleted());
			commentDTO.setUser_id(comment.getUser().getId());
			commentDTO.setPost_id(comment.getPost().getId());
			if(comment.getCommentParentId() == null) {
			commentDTO.setParentId("null");				
			}else {
				commentDTO.setParentId(Integer.toString(comment.getCommentParentId().getId()));
			}
			listComment.add(commentDTO);
		}
		return new ResponseEntity<>(listComment, HttpStatus.OK);
	}

//	@PutMapping("{postId}")
	@PostMapping()
	@Transactional
	public ResponseEntity<CommentDTO>save(Principal user, /*@PathVariable int postId,*/ @RequestBody CommentDTO commentDTO){
		User logedInUser = userService.findOneById(1);
//		Post post = postService.findById(postId);
//		comment.setUser(logedInUser);
//		comment.setPost(post);
//		Comment comment1 = commentService.save(comment);
//		CommentDTO commentDTO = new CommentDTO();
//		if(comment1 != null) {
//			commentDTO.setId(comment1.getId());
//			commentDTO.setText(comment1.getText());
//			commentDTO.setTimeStamp(comment1.getTimeStamp().toString());
//			commentDTO.setDeleted(comment1.isIsDeleted());
//			commentDTO.setUser_id(logedInUser.getId());
//			commentDTO.setPost_id(comment1.getPost().getId());
//		}
//		return new ResponseEntity<>(null, HttpStatus.CREATED);
		Comment comment = new Comment();
		Comment comment1 = new Comment();
		Post post = postService.findById(commentDTO.getPost_id());
		comment.setText(commentDTO.getText());
		if(commentDTO.getParentId() != null) {
			try {
				comment1 = commentService.findOneById(Integer.parseInt(commentDTO.getParentId()));
			} catch (Exception e) {
				System.out.println("lavor"+e);
			}
			comment.setCommentParentId(comment1);
		}else {
			comment.setCommentParentId(null);
		}
		comment.setIsDeleted(false);
		comment.setPost(post);
		comment.setUser(logedInUser);
		comment.setTimeStamp(LocalDate.now());
//		comment.setCommentParentId(parentComment);
		Comment mergedComment = em.merge(comment);
		    // Persist the merged entity
		Comment commen = commentService.save(mergedComment);
		CommentDTO commentdto = new CommentDTO();
		if(comment1 != null) {
			commentdto.setId(commen.getId());
			commentdto.setText(commen.getText());
			commentdto.setTimeStamp(commen.getTimeStamp().toString());
			commentdto.setDeleted(commen.isIsDeleted());
			commentdto.setUser_id(commen.getUser().getId());
			commentdto.setPost_id(commen.getPost().getId());
			if(commen.getCommentParentId() == null) {
			commentdto.setParentId("null");				
			}else {
				commentdto.setParentId(Integer.toString(commen.getCommentParentId().getId()));
			}
		}
		return new ResponseEntity<>(commentdto, HttpStatus.CREATED);
	}
	
	@PatchMapping("update/{id}")
	public ResponseEntity<CommentDTO>update(@PathVariable int id, @RequestBody Comment comment){
		comment.setId(id);
		try {
			commentService.update(comment);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		Comment comment1 = commentService.findOneById(id);
		CommentDTO commentDTO = new CommentDTO();
		commentDTO.setId(comment1.getId());
		commentDTO.setText(comment1.getText());
		commentDTO.setTimeStamp(comment1.getTimeStamp().toString());
		commentDTO.setDeleted(comment1.isIsDeleted());
		commentDTO.setUser_id(comment1.getUser().getId());
		commentDTO.setPost_id(comment1.getPost().getId());
		if(comment1.getCommentParentId() == null) {
		commentDTO.setParentId("null");				
		}else {
			commentDTO.setParentId(Integer.toString(comment1.getCommentParentId().getId()));
		}
		return new ResponseEntity<>(commentDTO, HttpStatus.OK);
	}
	
	@DeleteMapping("delete/{id}")
	public ResponseEntity<CommentDTO>delete(@PathVariable int id){
		try {
			commentService.delete(id);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	
	@GetMapping("/sort/{turn}/{datumOd}/{datumDo}")
	public ResponseEntity<List<CommentDTO>>fidAllByDate(@PathVariable int turn, @PathVariable String datumOd, @PathVariable String datumDo){
		List<Comment> list = new ArrayList<>();
		if(turn == 0) {
			list = commentRepository.findAllByDateFromLoverToHier(LocalDate.parse(datumOd), LocalDate.parse(datumDo));
		}else {
			list = commentRepository.findAllByDateFromHierToLower(LocalDate.parse(datumOd), LocalDate.parse(datumDo));
		}
		List<CommentDTO> listComment = new ArrayList<CommentDTO>();
		for(Comment comment : list) {
			CommentDTO commentDTO = new CommentDTO();
			commentDTO.setId(comment.getId());
			commentDTO.setText(comment.getText());
			commentDTO.setTimeStamp(comment.getTimeStamp().toString());
			commentDTO.setDeleted(comment.isIsDeleted());
			commentDTO.setUser_id(comment.getUser().getId());
			commentDTO.setPost_id(comment.getPost().getId());
			if(comment.getCommentParentId() == null) {
			commentDTO.setParentId("null");				
			}else {
				commentDTO.setParentId(Integer.toString(comment.getCommentParentId().getId()));
			}
			listComment.add(commentDTO);
		}
		return new ResponseEntity<>(listComment, HttpStatus.OK);
	}

}
