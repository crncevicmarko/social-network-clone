package rs.ac.uns.ftn.svtvezbe06.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rs.ac.uns.ftn.svtvezbe06.model.entity.RequestedFirendFrom;

@Repository
public interface RequestedFriendFromRepository extends JpaRepository<RequestedFirendFrom, Integer>{
	
	public RequestedFriendFromRepository save(RequestedFriendFromRepository requesteFriendFrom);
}
