package rs.ac.uns.ftn.svtvezbe06.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReactionDTO {
	private int id;
	private String reaction;
	private String timeStamp;
	private int user_id;
	private int post_id;
	private int comment_id;
}
