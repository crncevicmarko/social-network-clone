package rs.ac.uns.ftn.svtvezbe06.model.entity;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "userGroups") //ne moze rec groups, group nesto drugo
public class Group {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable = false, unique = true)
	private String name;
	
	@Column(nullable = false)
	private String description;
	
	@Column(nullable = false)
	private Date creationDate;
	
	@Column(nullable = true)
	private boolean isSuspended;
	
	@Column(nullable = true)
	private String suspendedReason;
	
	@ManyToOne(fetch = FetchType.EAGER) // vidi da li ista valja
	@JoinColumn(name = "user_id")
	private User user;
	
	@OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = CascadeType.ALL) // ?
	private Set<Post> posts = new HashSet<Post>();
	
	@OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<GroupRequest> groupsRequests = new HashSet<GroupRequest>();
	
}
