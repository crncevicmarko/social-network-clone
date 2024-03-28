package rs.ac.uns.ftn.svtvezbe06.service;

import java.util.List;

import rs.ac.uns.ftn.svtvezbe06.model.entity.Post;

public interface PostService {
	public Post findById (int id);
	public List<Post> findAll();
	public Post save(Post post);
	public void update(String content, Integer numberOfLikes, Integer id);
	public void delete(Integer id);
}
