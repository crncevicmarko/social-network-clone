package rs.ac.uns.ftn.svtvezbe06.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class RequestedFirendTo {
	@Id
	private int id;
	private int user_id;
	private int friend_id;
}
