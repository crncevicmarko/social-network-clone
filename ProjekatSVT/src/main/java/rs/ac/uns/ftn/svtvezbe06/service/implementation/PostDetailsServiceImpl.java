package rs.ac.uns.ftn.svtvezbe06.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rs.ac.uns.ftn.svtvezbe06.model.entity.Post;
import rs.ac.uns.ftn.svtvezbe06.repository.PostRepository;
import rs.ac.uns.ftn.svtvezbe06.service.PostService;

@Service
@Primary
public class PostDetailsServiceImpl implements PostService{

	@Autowired 
	private PostRepository postRepository;
	
	@Override
	public Post findById(int id) {
		return postRepository.findOneById(id);
	}

	@Override
	public List<Post> findAll() {
		return postRepository.findAll();
	}

	@Override
	public Post save(Post post) {
		try {	
			return postRepository.save(post);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	@Transactional
	public void update(String content, Integer numberOfLikes, Integer id) {
		postRepository.update(content, numberOfLikes, id);
	}

	@Override
	@Transactional
	public void delete(Integer id) {
		postRepository.delete(id);
		
	}

	@Override
	@Transactional
	public List<Post> findAllByGroupId(int groupId) {
		return postRepository.findAllByGroupId(groupId);
	}

}
