package rs.ac.uns.ftn.svtvezbe06.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rs.ac.uns.ftn.svtvezbe06.model.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer>{

	public Comment findOneById(int id);
	public Comment save(Comment comment);
	public List<Comment> findAll();
	@Modifying
	@Query("update Comment c set c.text = :text where c.id = :id")
	public void update(String text, int id);
	@Modifying
	@Query("update Comment c set c.IsDeleted = true where c.id = :id")
	public void delete(int id);
	
	@Modifying
	@Query("SELECT c FROM Comment c WHERE c.timeStamp >= :datumOd AND c.timeStamp <= :datumDo ORDER BY c.timeStamp ASC")
	public List<Comment> findAllByDateFromLoverToHier(LocalDate datumOd, LocalDate datumDo);
	
	@Modifying
	@Query("SELECT c FROM Comment c WHERE c.timeStamp >= :datumOd AND c.timeStamp <= :datumDo ORDER BY c.timeStamp DESC")
	public List<Comment> findAllByDateFromHierToLower(LocalDate datumOd, LocalDate datumDo);

	@Modifying
	@Query("select c from Comment c where c.post.id = :id")
    List<Comment> findAllByPostId(int id);
}
