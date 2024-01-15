package us.es.migrolgar2.manhattan.user;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import us.es.migrolgar2.manhattan.common.AbstractEntity;

@Entity
@Getter
@Setter
public class Friendship extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	private boolean accepted;
	
	@NotNull
	@ManyToOne
	private User user1;
	
	@NotNull
	@ManyToOne
	private User user2;
	
}
