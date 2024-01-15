package us.es.migrolgar2.manhattan.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorityService {

	private AuthorityRepository authorityRepository;
	
	@Autowired
	public AuthorityService(AuthorityRepository authorityRepository) {
		this.authorityRepository = authorityRepository;
	}
	
	public void save(Authority authority) {
		this.authorityRepository.save(authority);
	}
}
