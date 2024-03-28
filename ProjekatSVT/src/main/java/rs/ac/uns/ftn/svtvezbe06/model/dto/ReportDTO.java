package rs.ac.uns.ftn.svtvezbe06.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReportDTO {
	private int id;
	private String timeStamp;
	private String reason;
	private int user_id;
	private int post_id;
	private int comment_id;
}
