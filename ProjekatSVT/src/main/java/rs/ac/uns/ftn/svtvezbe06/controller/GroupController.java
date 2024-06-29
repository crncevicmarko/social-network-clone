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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.svtvezbe06.model.dto.*;
import rs.ac.uns.ftn.svtvezbe06.model.entity.*;
import rs.ac.uns.ftn.svtvezbe06.service.*;
import rs.ac.uns.ftn.svtvezbe06.service.UserService;

@RestController
@RequestMapping("groups")
public class GroupController {
	
	@Autowired
	private GroupService groupService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private  GroupIndexingService indexingService;

	@Autowired
	private SearchGroupService searchService;
	
	Date createdAt = new Date(System.currentTimeMillis());

//	public GroupController(GroupIndexingService indexingService, SearchGroupAndPostService searchGroupAndPostService) {
//		this.indexingService = indexingService;
//		this.searchService = searchGroupAndPostService;
//	}


	//	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	@GetMapping
	public ResponseEntity<List<GroupDTO>> getAll(Principal user){
		User user1 = userService.findByUsername(user.getName());
		List<Group> lista = groupService.findAll(user1.getId());
		List<GroupDTO> listGroups = new ArrayList<GroupDTO>();
		for(Group group : lista) {
			GroupDTO groupDTO = new GroupDTO();
			groupDTO.setId(group.getId());
			groupDTO.setName(group.getName());
			groupDTO.setDescription(group.getDescription());
			groupDTO.setSuspended(group.isSuspended());
			groupDTO.setRules(group.getRules());
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
			groupDTO.setRules(group.getRules());
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

	@PostMapping("/search/simple")
	public Page<GroupIndex> simpleSearch(@RequestBody SearchQueryDTO simpleSearchQuery, Pageable pageable) {
		return searchService.simpleSearch(simpleSearchQuery.keywords(), pageable);
	}

//	@PostMapping("/search/advanced")
//	public Page<GroupIndex> advancedSearch(@RequestBody SearchQueryDTO advancedSearchQuery, Pageable pageable) {
//		return searchService.advancedSearch(advancedSearchQuery.keywords(), pageable);
//	}

	@PostMapping("/search/advanced")
	public Page<GroupIndex> advanceddSearch(@RequestParam("name") String name, @RequestParam("description") String description, @RequestParam("rules") String rules, @RequestParam("likeRange") String likeRange, @RequestParam("postRange") String postRange, @RequestParam("operation") String operation, Pageable pageable) {
		List<Integer> likeRangeList = (likeRange == null || likeRange.isEmpty()) ? new ArrayList<>() : createListRange(likeRange);
		List<Integer> postRangeList = (postRange == null || postRange.isEmpty()) ? new ArrayList<>() : createListRange(postRange);
		return searchService.advanceddSearch(name, description, rules, likeRangeList, postRangeList, operation, pageable);
	}

	public List<Integer> createListRange(String listRange){
		String[] parts = listRange.split(":");
		List<Integer> likerange = new ArrayList<>();
		likerange.add(Integer.valueOf(parts[0]));
		likerange.add(Integer.valueOf(parts[1]));
		return  likerange;
	}

	@PostMapping("/search/byOneChoice")
	public Page<GroupIndex> searchByChoice(@RequestBody SearchQueryDTO simpleSearchQuery, Pageable pageable) {
		return searchService.oneChoiceSearch(simpleSearchQuery.keywords(), pageable);
	}

	@PostMapping("/search/byNumOfPosts")
	public Page<GroupIndex> searchByNumOfPostsChoice(@RequestParam("lower") String lowerBound,  @RequestParam("upper") String upperBound, Pageable pageable) {
		if(lowerBound == "" || upperBound == ""){
			return null;
		}
		return searchService.numOfPostsSearch(Integer.valueOf(lowerBound), Integer.valueOf(upperBound), pageable);
	}

	@PostMapping("/search/byAverageNumOfLikes")
	public Page<GroupIndex> searchAverageNumOfLikesChoice(@RequestParam("lower") String lowerBound,  @RequestParam("upper") String upperBound, Pageable pageable) {
		if(lowerBound == "" || upperBound == ""){
			return null;
		}
		return searchService.numofAverageLikeSearch(Integer.valueOf(lowerBound), Integer.valueOf(upperBound), pageable);
	}

//	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<GroupDTO> save(Principal user,
									  @RequestParam("group") String groupJson,
									  @RequestParam("file") MultipartFile file) throws JsonProcessingException { // primamo usera iz tokena verovatno i celu grupu
		Group group = new ObjectMapper().readValue(groupJson, Group.class);
		// moramo da ubacimo korinsika u grupu i sacuvamo
        if(group.getName().equals("") || group.getDescription().equals("")){
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
		User user1 = userService.findByUsername(user.getName());
		group.setCreationDate(createdAt);
		group.setSuspended(false);
		group.setRules(group.getRules());
		group.setUser(user1);
		Group groupSave = groupService.save(group);
		if(groupSave == null) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		else {
			// kreiramo indeks za grupu
			System.out.println("Group: "+group.getId());
			var newIdex = new GroupIndex();
			newIdex.setGroupId(group.getId());
			newIdex.setName(group.getName());
			newIdex.setDescription(group.getDescription());
			newIdex.setRules(group.getRules());
			newIdex.setAverageLikes(0);
			newIdex.setNumberOfPosts(0);
			System.out.println("Document: "+ file);
			indexingService.indexDocument(file, newIdex);
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
		groupDTO.setRules(group.getRules());
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
		groupDTO.setRules(newGroup.getRules());
		groupDTO.setSuspendedReason(newGroup.getSuspendedReason());
		groupDTO.setUser_id(newGroup.getUser().getId());
		return new ResponseEntity<GroupDTO>(groupDTO, HttpStatus.OK);
	}

}
