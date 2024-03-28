package rs.ac.uns.ftn.svtvezbe06.model.dto;

import java.sql.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GroupRequestDTO {
	private int id;
	private Boolean approved;
	private String createdAt;
	private Date requestAcceptedOrDeniedAt;
	private int user_id;
	private int group_id;
}
