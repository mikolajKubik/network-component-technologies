package pl.edu.dik.ports.infrastructure.game;

import pl.edu.dik.domain.model.game.Game;

import java.util.List;
import java.util.Optional;

public interface ReadGamePort {

    Optional<Game> findById(Object id);

    List<Game> findAll();
}
