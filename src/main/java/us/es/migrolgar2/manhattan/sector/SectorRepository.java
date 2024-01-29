package us.es.migrolgar2.manhattan.sector;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import us.es.migrolgar2.manhattan.city.City;

public interface SectorRepository extends JpaRepository<Sector, Integer> {

	@Query("SELECT s FROM Sector s WHERE s.city = :city AND s.index_ = :sectorIndex")
	Optional<Sector> findByCityAndIndex(@Param("city") City city, @Param("sectorIndex") Integer sectorIndex);

	List<Sector> findAllByCity(City city);

}
