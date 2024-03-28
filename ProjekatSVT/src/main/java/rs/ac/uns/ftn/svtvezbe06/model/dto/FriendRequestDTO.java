package rs.ac.uns.ftn.svtvezbe06.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
public class FriendRequestDTO {
    private int id;
    private Boolean approved;
    private String createdAt;
    private Date requestAcceptedOrDeniedAt;
    private int friend_id;
    private int user_id;
}
