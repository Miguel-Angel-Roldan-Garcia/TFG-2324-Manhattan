package us.es.migrolgar2.manhattan.sector;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import us.es.migrolgar2.manhattan.city.City;

@Service
@AllArgsConstructor
public class SectorService {

	private SectorRepository sectorRepository;
	
	public Sector save(Sector sector) {
		return this.sectorRepository.save(sector);
	}

	public Sector findByCityAndIndex(City city, Integer sectorIndex) {
		return this.sectorRepository.findByCityAndIndex(city, sectorIndex).orElse(null);
	}
	
	public List<Sector> getSectorsByCity(City city) {
		return this.sectorRepository.findAllByCity(city);
	}
	
}
