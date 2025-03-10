package pl.edu.dik.adapters.repository.game;

import pl.edu.dik.adapters.model.game.GameEnt;

import java.util.List;
import java.util.Optional;

public interface GameRepository {

    GameEnt save(GameEnt game);

    Optional<GameEnt> findById(Object id);

    List<GameEnt> findAll();

    void deleteById(Object id);

    GameEnt update(GameEnt updatedEntity);
}
