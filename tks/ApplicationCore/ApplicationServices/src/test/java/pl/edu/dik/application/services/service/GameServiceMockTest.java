package pl.edu.dik.application.services.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.dik.domain.model.game.Game;
import pl.edu.dik.ports._interface.GameService;
import pl.edu.dik.ports.exception.business.GameNotFoundException;
import pl.edu.dik.ports.exception.business.GameRentedException;
import pl.edu.dik.ports.exception.business.IncorrectPlayerNumberException;
import pl.edu.dik.ports.infrastructure.game.CreateGamePort;
import pl.edu.dik.ports.infrastructure.game.DeleteGamePort;
import pl.edu.dik.ports.infrastructure.game.ReadGamePort;
import pl.edu.dik.ports.infrastructure.game.UpdateGamePort;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameServiceMockTest {

    @Mock
    private CreateGamePort createGamePort;

    @Mock
    private ReadGamePort readGamePort;

    @Mock
    private UpdateGamePort updateGamePort;

    @Mock
    private DeleteGamePort deleteGamePort;

    private GameService gameService;
    private UUID gameId;
    private Game game;

    @BeforeEach
    void setUp() {
        gameService = new GameServiceImpl(createGamePort, readGamePort, updateGamePort, deleteGamePort);

        gameId = UUID.randomUUID();
        game = new Game(gameId, "Game", 10, 0, 1, 10);
    }

    @SneakyThrows
    @Test
    void createGame() {
        when(createGamePort.save(game)).thenReturn(game);

        Game result = gameService.createGame(game);

        assertThat(result).isEqualTo(game);

        verify(createGamePort, times(1)).save(game);
    }

    @Test
    void createGameIncorrectPlayersNumberTest() {
        Game game = new Game(gameId, "Game", 10, 0, 0, 10);

        assertThatThrownBy(() -> gameService.createGame(game))
                .isInstanceOf(IncorrectPlayerNumberException.class);

        verify(createGamePort, never()).save(game);
    }

    @SneakyThrows
    @Test
    void findGameByIdTest() {
        when(readGamePort.findById(gameId)).thenReturn(Optional.ofNullable(game));

        Game result = gameService.findGameById(gameId);

        assertThat(result).isEqualTo(game);

        verify(readGamePort, times(1)).findById(gameId);
    }

    @SneakyThrows
    @Test
    void updateGameTest() {
        when(readGamePort.findById(gameId)).thenReturn(Optional.ofNullable(game));
        when(updateGamePort.update(game)).thenReturn(game);

        Game result = gameService.updateGame(game);

        assertThat(result).isEqualTo(game);

        verify(readGamePort, times(1)).findById(gameId);
        verify(updateGamePort, times(1)).update(game);
    }

    @Test
    void updateGameGameNotFoundTest() {
        when(readGamePort.findById(gameId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> gameService.updateGame(game))
                .isInstanceOf(GameNotFoundException.class);

        verify(readGamePort, times(1)).findById(gameId);
        verify(updateGamePort, never()).update(game);
    }

    @Test
    void deleteGameById() {
        when(readGamePort.findById(gameId)).thenReturn(Optional.ofNullable(game));
        doNothing().when(deleteGamePort).deleteById(gameId);

        assertDoesNotThrow(() -> gameService.deleteGameById(gameId));

        verify(readGamePort, times(1)).findById(gameId);
        verify(deleteGamePort, times(1)).deleteById(gameId);
    }

    @Test
    void deleteGameNotFoundTest() {
        when(readGamePort.findById(gameId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> gameService.deleteGameById(gameId))
                .isInstanceOf(GameNotFoundException.class);

        verify(readGamePort, times(1)).findById(gameId);
        verify(deleteGamePort, never()).deleteById(gameId);
    }

    @Test
    void deleteGameGameCurrentlyRentedTest() {
        Game game = new Game(gameId, "Game", 10, 1, 1, 10);
        when(readGamePort.findById(gameId)).thenReturn(Optional.of(game));

        assertThatThrownBy(() -> gameService.deleteGameById(gameId))
                .isInstanceOf(GameRentedException.class);

        verify(readGamePort, times(1)).findById(gameId);
        verify(deleteGamePort, never()).deleteById(gameId);
    }
}