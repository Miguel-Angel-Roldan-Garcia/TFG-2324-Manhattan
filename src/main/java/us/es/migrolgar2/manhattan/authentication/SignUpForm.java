package us.es.migrolgar2.manhattan.authentication;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpForm {
	
	@NotBlank
    @Length(max = 15)
    private String username;
    
    @NotBlank
    private String password;
}
