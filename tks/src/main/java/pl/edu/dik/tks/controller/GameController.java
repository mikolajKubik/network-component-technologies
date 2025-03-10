package pl.edu.dik.tks.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.dik.tks.dto.game.CreateGameRequest;
import pl.edu.dik.tks.dto.game.GameResponse;
import pl.edu.dik.tks.dto.game.UpdateGameRequest;
import pl.edu.dik.tks.exception.business.GameNotFoundException;
import pl.edu.dik.tks.exception.business.GameRentedException;
import pl.edu.dik.tks.exception.business.IncorrectPlayerNumberException;
import pl.edu.dik.tks.model.game.Game;
import pl.edu.dik.tks.service.GameService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
public class GameController {

    private final ModelMapper modelMapper;
    private final GameService gameService;

    @PostMapping()
    public ResponseEntity<GameResponse> create(@Valid @RequestBody CreateGameRequest createGameRequest) throws
            IncorrectPlayerNumberException {
        Game game = modelMapper.map(createGameRequest, Game.class);
        Game createdGame = gameService.createGame(game);
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(createdGame, GameResponse.class));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameResponse> findById(@PathVariable UUID id) throws GameNotFoundException {
        Game foundGame = gameService.findGameById(id);
        return ResponseEntity.ok(modelMapper.map(foundGame, GameResponse.class));
    }

    @GetMapping
    public ResponseEntity<List<GameResponse>> findAll() {
        List<Game> games = gameService.getAllGames();
        List<GameResponse> responses = games.stream()
                .map(game -> modelMapper.map(game, GameResponse.class))
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PutMapping
    public ResponseEntity<GameResponse> update(@Valid @RequestBody UpdateGameRequest updateGameRequest) throws GameNotFoundException {
        Game game = modelMapper.map(updateGameRequest, Game.class);
        Game updatedGame = gameService.updateGame(game);
        return ResponseEntity.ok(modelMapper.map(updatedGame, GameResponse.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoardGame(@PathVariable UUID id) throws GameRentedException, GameNotFoundException {
        gameService.deleteGameById(id);
        return ResponseEntity.noContent().build();
    }
}
