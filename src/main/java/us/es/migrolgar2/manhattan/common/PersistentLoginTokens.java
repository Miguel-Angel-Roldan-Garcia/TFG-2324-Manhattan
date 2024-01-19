package us.es.migrolgar2.manhattan.common;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "persistent_logins")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersistentLoginTokens extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;
	
	@NotNull
	@Column(name = "username")
	private String username;
	
	@NotNull
	@Column(name = "series", unique = true)
	private String series;
	
	@NotNull
	@Column(name = "token")
	private String token;
	
	@NotNull
	@Column(name = "last_used")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUsed;
	
}
