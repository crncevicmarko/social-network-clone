package rs.ac.uns.ftn.svtvezbe06.service;

import java.util.List;

import rs.ac.uns.ftn.svtvezbe06.model.dto.UserDTO;
import rs.ac.uns.ftn.svtvezbe06.model.entity.User;

public interface UserService {

	User findOneById(int id);
	
	User findByUsername(String username);

    User createUser(UserDTO userDTO);

    List<User> findAll();
    
    void update(User user);
    
    void block(int id);
    
    void unBlock(int id);

    List<User> findAllByIfUserIsUser(int id);

    List<User> seach(String username);
}
