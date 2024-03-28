package rs.ac.uns.ftn.svtvezbe06.service;

import rs.ac.uns.ftn.svtvezbe06.model.entity.FriendRequest;

import java.sql.Date;
import java.util.List;

public interface FriendRequestService {

	public FriendRequest findOneById(int id);
	
	public FriendRequest save(FriendRequest friendRequest);
	
	public void accept(Date requestAcceptedOrDeniedAt, int id);

	public void decline(Date requestAcceptedOrDeniedAt, int id);

	public List<FriendRequest> getAllByUserId(int id);

	public List<FriendRequest> getAllById(int userId);

}
