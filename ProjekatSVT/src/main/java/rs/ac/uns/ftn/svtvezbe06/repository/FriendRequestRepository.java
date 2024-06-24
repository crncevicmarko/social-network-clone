package rs.ac.uns.ftn.svtvezbe06.repository;

import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import rs.ac.uns.ftn.svtvezbe06.model.entity.FriendRequest;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Integer>{
	public FriendRequest findOneById(int id);
	
	public FriendRequest save(FriendRequest friendRequest);
	
	@Modifying
	@Query("UPDATE FriendRequest f SET f.approved = true, f.requestAcceptedOrDeniedAt = :requestAcceptedOrDeniedAt WHERE f.id = :id")
	void accept(Date requestAcceptedOrDeniedAt, int id);

	@Modifying
	@Query("UPDATE FriendRequest f SET f.approved = false, f.requestAcceptedOrDeniedAt = :requestAcceptedOrDeniedAt WHERE f.id = :id")
	void decline(Date requestAcceptedOrDeniedAt, int id);

	@Modifying
	@Query("update User u set u.isBlocked = true where u.id = :id")
	void block(int id);

	@Modifying
	@Query("select f from FriendRequest f where f.approved = true and f.user.id = :id ")
	public List<FriendRequest> getAllById(int id);

	@Modifying
	@Query("select f from FriendRequest f where f.approved IS null and f.user.id = :id")
	public List<FriendRequest> getAllByUserId(int id);

	@Modifying
	@Query("select f from FriendRequest f where f.us.id = :id and (f.approved = true or f.approved is null)")
	public List<FriendRequest> getAllRequestsThatCantBeSentAgainByUserId(Integer id);

}
