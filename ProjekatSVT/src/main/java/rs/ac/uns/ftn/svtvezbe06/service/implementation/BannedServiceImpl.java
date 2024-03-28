package rs.ac.uns.ftn.svtvezbe06.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.svtvezbe06.model.entity.Banned;
import rs.ac.uns.ftn.svtvezbe06.repository.BannedRepository;
import rs.ac.uns.ftn.svtvezbe06.service.BannedService;

@Service
public class BannedServiceImpl implements BannedService{

	@Autowired
	private BannedRepository bannedRepository;
	@Override
	public Banned save(Banned banned) {
		return bannedRepository.save(banned);
	}

}
