package rs.ac.uns.ftn.svtvezbe06.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import rs.ac.uns.ftn.svtvezbe06.model.entity.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer>{
	
	public Group findOneById(int id);
	
	public List<Group> findAll();
	
	public Group save(Group group);
	
	@Modifying
	@Query("update Group g set g.isSuspended = true where g.id = :id")
	public void delete(Integer id);
	
	@Modifying
	@Query("update Group g set g.description = :description where g.id = :id")
	public void update(Integer id, String description);

	@Modifying
	@Query("select g from Group g where g.user.id = :id")
	List<Group> findAllByUserId(int id);
}
