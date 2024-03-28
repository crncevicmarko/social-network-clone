package rs.ac.uns.ftn.svtvezbe06.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "reports")
public class Report {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(nullable = false)
	private LocalDate timeStamp;
		
	private Boolean accepted;
	
	@Column(nullable = false)
    @Enumerated(EnumType.STRING)
	private ReportReasons reason;
	
	@ManyToOne(fetch = FetchType.EAGER) // vidi da li ista valja
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne(fetch = FetchType.EAGER) // vidi da li ista valja
	@JoinColumn(name = "comment_id")
	private Comment comments;
	
	@ManyToOne(fetch = FetchType.EAGER) // vidi da li ista valja
	@JoinColumn(name = "post_id")
	private Post post;
}
