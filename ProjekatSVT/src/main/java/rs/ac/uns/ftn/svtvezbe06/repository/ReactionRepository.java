package rs.ac.uns.ftn.svtvezbe06.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rs.ac.uns.ftn.svtvezbe06.model.entity.Reaction;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Integer>{
	public Reaction save(Reaction reaction);
	List<Reaction> findAll();
	List<Reaction> findOneByUserId(int id);
	List<Reaction> findOnByPostId(int id);
}
