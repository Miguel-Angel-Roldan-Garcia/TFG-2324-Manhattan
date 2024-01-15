package us.es.migrolgar2.manhattan.user;

import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import us.es.migrolgar2.manhattan.common.AbstractEntity;

@Getter
@Setter
@Entity
@Table(name = "authorities")
public class Authority extends AbstractEntity implements GrantedAuthority {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne
	@JoinColumn(name = "username")
	User user;
	
	@NotBlank
	@Size(min = 3, max = 50)
	String authority;
	
	public Authority() {
		
	}
	
	public Authority(Integer id, @Valid User user, String authority) {
		this.id = id;
		this.user = user;
		this.authority = authority;
	}
	
	public Authority(@Valid User user, String authority) {
		this.user = user;
		this.authority = authority;
	}
	
}
