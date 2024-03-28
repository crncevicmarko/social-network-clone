package rs.ac.uns.ftn.svtvezbe06.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rs.ac.uns.ftn.svtvezbe06.model.entity.RequestedFirendTo;

@Repository
public interface RequestedFriendToRepository extends JpaRepository<RequestedFirendTo, Integer>{
		
		public RequestedFirendTo save (RequestedFirendTo requestedFirendTo);
	}


