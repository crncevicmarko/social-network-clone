package rs.ac.uns.ftn.svtvezbe06.service.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rs.ac.uns.ftn.svtvezbe06.model.entity.Group;
import rs.ac.uns.ftn.svtvezbe06.repository.GroupRepository;
import rs.ac.uns.ftn.svtvezbe06.service.GroupService;

@Service
@Primary
public class GroupDetailsServiceImpl implements GroupService{

	@Autowired
	private GroupRepository groupRepository;
	
	@Override
	public Group findById(int id) {
		return groupRepository.findOneById(id);
	}

	@Override
	public List<Group> findAll(int userId) { // podesili smo da metoda vraca sve grupe koje nisu u vlasnistvu korisika ako je trazi admin vratice sve grupe
		List<Group> newList = new ArrayList<Group>();
		for (Group group : groupRepository.findAll()){
			if(group.getUser().getId() != userId) {
				newList.add(group);
			}
		}
		return newList;
	}

	@Override
	public List<Group> findAll() {
		return groupRepository.findAll();
	}

	@Override
	public List<Group> findAllByUserId(int id) {
		return groupRepository.findAllByUserId(id);
	}

	@Override
	public Group save(Group group) {
		return groupRepository.save(group);
	}

	@Transactional
	@Override
	public void delete(Integer id) {
		groupRepository.delete(id);
	}

	@Transactional
	@Override
	public void update(Integer id, String description) {
		groupRepository.update(id, description);
		System.out.println("lavor");
	}


}
