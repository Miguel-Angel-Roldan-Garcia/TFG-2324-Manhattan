package us.es.migrolgar2.manhattan.sector;

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
		return this.sectorRepository.findByCityAndIndex_(city, sectorIndex).orElse(null);
	}
	
}
