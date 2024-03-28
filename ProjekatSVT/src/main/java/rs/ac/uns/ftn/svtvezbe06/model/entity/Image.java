package rs.ac.uns.ftn.svtvezbe06.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "images")
public class Image {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable = false)
	private String path;
	
    @OneToOne(mappedBy = "image")
	private User user;
    
    @ManyToOne(fetch = FetchType.EAGER) // vidi da li ista valja
	@JoinColumn(name = "post_id")
	private Post post;
}
