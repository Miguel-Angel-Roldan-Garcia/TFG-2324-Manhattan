package us.es.migrolgar2.manhattan.block;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import us.es.migrolgar2.manhattan.game.Game;
import us.es.migrolgar2.manhattan.playerDetails.PlayerDetails;
import us.es.migrolgar2.manhattan.sector.Sector;

public interface BlockRepository extends JpaRepository<Block, Integer> {

	@Query("SELECT b FROM Block b WHERE b.player = :pd AND b.index_ = :blockIndex")
	Optional<Block> findByPlayerAndIndex_(@Param("pd") PlayerDetails playerDetails, @Param("blockIndex") Integer blockIndex);

	@Query("SELECT b FROM Block b WHERE b.sector = :sector ORDER BY b.order_ DESC")
	List<Block> getBlocksBySectorOrderedDesc(@Param("sector") Sector sector);

	@Query("SELECT b FROM Block b WHERE b.player.game.id = :gameId")
	List<Block> getAllByGameId(@Param("gameId") int gameId);

	@Query("SELECT b FROM Block b WHERE b.selected AND b.player = :player")
	List<Block> findAllBlocksSelectedByPlayer(@Param("player") PlayerDetails player);

	@Query("SELECT b FROM Block b WHERE b.player.game = :game ORDER BY b.order_ DESC")
	List<Block> getBlocksByGameOrderedDesc(@Param("game") Game game);

	@Query("SELECT b FROM Block b WHERE b.player.game = :game AND b.sector.city.index_ = :cityIndex")
	List<Block> findBlocksByGameAndCityIndex(@Param("game") Game game, @Param("cityIndex") int cityIndex);

	@Query("SELECT b FROM Block b WHERE b.player = :pd AND NOT b.selected AND NOT b.placed")
	List<Block> findAvailableBlocksByPlayer(PlayerDetails pd);

}
