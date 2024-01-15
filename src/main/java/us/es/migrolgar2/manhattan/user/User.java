package us.es.migrolgar2.manhattan.user;

import java.time.LocalDateTime;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
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
    private String password;
    
    @NotNull
    private boolean enabled;
    
    private LocalDateTime creationDate;

}


