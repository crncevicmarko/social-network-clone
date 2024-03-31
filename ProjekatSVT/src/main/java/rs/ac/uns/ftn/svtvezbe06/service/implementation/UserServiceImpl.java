package rs.ac.uns.ftn.svtvezbe06.service.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.svtvezbe06.service.UserService;
import rs.ac.uns.ftn.svtvezbe06.model.dto.UserDTO;
import rs.ac.uns.ftn.svtvezbe06.model.entity.Roles;
import rs.ac.uns.ftn.svtvezbe06.model.entity.User;
import rs.ac.uns.ftn.svtvezbe06.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserServiceImpl implements UserService{
	@Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /*
    @Autowired
    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }
*/
    @Override
    public User findByUsername(String username) {
        Optional<User> user = userRepository.findFirstByUsername(username);
        if (!user.isEmpty()) {
            return user.get();
        }
        return null;
    }
    
    @Override
    public User findOneById(int id) {
    	Optional<User> user = userRepository.findById(id);
        if (!user.isEmpty()) {
            return user.get();
        }
        return null;
    }

    @Override
    public User createUser(UserDTO userDTO) {

        Optional<User> user = userRepository.findFirstByUsername(userDTO.getUsername());

        if(user.isPresent()){
            return null;
        }

        User newUser = new User();
        newUser.setUsername(userDTO.getUsername());
        newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        newUser.setEmail(userDTO.getEmail());
        newUser.setRole(Roles.USER);
        newUser = userRepository.save(newUser);

        return newUser;
    }

    @Override
    public List<User> findAll() {
        return this.userRepository.findAll();
    }

	@Override
	@Transactional
	public void update(User user) {
		userRepository.update(user.getUsername(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getId());
	}

	@Override
	@Transactional
	public void block(int id) {
		userRepository.block(id);
	}

	@Override
	@Transactional
	public void unBlock(int id) {
		userRepository.unBlock(id);
	}

    @Override
    public List<User> findAllByIfUserIsUser(int id) {
        return userRepository.findAllByIfUserIsUser(id);
    }
    @Override
    @Transactional
    public String changePassword(String oldPassword, String newPassword) {
        // Get the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Find the user by username
        User user = findByUsername(username);

        // Verify the old password
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return "Old password is incorrect.";
        }

        // Update the password with the new one
        user.setPassword(passwordEncoder.encode(newPassword));
//        userRepository.changePassword(user.getPassword(), user.getId());
        try {
            userRepository.changePassword(user.getPassword(), user.getId());
        } catch (Exception e) {
            return "Cant save password.";
        }
        return "";
    }

    @Override
    @Transactional
    public List<User> seach(String username) {
        return userRepository.seach(username);
    }

}
