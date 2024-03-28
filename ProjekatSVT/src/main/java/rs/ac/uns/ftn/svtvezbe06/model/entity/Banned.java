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
@Table(name = "banns")
public class Banned {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable = false)
	private LocalDate timeStamp;
	
	@ManyToOne(fetch = FetchType.EAGER) // vidi da li ista valja
	@JoinColumn(name = "user")
	private User user;
}
