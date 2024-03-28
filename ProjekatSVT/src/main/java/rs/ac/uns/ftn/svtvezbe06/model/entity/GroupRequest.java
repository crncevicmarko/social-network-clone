package rs.ac.uns.ftn.svtvezbe06.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "groupRequests")
public class GroupRequest {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int Id;
	
	@Column(nullable = true)
	private Boolean approved;
	
	@Column(nullable = false)
	private Date createdAt;
	
	@Column(nullable = true)
	private Date requestAcceptedOrDeniedAt;
	
	@ManyToOne(fetch = FetchType.EAGER) // vidi da li ista valja
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne(fetch = FetchType.EAGER) // vidi da li ista valja
	@JoinColumn(name = "group_id")
	private Group group;
}
