package rs.ac.uns.ftn.svtvezbe06.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.uns.ftn.svtvezbe06.model.entity.User;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private int id;

    @NotBlank
    private String firstName;
    
    @NotBlank
    private String lastName;
    
    @NotBlank
    private String email;
    
    @NotBlank
    private String username;

    @NotBlank
    private String password;
    


    public UserDTO(User createdUser) {
        this.id = createdUser.getId();
        this.username = createdUser.getUsername();
        this.firstName = createdUser.getFirstName();
        this.lastName = createdUser.getLastName();
        this.email = createdUser.getEmail
        		();
    }
}
