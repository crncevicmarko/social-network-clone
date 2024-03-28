package rs.ac.uns.ftn.svtvezbe06.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rs.ac.uns.ftn.svtvezbe06.model.entity.GroupRequest;
import rs.ac.uns.ftn.svtvezbe06.repository.GroupReguestRepository;
import rs.ac.uns.ftn.svtvezbe06.service.GroupRequestService;

@Service
@Primary
public class GroupRequestServiceImpl implements GroupRequestService{

	@Autowired
	private GroupReguestRepository groupReguestRepository;

	@Override
	@Transactional
	public List<GroupRequest> findAll(int userId) {
		return groupReguestRepository.findAll();
	}

	
	@Override
	public GroupRequest save(GroupRequest groupRequest) {
		return groupReguestRepository.save(groupRequest);
	}
	
	@Override
	@Transactional
	public void update(GroupRequest groupReqest, Integer id) {
		groupReguestRepository.update(groupReqest.getApproved(), groupReqest.getRequestAcceptedOrDeniedAt(), id);
	}

	@Override
	@Transactional
	public List<GroupRequest> findAllByUserId(int id) {
		return groupReguestRepository.findAllByUserId(id);
	}

}
