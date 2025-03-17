package pl.edu.dik.ports.infrastructure.game;

import pl.edu.dik.domain.model.game.Game;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadGamePort {

    Optional<Game> findById(UUID id);

    List<Game> findAll();
}
