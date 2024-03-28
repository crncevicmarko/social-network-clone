package rs.ac.uns.ftn.svtvezbe06.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "posts")
public class Post {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	
	@Column(nullable = false)
	private String content;
	
	@Column(nullable = false)
	private Date creationDate;
	
	@Column(nullable = false)
	private int numberOfLikes;
	
	@ManyToOne(fetch = FetchType.LAZY) // vidi da li ista valja
	@JoinColumn(name = "user_id")
	private User user;
	
	@OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Reaction> reactions = new HashSet<Reaction>();
	
	@OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Report> reports = new HashSet<Report>();

	@OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Image> images = new HashSet<Image>();
	
	@OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Comment> comments = new HashSet<Comment>();
	
	@ManyToOne(fetch = FetchType.LAZY) // vidi da li ista valja
	@JoinColumn(name = "group_id")
	private Group group;

	public Post(int id, String content, Date creationDate, int numberOfLikes, User user, Set<Reaction> reactions, Set<Report> reports,
			Set<Image> images, Set<Comment> comments, Group group) {
		super();
		this.id = id;
		this.content = content;
		this.creationDate = creationDate;
		this.numberOfLikes = numberOfLikes;
		this.user = user;
		this.reactions = reactions;
		this.reports = reports;
		this.images = images;
		this.comments = comments;
		this.group = group;
	}
	
	
}
