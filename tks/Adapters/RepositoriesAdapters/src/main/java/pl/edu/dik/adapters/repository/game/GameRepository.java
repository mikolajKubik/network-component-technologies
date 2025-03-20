package pl.edu.dik.adapters.repository.game;

import pl.edu.dik.adapters.model.game.GameEnt;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GameRepository {

    GameEnt save(GameEnt game);

    Optional<GameEnt> findById(UUID id);

    List<GameEnt> findAll();

    void deleteById(UUID id);

    GameEnt update(GameEnt updatedEntity);
}
