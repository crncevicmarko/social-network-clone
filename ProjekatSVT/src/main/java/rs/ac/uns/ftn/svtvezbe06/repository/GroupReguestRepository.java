package rs.ac.uns.ftn.svtvezbe06.repository;

import java.sql.Date;
import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import rs.ac.uns.ftn.svtvezbe06.model.entity.Group;
import rs.ac.uns.ftn.svtvezbe06.model.entity.GroupRequest;
import rs.ac.uns.ftn.svtvezbe06.model.entity.Post;

@Repository
public interface GroupReguestRepository extends JpaRepository<GroupRequest, Integer>{


	@Modifying
	@Query("SELECT gr FROM GroupRequest gr " +
			"INNER JOIN Group g ON gr.group.id = g.id " +
			"WHERE g.user.id = :userId " +
			"AND gr.approved IS NULL")
	public List<GroupRequest> findAll(int userId);
	
	public GroupRequest save(GroupRequest groupRequest);
	
	@Modifying
	@Query("update GroupRequest r set r.approved = :approved, r.requestAcceptedOrDeniedAt = :requestAcceptedOrDeniedAt where r.id = :id")
	public void update(Boolean approved, Date requestAcceptedOrDeniedAt, Integer id);

	@Modifying
	@Query("select r from GroupRequest r where r.approved IS null and r.user.id = :id")
	public List<GroupRequest> findAllByUserId(int id);

	@Modifying
	@Query("SELECT r FROM GroupRequest r WHERE r.user.id = :id AND (r.approved = true OR r.approved IS NULL)")
	public List<GroupRequest> findAllSentAndApprovedGroupRequestsByUserId(int id);
}
