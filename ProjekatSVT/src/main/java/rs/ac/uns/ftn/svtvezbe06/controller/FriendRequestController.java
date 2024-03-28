package rs.ac.uns.ftn.svtvezbe06.controller;

import java.security.Principal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import rs.ac.uns.ftn.svtvezbe06.model.dto.FriendRequestDTO;
import rs.ac.uns.ftn.svtvezbe06.model.entity.FriendRequest;
import rs.ac.uns.ftn.svtvezbe06.model.entity.RequestedFirendFrom;
import rs.ac.uns.ftn.svtvezbe06.model.entity.RequestedFirendTo;
import rs.ac.uns.ftn.svtvezbe06.model.entity.User;
import rs.ac.uns.ftn.svtvezbe06.service.FriendRequestService;
import rs.ac.uns.ftn.svtvezbe06.service.RequestedFirendFromService;
import rs.ac.uns.ftn.svtvezbe06.service.RequestedFirendToService;
import rs.ac.uns.ftn.svtvezbe06.service.UserService;

@RestController
@RequestMapping("friendRequests")
public class FriendRequestController {
	
	@Autowired
	private FriendRequestService friendRequestService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private RequestedFirendToService requestedFirendToService;
	
	@Autowired
	private RequestedFirendFromService requestedFirendFromService;
	
	Date createdAt = new Date(System.currentTimeMillis());

	@GetMapping()
	public ResponseEntity<List<FriendRequestDTO>> getAllByUserId (Principal user){
		User logedInUser = userService.findByUsername(user.getName());
		List<FriendRequest> list = friendRequestService.getAllByUserId(logedInUser.getId());
		List<FriendRequestDTO> listDTO = new ArrayList<FriendRequestDTO>();
		for(FriendRequest friendRequest : list){
			FriendRequestDTO friendRequestDTO = new FriendRequestDTO();
			friendRequestDTO.setId(friendRequest.getId());
			friendRequestDTO.setApproved(friendRequest.getApproved());
			friendRequestDTO.setCreatedAt(friendRequest.getCreatedAt().toString());
			friendRequestDTO.setRequestAcceptedOrDeniedAt(friendRequest.getRequestAcceptedOrDeniedAt());
			friendRequestDTO.setFriend_id(friendRequest.getUs().getId());
			friendRequestDTO.setUser_id(friendRequest.getUser().getId());
			listDTO.add(friendRequestDTO);
		}
		return new ResponseEntity<>(listDTO, HttpStatus.OK);
	}

	@GetMapping("/all")
	public ResponseEntity<List<FriendRequestDTO>> getAllById (Principal user){
		User logedInUser = userService.findByUsername(user.getName());
		List<FriendRequest> list = friendRequestService.getAllById(logedInUser.getId());
		List<FriendRequestDTO> listDTO = new ArrayList<FriendRequestDTO>();
		for(FriendRequest friendRequest : list){
			FriendRequestDTO friendRequestDTO = new FriendRequestDTO();
			friendRequestDTO.setId(friendRequest.getId());
			friendRequestDTO.setApproved(friendRequest.getApproved());
			friendRequestDTO.setCreatedAt(friendRequest.getCreatedAt().toString());
			friendRequestDTO.setRequestAcceptedOrDeniedAt(friendRequest.getRequestAcceptedOrDeniedAt());
			friendRequestDTO.setFriend_id(friendRequest.getUs().getId());
			friendRequestDTO.setUser_id(friendRequest.getUser().getId());
			listDTO.add(friendRequestDTO);
		}
		return new ResponseEntity<>(listDTO, HttpStatus.OK);
	}
	@PostMapping("/{id}")
	public ResponseEntity<FriendRequest> save (Principal user, @PathVariable int id){
		User logedInUser = userService.findByUsername(user.getName());
		User friend = userService.findOneById(id);
		FriendRequest friendRequest = new FriendRequest();
		friendRequest.setUser(friend);
		friendRequest.setUs(logedInUser);
		friendRequest.setCreatedAt(createdAt);
		FriendRequest friendRequest1 = friendRequestService.save(friendRequest);
		if(friendRequest1 == null) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		else {
			return new ResponseEntity<>(null, HttpStatus.CREATED);
		}
	}
	
	@PutMapping("/{id}/{approved}")
	public ResponseEntity<FriendRequest> update (@PathVariable int id, @PathVariable boolean approved){
		FriendRequest r = friendRequestService.findOneById(id);
//		r.setApproved(approved);
//		r.setRequestAcceptedOrDeniedAt(createdAt);
//		r.setId(id);
//		if(friendRequest.getApproved() == true) {
//			RequestedFirendFrom requestedFirendFrom = requestedFirendFromService.save(makeRequestFriendFrom(r));
//			RequestedFirendTo requestedFirendTo = requestedFirendToService.save(makeRequestFriendTo(r));
//		}
		try {
			if(approved == true){
				friendRequestService.accept(createdAt, r.getId());
			}else{
				friendRequestService.decline(createdAt, r.getId());
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	public RequestedFirendFrom makeRequestFriendFrom(FriendRequest friendRequest) {
		RequestedFirendFrom requestedFirendFrom = new RequestedFirendFrom();
		requestedFirendFrom.setFriend_id(friendRequest.getUs().getId());
		requestedFirendFrom.setUser_id(friendRequest.getUser().getId());
		return requestedFirendFrom;
	}
	
	public RequestedFirendTo makeRequestFriendTo(FriendRequest friendRequest) {
		RequestedFirendTo requestedFirendTo = new RequestedFirendTo();
		requestedFirendTo.setUser_id(friendRequest.getUser().getId());
		requestedFirendTo.setFriend_id(friendRequest.getUs().getId());
		return requestedFirendTo;
	}
}
