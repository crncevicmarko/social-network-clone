package rs.ac.uns.ftn.svtvezbe06.service;

import java.util.List;

import rs.ac.uns.ftn.svtvezbe06.model.entity.GroupRequest;

public interface GroupRequestService {

	public List<GroupRequest> findAll(int userId);
	public GroupRequest save(GroupRequest groupRequest);
	public void update(GroupRequest groupRequest, Integer id);
	public List<GroupRequest> findAllByUserId(int id);
	public List<GroupRequest> findAllSentAndApprovedGroupRequestsByUserId(int id);
}
