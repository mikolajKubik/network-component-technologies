package pl.edu.dik.adapters.aggregate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.dik.adapters.repository.game.GameRepository;
import pl.edu.dik.domain.model.game.Game;
import pl.edu.dik.ports.infrastructure.game.CreateGamePort;
import pl.edu.dik.ports.infrastructure.game.DeleteGamePort;
import pl.edu.dik.ports.infrastructure.game.ReadGamePort;
import pl.edu.dik.ports.infrastructure.game.UpdateGamePort;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GameRepositoryAdapter implements CreateGamePort, UpdateGamePort, DeleteGamePort, ReadGamePort {

    private final GameRepository gameRepository;

    @Override
    public Game save(Game game) {
        return null;
    }

    @Override
    public void deleteById(Object id) {

    }

    @Override
    public Optional<Game> findById(Object id) {
        return Optional.empty();
    }

    @Override
    public List<Game> findAll() {
        return List.of();
    }

    @Override
    public Game update(Game updatedEntity) {
        return null;
    }
}

