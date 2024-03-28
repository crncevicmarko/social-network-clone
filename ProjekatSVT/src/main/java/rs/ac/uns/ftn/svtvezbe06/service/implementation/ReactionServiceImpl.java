package rs.ac.uns.ftn.svtvezbe06.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.svtvezbe06.model.entity.Reaction;
import rs.ac.uns.ftn.svtvezbe06.repository.ReactionRepository;
import rs.ac.uns.ftn.svtvezbe06.service.ReactionService;

@Service
public class ReactionServiceImpl implements ReactionService{

	@Autowired
	private ReactionRepository reactionRepository;
	
	@Override
	public Reaction save(Reaction reaction) {
		return reactionRepository.save(reaction);
	}

}
