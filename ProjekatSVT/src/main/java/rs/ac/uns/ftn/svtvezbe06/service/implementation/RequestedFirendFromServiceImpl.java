package rs.ac.uns.ftn.svtvezbe06.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.svtvezbe06.model.entity.RequestedFirendFrom;
import rs.ac.uns.ftn.svtvezbe06.repository.RequestedFriendFromRepository;
import rs.ac.uns.ftn.svtvezbe06.service.RequestedFirendFromService;

@Service
@Primary
public class RequestedFirendFromServiceImpl implements RequestedFirendFromService{

	@Autowired
	private RequestedFriendFromRepository requestedFirenFromRepository;
	@Override
	public RequestedFirendFrom save(RequestedFirendFrom requestedFirendFrom) {
		return requestedFirenFromRepository.save(requestedFirendFrom);
	}

}
