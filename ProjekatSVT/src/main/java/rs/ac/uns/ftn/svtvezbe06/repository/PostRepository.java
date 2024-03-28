package rs.ac.uns.ftn.svtvezbe06.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import rs.ac.uns.ftn.svtvezbe06.model.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer>{
	
	public Post findOneById(int id);
	
//	@Query(nativeQuery = true, value = "select * from posts")
	public List<Post> findAll();
	
	public Post save(Post post);
	
	@Modifying
	@Query("update Post p set p.content = :content, p.numberOfLikes = :numberOfLikes WHERE p.id = :id")
	public void update(String content, Integer numberOfLikes, Integer id);
	
	@Modifying
	@Query("delete from Post p where p.id = :id")
	public void delete(Integer id);
	
	
}
