package pl.edu.dik.application.services.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.dik.domain.model.game.Game;
import pl.edu.dik.ports.exception.business.GameNotFoundException;
import pl.edu.dik.ports.exception.business.GameRentedException;
import pl.edu.dik.ports.exception.business.IncorrectPlayerNumberException;
import pl.edu.dik.ports.infrastructure.game.CreateGamePort;
import pl.edu.dik.ports.infrastructure.game.DeleteGamePort;
import pl.edu.dik.ports.infrastructure.game.ReadGamePort;
import pl.edu.dik.ports.infrastructure.game.UpdateGamePort;
import pl.edu.dik.ports._interface.GameService;


import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final CreateGamePort createGamePort;
    private final ReadGamePort readGamePort;
    private final UpdateGamePort updateGamePort;
    private final DeleteGamePort deleteGamePort;

    public Game createGame(Game game) throws IncorrectPlayerNumberException {
        if (game.getMaxPlayers() < game.getMinPlayers()) {
            throw new IncorrectPlayerNumberException("Max players cannot be less than min players");
        }
        return createGamePort.save(game);
    }

    public Game findGameById(UUID id) throws GameNotFoundException {
        return readGamePort.findById(id)
                .orElseThrow(() -> new GameNotFoundException("Game with ID " + id + " not found"));
    }

    public List<Game> getAllGames() {
        return readGamePort.findAll();
    }

    public Game updateGame(Game game) throws GameNotFoundException {
        Game foundGame = readGamePort.findById(game.getId())
                .orElseThrow(() -> new GameNotFoundException("Game with ID " + game.getId() + " not found"));

        if (!game.getName().equals(foundGame.getName())) {
            foundGame.setName(game.getName());
        }
        if (game.getPricePerDay() != foundGame.getPricePerDay()) {
            foundGame.setPricePerDay(game.getPricePerDay());
        }

        return updateGamePort.update(foundGame);
    }

    public void deleteGameById(UUID id) throws GameRentedException, GameNotFoundException {
        Game game = readGamePort.findById(id)
                .orElseThrow(() -> new GameNotFoundException("Game with ID " + id + " not found"));

        if (game.getRentalStatusCount() != 0) {
            throw new GameRentedException("Game with ID " + id + " is currently rented.");
        }
        deleteGamePort.deleteById(game.getId());
    }
}
