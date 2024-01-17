package us.es.migrolgar2.manhattan.sector;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SectorService {

	private SectorRepository sectorRepository;
	
	public Sector save(Sector sector) {
		return this.sectorRepository.save(sector);
	}
	
}
