package rs.ac.uns.ftn.svtvezbe06.service;

import java.util.List;

import rs.ac.uns.ftn.svtvezbe06.model.entity.Group;

public interface GroupService {

	public Group findById(int id);
	public List<Group> findAll();
	public Group save(Group group);
	public void delete(Integer id);
	public void update(Integer id, String description);
	List<Group> findAllByUserId(int id);
}
