package rs.ac.uns.ftn.svtvezbe06.model.entity;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable = false, unique = true)
	private String username;
	
	@Column(nullable = false)
	private String password;
	private String email;
	private Date lastLogin;// vidi da li treba sql ili util inport !!!!!!!!!!!!
	private String firstName;
	private String lastName;
	private boolean isBlocked;
	// vidi da li ces da role pravis kao posebne klase ili kao enum!!!!!!!!!
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id", nullable = true)
	private Image image;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<FriendRequest> friendRequests = new HashSet<FriendRequest>();
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Post> poasts = new HashSet<Post>();
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Report> reports = new HashSet<Report>();
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Reaction> reactions = new HashSet<Reaction>();
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<GroupRequest> groupReguests = new HashSet<GroupRequest>();
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Comment> comments = new HashSet<Comment>();
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Group> groups = new HashSet<Group>();
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Banned> banns = new HashSet<Banned>();
	
	@ManyToMany // vidi
	@JoinTable(name = "requestedFirendFrom", joinColumns = @JoinColumn(name = "friend_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
	private Set<User> friends1 = new HashSet<User>(); 
	
	
	@ManyToMany // vidi
	@JoinTable(name = "requestedFirendTo", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "friend_id", referencedColumnName = "id"))
	private Set<User> friends = new HashSet<User>();
	
	@Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Roles role;
	

	public User(int id, String username, String password, String email, Date lastLogin, String firstName,
			String lastName, boolean isBlocked, Image image, Set<FriendRequest> friendRequests, Set<Post> poasts, Set<Report> reports,
			Set<Reaction> reactions, Set<GroupRequest> groupReguests, Set<Comment> comments, Set<User> friends1,
			Roles role) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.lastLogin = lastLogin;
		this.firstName = firstName;
		this.lastName = lastName;
		this.isBlocked = isBlocked;
		this.image = image;
		this.friendRequests = friendRequests;
		this.poasts = poasts;
		this.reports = reports;
		this.reactions = reactions;
		this.groupReguests = groupReguests;
		this.comments = comments;
		this.friends1 = friends1;
		this.role = role;
	}
	
	
	
	//TODO banned
	
	
}
