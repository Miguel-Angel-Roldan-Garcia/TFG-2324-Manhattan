package us.es.migrolgar2.manhattan.block;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import us.es.migrolgar2.manhattan.playerDetails.PlayerDetails;
import us.es.migrolgar2.manhattan.sector.Sector;

public interface BlockRepository extends JpaRepository<Block, Integer> {

	@Query("SELECT b FROM Block b WHERE b.player = :pd AND b.index_ = :blockIndex")
	Optional<Block> findByPlayerAndIndex_(@Param("pd") PlayerDetails playerDetails, @Param("blockIndex") Integer blockIndex);

	@Query("SELECT b FROM Block b WHERE b.sector = :sector ORDER BY b.order_ DESC")
	List<Block> getBlocksBySectorOrderedDesc(@Param("sector") Sector sector);

	@Query("SELECT b FROM Block b WHERE b.player.game.id = :gameId")
	List<Block> getAllByGameId(@Param("gameId") int gameId);

}
