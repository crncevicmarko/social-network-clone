package rs.ac.uns.ftn.svtvezbe06.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.apache.tomcat.jni.Address;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "comments")
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private int id;
	
	@Column(nullable = false)
	private String text;
	
	@Column(nullable = false)
	private LocalDate timeStamp; // vidi da li treba Date !!!!!!!!
	
	@Column(nullable = false)
	private boolean IsDeleted;
	
	@ManyToOne(fetch = FetchType.EAGER) // vidi da li ista valja
	@JoinColumn(name = "user_id")
	private User user;
	
	@OneToMany(mappedBy = "comments", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Reaction> reaction = new HashSet<Reaction>();
	
	@OneToMany(mappedBy = "comments", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Report> reports = new HashSet<Report>();
	
	@ManyToOne(fetch = FetchType.EAGER) // vidi da li ista valja
	@JoinColumn(name = "post_id")
	private Post post;
	
//	@ManyToMany
//	@JoinTable(name = "coment_reply", joinColumns = @JoinColumn(name = "comment_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "comment_reply_id", referencedColumnName = "id"))
//	private Set<Comment> comments = new HashSet<Comment>();
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "parentId", referencedColumnName = "id")
	private Comment commentParentId;
	
//	@Column(name="coment_id", insertable=false, updatable=false)
//	private Integer coment_id; 
//
//	//@OneToMany(mappedBy="parent", fetch=FetchType.EAGER)
//	@OneToMany(mappedBy = "coment_id", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//	@JoinColumn(name="coment_id")
//	private Set<Comment> comment;
}
