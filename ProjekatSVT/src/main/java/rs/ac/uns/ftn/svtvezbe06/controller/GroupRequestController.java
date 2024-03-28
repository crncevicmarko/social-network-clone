package rs.ac.uns.ftn.svtvezbe06.controller;

import java.security.Principal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import rs.ac.uns.ftn.svtvezbe06.model.dto.GroupRequestDTO;
import rs.ac.uns.ftn.svtvezbe06.model.dto.PostDTO;
import rs.ac.uns.ftn.svtvezbe06.model.entity.Group;
import rs.ac.uns.ftn.svtvezbe06.model.entity.GroupRequest;
import rs.ac.uns.ftn.svtvezbe06.model.entity.User;
import rs.ac.uns.ftn.svtvezbe06.service.GroupRequestService;
import rs.ac.uns.ftn.svtvezbe06.service.GroupService;
import rs.ac.uns.ftn.svtvezbe06.service.UserService;

@RestController
@RequestMapping("groupRequests")
public class GroupRequestController {
	
	@Autowired
	private GroupRequestService groupRequestService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private GroupService groupService;

	Date createdAt = new Date(System.currentTimeMillis());
	
//	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public ResponseEntity<List<GroupRequestDTO>> getAll(Principal user){
		User user1 = userService.findByUsername(user.getName());
		List<GroupRequest> groupRequests = groupRequestService.findAll(user1.getId());
		List<GroupRequestDTO> listGroupRequests = new ArrayList<GroupRequestDTO>();
		for(GroupRequest groupRequest : groupRequests) {
			GroupRequestDTO groupReques = new GroupRequestDTO();
			groupReques.setId(groupRequest.getId());
			groupReques.setApproved(groupRequest.getApproved());
			groupReques.setCreatedAt(groupRequest.getCreatedAt().toString());
			groupReques.setRequestAcceptedOrDeniedAt(groupRequest.getRequestAcceptedOrDeniedAt());
			groupReques.setUser_id(groupRequest.getUser().getId());
			groupReques.setGroup_id(groupRequest.getGroup().getId());
			listGroupRequests.add(groupReques);
		}
		return new ResponseEntity<List<GroupRequestDTO>>(listGroupRequests, HttpStatus.OK);
	}
	
//	@GetMapping("/{id}")
//	public ResponseEntity<GroupRequestDTO> getOneById(@PathVariable int id){
//		GroupRequest groupRequests = groupRequestService.findById(id);
//		GroupRequestDTO groupReques = new GroupRequestDTO();
//		groupReques.setId(groupRequests.getId());
//		groupReques.setApproved(groupRequests.getApproved());
//		groupReques.setCreatedAt(groupRequests.getCreatedAt().toString());
//		groupReques.setRequestAcceptedOrDeniedAt(groupRequests.getRequestAcceptedOrDeniedAt());
//		groupReques.setUser_id(groupRequests.getUser().getId());
//		groupReques.setGroup_id(groupRequests.getGroup().getId());
//		return new ResponseEntity<GroupRequestDTO>(groupReques, HttpStatus.OK);
//	}
	
//	@PreAuthorize("hasRole('USER','ADMIN')")
	@PutMapping("/update/{id}")
	public ResponseEntity<GroupRequestDTO> update(@PathVariable Integer id, @RequestParam boolean value){
		GroupRequest groupRequest = new GroupRequest();
		groupRequest.setId(id);
		groupRequest.setApproved(value);
		groupRequest.setRequestAcceptedOrDeniedAt(createdAt);
		groupRequestService.update(groupRequest, id);
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
//	@PreAuthorize("hasRole('USER','ADMIN')")
	@PostMapping("/save/{groupid}")
	public ResponseEntity<GroupRequest> save (Principal user, @PathVariable int groupid) {
		User user1 = userService.findByUsername(user.getName());
		Group group = groupService.findById(groupid);
		GroupRequest groupRequest = new GroupRequest();
		groupRequest.setUser(user1);
		groupRequest.setGroup(group);
		groupRequest.setCreatedAt(createdAt);
		GroupRequest groupRequest2 = groupRequestService.save(groupRequest);
		if(groupRequest2 == null) {
            return new ResponseEntity<>(null,HttpStatus.NOT_ACCEPTABLE);
		}
		else {
            return new ResponseEntity<>(null,HttpStatus.CREATED);
		}
	}
	
}
