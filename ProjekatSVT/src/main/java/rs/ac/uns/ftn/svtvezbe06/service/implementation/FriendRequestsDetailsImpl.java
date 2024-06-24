package rs.ac.uns.ftn.svtvezbe06.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.svtvezbe06.model.entity.FriendRequest;
import rs.ac.uns.ftn.svtvezbe06.repository.FriendRequestRepository;
import rs.ac.uns.ftn.svtvezbe06.service.FriendRequestService;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

@Service
@Primary
public class FriendRequestsDetailsImpl implements FriendRequestService{

	@Autowired
	private FriendRequestRepository friendRequestRepository;
	
	@Override
	public FriendRequest findOneById(int id) {
		return friendRequestRepository.findOneById(id);
	}
	
	@Override
	public FriendRequest save(FriendRequest friendRequest) {
		return friendRequestRepository.save(friendRequest);
	}

	@Override
	@Transactional
	public void accept(Date requestAcceptedOrDeniedAt, int id) {
		friendRequestRepository.accept(requestAcceptedOrDeniedAt, id);
	}

	@Override
	@Transactional
	public void decline(Date requestAcceptedOrDeniedAt, int id) {
		friendRequestRepository.decline(requestAcceptedOrDeniedAt, id);
	}

	//	@Override
//	@Transactional
//	public void update(FriendRequest friendRequest) {
//		friendRequestRepository.update(friendRequest.getApproved(), friendRequest.getRequestAcceptedOrDeniedAt(), friendRequest.getId());
//	}
	@Override
	public List<FriendRequest> getAllByUserId(int id) {
		return friendRequestRepository.getAllByUserId(id);
	}

	@Override
	public List<FriendRequest> getAllById(int id) {
		return friendRequestRepository.getAllById(id);
	}

	@Override
	public List<FriendRequest> getAllRequestsThatCantBeSentAgainByUserId(int id) {
		return friendRequestRepository.getAllRequestsThatCantBeSentAgainByUserId(id);
	}


}
