package pl.edu.dik.ports.service;

import pl.edu.dik.domain.model.game.Game;
import pl.edu.dik.ports.exception.business.GameNotFoundException;
import pl.edu.dik.ports.exception.business.GameRentedException;
import pl.edu.dik.ports.exception.business.IncorrectPlayerNumberException;

import java.util.List;
import java.util.UUID;

public interface GameService {

    Game createGame(Game game) throws IncorrectPlayerNumberException;

    Game findGameById(UUID id) throws GameNotFoundException;

    List<Game> getAllGames();

    Game updateGame(Game game) throws GameNotFoundException;

    void deleteGameById(UUID id) throws GameRentedException, GameNotFoundException;
}

