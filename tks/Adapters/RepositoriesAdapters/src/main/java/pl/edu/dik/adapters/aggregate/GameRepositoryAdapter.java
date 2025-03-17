package pl.edu.dik.adapters.aggregate;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import pl.edu.dik.adapters.model.game.GameEnt;
import pl.edu.dik.adapters.repository.game.GameRepository;
import pl.edu.dik.domain.model.game.Game;
import pl.edu.dik.ports.infrastructure.game.CreateGamePort;
import pl.edu.dik.ports.infrastructure.game.DeleteGamePort;
import pl.edu.dik.ports.infrastructure.game.ReadGamePort;
import pl.edu.dik.ports.infrastructure.game.UpdateGamePort;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GameRepositoryAdapter implements CreateGamePort, UpdateGamePort, DeleteGamePort, ReadGamePort {

    private final GameRepository gameRepository;
    private final ModelMapper modelMapper;

    @Override
    public Game save(Game game) {
        return modelMapper.map(gameRepository.save(modelMapper.map(game, GameEnt.class)), Game.class);
    }

    @Override
    public void deleteById(UUID id) {
        gameRepository.deleteById(id);
    }

    @Override
    public Optional<Game> findById(UUID id) {
        return Optional.ofNullable(modelMapper.map(gameRepository.findById(id), Game.class));
    }

    @Override
    public List<Game> findAll() {
        return gameRepository.findAll().stream().map(gameEntity -> modelMapper.map(gameEntity, Game.class)).collect(Collectors.toList());
    }

    @Override
    public Game update(Game updatedEntity) {
        return modelMapper.map(gameRepository.update(modelMapper.map(updatedEntity, GameEnt.class)), Game.class);
    }
}

