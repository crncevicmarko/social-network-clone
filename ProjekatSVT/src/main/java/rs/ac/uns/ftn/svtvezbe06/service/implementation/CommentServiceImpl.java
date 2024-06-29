package rs.ac.uns.ftn.svtvezbe06.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rs.ac.uns.ftn.svtvezbe06.model.entity.Comment;
import rs.ac.uns.ftn.svtvezbe06.repository.CommentRepository;
import rs.ac.uns.ftn.svtvezbe06.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService{

	@Autowired
	private CommentRepository commentRepository;
	
	@Override
	public Comment save(Comment comment) {
		return commentRepository.save(comment);
	}

	@Override
	public Comment findOneById(int id) {
		return commentRepository.findOneById(id);
	}

	@Override
	public List<Comment> findAll() {
		return commentRepository.findAll();
	}

	@Transactional
	@Override
	public void update(Comment comment) {
		commentRepository.update(comment.getText(), comment.getId());
	}

	@Transactional
	@Override
	public void delete(int id) {
		commentRepository.delete(id);
	}

	@Override
	public List<Comment> findAllByPostId(int postId) {
		return  commentRepository.findAllByPostId(postId);
	}

}
