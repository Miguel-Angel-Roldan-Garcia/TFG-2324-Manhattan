package us.es.migrolgar2.manhattan.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Integer> {
	List<Authority> findByUserUsername(String username);
}
