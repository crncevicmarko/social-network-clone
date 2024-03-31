package rs.ac.uns.ftn.svtvezbe06.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.svtvezbe06.model.dto.*;
import rs.ac.uns.ftn.svtvezbe06.model.entity.User;
import rs.ac.uns.ftn.svtvezbe06.security.TokenUtils;
import rs.ac.uns.ftn.svtvezbe06.service.UserService;

@RestController
@RequestMapping("users")
public class UserController {
	@Autowired
    UserService userService;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    TokenUtils tokenUtils;

    /* Ili preporucen nacin: Constructor Dependency Injection
    @Autowired
    public UserController(UserServiceImpl userService, AuthenticationManager authenticationManager,
                          UserDetailsService userDetailsService, TokenUtils tokenUtils){
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.tokenUtils = tokenUtils;
    }
    */

//    @PreAuthorize("hasRole('USER', 'ADMIN')")
    @PatchMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request) {
        if(!request.getConfirmPassword().equals(request.getNewPassword())){
            return ResponseEntity.badRequest().body("Confirm password must be same as New Password");
        }
        String response = userService.changePassword(request.getOldPassword(), request.getNewPassword());
        if (response.equals("Old password is incorrect.")){
            return ResponseEntity.badRequest().body("Old password is incorrect.");
        }
        else if (response.equals("Cant save password.")) {
            return  ResponseEntity.internalServerError().body("Cant save password.");
        }
        return ResponseEntity.ok("Password changed successfully.");
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> create(@RequestBody @Validated UserDTO newUser){

        User createdUser = userService.createUser(newUser);

        if(createdUser == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
        UserDTO userDTO = new UserDTO(createdUser);

        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserTokenState> createAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletResponse response) {

        // Ukoliko kredencijali nisu ispravni, logovanje nece biti uspesno, desice se
        // AuthenticationException
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(), authenticationRequest.getPassword()));

        // Ukoliko je autentifikacija uspesna, ubaci korisnika u trenutni security
        // kontekst
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Kreiraj token za tog korisnika
        UserDetails user = (UserDetails) authentication.getPrincipal();
        String jwt = tokenUtils.generateToken(user);
        int expiresIn = tokenUtils.getExpiredIn();

        User user1 = userService.findByUsername(authenticationRequest.getUsername());
        System.out.println(user1.getRole().toString());
        if(user1.isBlocked() == true){
            UserTokenState tokenState = new UserTokenState();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(tokenState);
        }else{
            // Vrati token kao odgovor na uspesnu autentifikaciju
            return ResponseEntity.ok(new UserTokenState(jwt, expiresIn,user1.getRole().toString()));
        }
    }


    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<UserDTO>> loadAll(Principal user) {
        User user11 = userService.findByUsername(user.getName());
        List<User> users = new ArrayList<>();
        if(user.getName() != "admin"){
            users = userService.findAllByIfUserIsUser(user11.getId());
        }else{
    	    users = userService.findAll();
        }
    	List<UserDTO> list = new ArrayList<UserDTO>();
    	for(User user1 : users) {
    		UserDTO userDTO = new UserDTO();
    		userDTO.setId(user1.getId());
    		userDTO.setFirstName(user1.getFirstName());
    		userDTO.setLastName(user1.getLastName());
    		userDTO.setEmail(user1.getEmail());
    		userDTO.setUsername(user1.getUsername());
    		userDTO.setPassword(user1.getPassword());
    		list.add(userDTO);
    	}
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<UserDTO>> search(@RequestParam String username) {
        List<User> users = userService.seach(username);
        List<UserDTO> list = new ArrayList<UserDTO>();
        for(User user1 : users) {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user1.getId());
            userDTO.setFirstName(user1.getFirstName());
            userDTO.setLastName(user1.getLastName());
            userDTO.setEmail(user1.getEmail());
            userDTO.setUsername(user1.getUsername());
            userDTO.setPassword(user1.getPassword());
            list.add(userDTO);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/whoami")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public User user(Principal user) {
        return this.userService.findByUsername(user.getName());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findOneById(Principal user, @PathVariable int id) {
        User user2 = userService.findByUsername(user.getName());
    	User user1 = new User();
    	try {
            if(id == 0){
                user1 = userService.findOneById(user2.getId());
            }else{
        	    user1 = userService.findOneById(id);
            }
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user1.getId());
        userDTO.setFirstName(user1.getFirstName());
        userDTO.setLastName(user1.getLastName());
        userDTO.setEmail(user1.getEmail());
        userDTO.setUsername(user1.getUsername());
        userDTO.setPassword(user1.getPassword());
        
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }
    
    @GetMapping("/user/{userName}")
    public ResponseEntity<UserDTO> findOneByUserName(@PathVariable String userName) {
    	User user = new User();
    	try {
        	user = userService.findByUsername(userName);			
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }
    
    @PatchMapping("/user/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable int id, @RequestBody User user) {
    	try {
        	userService.update(user);			
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }
    
    @PostMapping("/block/{id}")
    public ResponseEntity<UserDTO> block(@PathVariable int id) {
    	User user = userService.findOneById(id);
    	try {
        	userService.block(user.getId());			
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
		}
        
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
    
    @PostMapping("/unblock/{id}")
    public ResponseEntity<UserDTO> unblock(@PathVariable int id) {
    	User user = userService.findOneById(id);
    	try {
        	userService.unBlock(user.getId());			
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
		}
        
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
    
}
