package rs.ac.uns.ftn.svtvezbe06.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostDTO {
	
	private Integer id;
	private String content;
	private Integer numberOfLikes;
	private Integer group_id;
	private String groupName;
	private Integer user_id;
}
