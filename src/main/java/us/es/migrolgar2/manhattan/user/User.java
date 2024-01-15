package us.es.migrolgar2.manhattan.user;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import us.es.migrolgar2.manhattan.common.AbstractEntity;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;

    @NotBlank
    @Length(max = 20)
    @Column(unique = true)
    private String username;
    
    @NotBlank
    @Length(max = 75)
    private String password;
    
    @NotNull
    private boolean enabled;

}


