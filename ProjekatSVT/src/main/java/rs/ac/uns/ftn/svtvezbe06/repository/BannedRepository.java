package rs.ac.uns.ftn.svtvezbe06.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rs.ac.uns.ftn.svtvezbe06.model.entity.Banned;
@Repository
public interface BannedRepository extends JpaRepository<Banned, Integer>{
	public Banned save(Banned banned);
}
