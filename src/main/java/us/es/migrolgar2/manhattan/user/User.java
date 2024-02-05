package us.es.migrolgar2.manhattan.user;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import us.es.migrolgar2.manhattan.common.AbstractEntity;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;

    @NotBlank
    @Length(max = 15)
    @Column(unique = true)
    private String username;
    
    @NotBlank
    private String password;
    
    @NotNull
    private boolean enabled;
    
    private LocalDateTime creationDate;
    
    @Transient
    @JsonIgnore
	public long getAge() {
    	return this.creationDate.until(LocalDateTime.now(), ChronoUnit.SECONDS);
    }
    
}


