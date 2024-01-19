package us.es.migrolgar2.manhattan.city;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import us.es.migrolgar2.manhattan.game.Game;

public interface CityRepository extends JpaRepository<City, Integer> {

	@Query("SELECT c FROM City c WHERE c.game = :game AND c.index_ = :cityIndex")
	Optional<City> findByGameAndIndex_(@Param("game") Game game, @Param("cityIndex") Integer cityIndex);

}
