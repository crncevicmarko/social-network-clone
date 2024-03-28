package rs.ac.uns.ftn.svtvezbe06.model.dto;

//import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentDTO {
	private int id;
	private String text;
	private String timeStamp;
	private boolean isDeleted;
	private int user_id;
	private int post_id;
	private String userName;
	private String parentId;
}
