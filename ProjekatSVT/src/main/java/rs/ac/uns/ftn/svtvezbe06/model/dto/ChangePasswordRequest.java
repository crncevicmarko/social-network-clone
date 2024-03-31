package rs.ac.uns.ftn.svtvezbe06.model.dto;

import lombok.Getter;

@Getter
public class ChangePasswordRequest {
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
