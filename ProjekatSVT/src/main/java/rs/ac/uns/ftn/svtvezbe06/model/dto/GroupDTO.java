package rs.ac.uns.ftn.svtvezbe06.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GroupDTO {
	private int id;
	private String name;
	private String description;
	private boolean isSuspended;
	private String suspendedReason;
	private int user_id;
}
