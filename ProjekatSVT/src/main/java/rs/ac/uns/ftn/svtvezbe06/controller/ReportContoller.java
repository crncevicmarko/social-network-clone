package rs.ac.uns.ftn.svtvezbe06.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.svtvezbe06.model.dto.ReportDTO;
import rs.ac.uns.ftn.svtvezbe06.model.entity.Comment;
import rs.ac.uns.ftn.svtvezbe06.model.entity.Post;
import rs.ac.uns.ftn.svtvezbe06.model.entity.Report;
import rs.ac.uns.ftn.svtvezbe06.model.entity.User;
import rs.ac.uns.ftn.svtvezbe06.service.CommentService;
import rs.ac.uns.ftn.svtvezbe06.service.PostService;
import rs.ac.uns.ftn.svtvezbe06.service.ReportService;
import rs.ac.uns.ftn.svtvezbe06.service.UserService;

@RestController
@RequestMapping("reports")
public class ReportContoller {

	@Autowired
	private ReportService reposrtService;
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private UserService service;
	
	@PostMapping("/{commentId}/{postId}")
	public ResponseEntity<ReportDTO> save(Principal user, @PathVariable int commentId, @PathVariable int postId, @RequestBody Report report){
		User user1 = service.findByUsername(user.getName());
		if(commentId == 0) {
			report.setComments(null);			
		}else {
			Comment comment = commentService.findOneById(commentId);
			report.setComments(comment);
		}
		Post post = postService.findById(postId);
		report.setUser(user1);
		report.setPost(post);
		report.setTimeStamp(LocalDate.now());
		try {
			Report report1 = reposrtService.save(report);			
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(null, HttpStatus.CREATED);
	}
	
	@GetMapping
	public ResponseEntity<List<ReportDTO>> getAll(){
		List<Report> list = new ArrayList<Report>();
		try {
			list = reposrtService.findAll();			
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		List<ReportDTO> listDTO = new ArrayList<ReportDTO>();
		for(Report report : list) {
			ReportDTO reportDTO = new ReportDTO();
			reportDTO.setId(report.getId());
			reportDTO.setTimeStamp(report.getTimeStamp().toString());
			reportDTO.setReason(report.getReason().toString());
			reportDTO.setUser_id(report.getUser().getId());
			reportDTO.setPost_id(report.getPost().getId());
			if(report.getComments() == null) {
				reportDTO.setComment_id(0);
			}else {
				reportDTO.setComment_id(report.getComments().getId());				
			}
			listDTO.add(reportDTO);
		}
		return new ResponseEntity<>(listDTO, HttpStatus.OK);
	}
	
}
