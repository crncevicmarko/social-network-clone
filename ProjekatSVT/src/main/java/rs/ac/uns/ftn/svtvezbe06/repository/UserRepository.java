package rs.ac.uns.ftn.svtvezbe06.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.svtvezbe06.model.entity.GroupRequest;
import rs.ac.uns.ftn.svtvezbe06.model.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	Optional<User> findFirstByUsername(String username);
	
	@Modifying
	@Query("update User u set u.username = :userName, u.firstName = :firstName, u.lastName = :lastName, u.email = :email where u.id = :id")
	void update(String userName, String firstName, String lastName, String email, Integer id);
	
	@Modifying
	@Query("update User u set u.isBlocked = true where u.id = :id")
	void block(int id);
	
	@Modifying
	@Query("update User u set u.isBlocked = false where u.id = :id")
	void unBlock(int id);

	@Modifying
	@Query("select u from User u where u.username != 'admin' and u.id != :id")
	public List<User> findAllByIfUserIsUser(int id);

	@Modifying
	@Query("update User u set u.password = :newPassword where u.id = :id")
	void changePassword(String newPassword, Integer id);

	@Modifying
	@Query("SELECT u FROM User u WHERE u.username LIKE %:username%")
	List<User> seach(String username);

}
