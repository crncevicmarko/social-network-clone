package rs.ac.uns.ftn.svtvezbe06.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.svtvezbe06.model.entity.RequestedFirendTo;
import rs.ac.uns.ftn.svtvezbe06.repository.RequestedFriendToRepository;
import rs.ac.uns.ftn.svtvezbe06.service.RequestedFirendToService;

@Service
@Primary
public class RequestedFirendToServiceImpl implements RequestedFirendToService{

	@Autowired
	private RequestedFriendToRepository requestedFirendToRepository;
	@Override
	public RequestedFirendTo save(RequestedFirendTo requestedFirendTo) {
		return requestedFirendToRepository.save(requestedFirendTo);
	}

}
