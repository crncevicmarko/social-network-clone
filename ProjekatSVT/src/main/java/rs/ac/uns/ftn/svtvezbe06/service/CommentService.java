package rs.ac.uns.ftn.svtvezbe06.service;

import java.util.List;

import rs.ac.uns.ftn.svtvezbe06.model.entity.Comment;

public interface CommentService {
	
	public Comment findOneById(int id);
	public Comment save(Comment comment);
	List<Comment> findAll();
	void update(Comment comment);
	void delete(int id);

    List<Comment> findAllByPostId(int postId);
}
