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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mysql.cj.Session;

import rs.ac.uns.ftn.svtvezbe06.model.dto.GroupDTO;
import rs.ac.uns.ftn.svtvezbe06.model.dto.PostDTO;
import rs.ac.uns.ftn.svtvezbe06.model.dto.UserTokenState;
import rs.ac.uns.ftn.svtvezbe06.model.entity.Group;
import rs.ac.uns.ftn.svtvezbe06.model.entity.Post;
import rs.ac.uns.ftn.svtvezbe06.model.entity.User;
import rs.ac.uns.ftn.svtvezbe06.service.GroupService;
import rs.ac.uns.ftn.svtvezbe06.service.UserService;

@RestController
@RequestMapping("groups")
public class GroupController {
	
	@Autowired
	private GroupService groupService;
	
	@Autowired
	private UserService userService;
	
	Date createdAt = new Date(System.currentTimeMillis());

	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	@GetMapping
	public ResponseEntity<List<GroupDTO>> getAll(){
		List<Group> lista = groupService.findAll();
		List<GroupDTO> listGroups = new ArrayList<GroupDTO>();
		for(Group group : lista) {
			GroupDTO groupDTO = new GroupDTO();
			groupDTO.setId(group.getId());
			groupDTO.setName(group.getName());
			groupDTO.setDescription(group.getDescription());
			groupDTO.setSuspended(group.isSuspended());
			groupDTO.setSuspendedReason(group.getSuspendedReason());
			groupDTO.setUser_id(group.getUser().getId());
			listGroups.add(groupDTO);
		}
		return new ResponseEntity<>(listGroups, HttpStatus.OK);
	}

	@GetMapping("/getAllByUserId/")
	public ResponseEntity<List<GroupDTO>> getAllByUserID(Principal user){
		User user1 = userService.findByUsername(user.getName());
		List<Group> lista = groupService.findAllByUserId(user1.getId());
		List<GroupDTO> listGroups = new ArrayList<GroupDTO>();
		for(Group group : lista) {
			GroupDTO groupDTO = new GroupDTO();
			groupDTO.setId(group.getId());
			groupDTO.setName(group.getName());
			groupDTO.setDescription(group.getDescription());
			groupDTO.setSuspended(group.isSuspended());
			groupDTO.setSuspendedReason(group.getSuspendedReason());
			groupDTO.setUser_id(group.getUser().getId());
			listGroups.add(groupDTO);
		}
		return new ResponseEntity<>(listGroups, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	@GetMapping("/{id}")
	public ResponseEntity<Group> getOne(@PathVariable int id){
		Group group = groupService.findById(id);
		if(group == null) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		else {
			return new ResponseEntity<>(group, HttpStatus.OK);
		}
	}
	
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	@PostMapping
	public ResponseEntity<Group> save(Principal user, @RequestBody Group group){ // primamo usera iz tokena verovatno i celu grupu
		// moramo da ubacimo korinsika u grupu i sacuvamo
        if(group.getName().equals("") || group.getDescription().equals("")){
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
		User user1 = userService.findByUsername(user.getName());
		group.setCreationDate(createdAt);
		group.setSuspended(false);
		group.setUser(user1);
		Group groupSave = groupService.save(group);
		if(groupSave == null) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		else {
			return new ResponseEntity<>(null, HttpStatus.CREATED);
		}
	}
	
//	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	@PutMapping("/update/{id}")
	public ResponseEntity<GroupDTO> update(Principal user, @PathVariable int id, @RequestBody Group group){
		try {
			groupService.update(id, group.getDescription());
		} catch (Exception e) {
			System.out.println(e);
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		Group newGroup = groupService.findById(id);
		GroupDTO groupDTO = new GroupDTO();
		groupDTO.setId(newGroup.getId());
		groupDTO.setName(newGroup.getName());
		groupDTO.setDescription(newGroup.getDescription());
		groupDTO.setSuspended(newGroup.isSuspended());
		groupDTO.setSuspendedReason(newGroup.getSuspendedReason());
		groupDTO.setUser_id(newGroup.getUser().getId());
		return new ResponseEntity<GroupDTO>(groupDTO, HttpStatus.OK);
	}
	
	
	@PreAuthorize("hasAnyRole('GROUPADMIN','ADMIN')")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<GroupDTO> delete(@PathVariable int id){
		try {
			groupService.delete(id);	
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		Group newGroup = groupService.findById(id);
		GroupDTO groupDTO = new GroupDTO();
		groupDTO.setId(newGroup.getId());
		groupDTO.setName(newGroup.getName());
		groupDTO.setDescription(newGroup.getDescription());
		groupDTO.setSuspended(newGroup.isSuspended());
		groupDTO.setSuspendedReason(newGroup.getSuspendedReason());
		groupDTO.setUser_id(newGroup.getUser().getId());
		return new ResponseEntity<GroupDTO>(groupDTO, HttpStatus.OK);
	}

}
