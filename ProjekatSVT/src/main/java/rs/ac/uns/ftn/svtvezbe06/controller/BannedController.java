package rs.ac.uns.ftn.svtvezbe06.controller;

import java.security.Principal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.svtvezbe06.model.entity.Banned;
import rs.ac.uns.ftn.svtvezbe06.model.entity.User;
import rs.ac.uns.ftn.svtvezbe06.service.BannedService;
import rs.ac.uns.ftn.svtvezbe06.service.UserService;

@RestController
@RequestMapping("banns")
public class BannedController {
	@Autowired
	private BannedService bannedService;
	
	@Autowired
	private UserService userService;
	
	@PutMapping
	public ResponseEntity<Banned> save(Principal user){
		Banned banned = new Banned();
		User user1 = userService.findByUsername(user.getName());
		banned.setTimeStamp(LocalDate.now());
		banned.setUser(user1);
		try {
			Banned bann = bannedService.save(banned);			
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(null, HttpStatus.CREATED);
	}
}
