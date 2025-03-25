package pl.edu.dik.rest.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.edu.dik.domain.model.game.Game;
import pl.edu.dik.ports._interface.GameService;
import pl.edu.dik.ports.exception.business.GameNotFoundException;
import pl.edu.dik.rest.exception.AppExceptionHandler;
import pl.edu.dik.rest.model.game.CreateGameRequest;
import pl.edu.dik.rest.model.game.GameResponse;
import pl.edu.dik.rest.model.game.UpdateGameRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.Mockito.*;

import java.util.List;
import java.util.UUID;


@ExtendWith(SpringExtension.class)
class GameControllerMockTest {

    @InjectMocks
    private GameController gameController;

    @Mock
    private GameService gameService;

    @Mock
    private ModelMapper modelMapper;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private UUID gameId;
    private Game game;
    private Game updatedGame;
    private GameResponse gameResponse;
    private GameResponse updatedGameResponse;
    private CreateGameRequest createGameRequest;
    private UpdateGameRequest updateGameRequest;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(gameController)
                .setControllerAdvice(new AppExceptionHandler())
                .build();

        gameId = UUID.randomUUID();
        game = new Game(gameId, "Chess", 20, 1, 2, 2);
        updatedGame = new Game(gameId, "Chess2", 25, 1, 2, 2);

        gameResponse = new GameResponse();
        gameResponse.setId(gameId);
        gameResponse.setName("Chess");
        gameResponse.setMinPlayers(2);
        gameResponse.setMaxPlayers(2);
        gameResponse.setPricePerDay(20);
        gameResponse.setRentalStatusCount(1);

        updatedGameResponse = new GameResponse();
        updatedGameResponse.setId(gameId);
        updatedGameResponse.setName("Chess2");
        updatedGameResponse.setMinPlayers(2);
        updatedGameResponse.setMaxPlayers(2);
        updatedGameResponse.setPricePerDay(25);
        updatedGameResponse.setRentalStatusCount(1);

        createGameRequest = new CreateGameRequest();
        createGameRequest.setName("Chess");
        createGameRequest.setPricePerDay(20);
        createGameRequest.setMinPlayers(2);
        createGameRequest.setMaxPlayers(2);

        updateGameRequest = new UpdateGameRequest();
        updateGameRequest.setName("Chess2");
        updateGameRequest.setPricePerDay(25);
        updateGameRequest.setId(gameId);
    }

    @SneakyThrows
    @Test
    void createGame() {
        when(modelMapper.map(any(CreateGameRequest.class), any())).thenReturn(game);
        when(gameService.createGame(any(Game.class))).thenReturn(game);
        when(modelMapper.map(any(Game.class), any())).thenReturn(gameResponse);

        mockMvc.perform(post("/api/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createGameRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(gameId.toString()))
                .andExpect(jsonPath("$.name").value("Chess"));
    }

    @SneakyThrows
    @Test
    void returnGameById() {
        when(gameService.findGameById(gameId)).thenReturn(game);
        when(modelMapper.map(any(Game.class), any())).thenReturn(gameResponse);

        mockMvc.perform(get("/api/games/{id}", gameId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(gameId.toString()))
                .andExpect(jsonPath("$.name").value("Chess"));
    }

    @SneakyThrows
    @Test
    void returnNotFoundForNonExistingGame() {
        when(gameService.findGameById(gameId)).thenThrow(new GameNotFoundException("Game not found"));

        mockMvc.perform(get("/api/games/{id}", gameId))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void returnAllGames() {
        when(gameService.getAllGames()).thenReturn(List.of(game));
        when(modelMapper.map(any(Game.class), any())).thenReturn(gameResponse);

        mockMvc.perform(get("/api/games"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(gameId.toString()))
                .andExpect(jsonPath("$[0].name").value("Chess"));
    }

    @SneakyThrows
    @Test
    void updateGame() {
        when(modelMapper.map(any(UpdateGameRequest.class), any())).thenReturn(updatedGame);
        when(gameService.updateGame(any(Game.class))).thenReturn(updatedGame);
        when(modelMapper.map(any(Game.class), any())).thenReturn(updatedGameResponse);

        mockMvc.perform(put("/api/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateGameRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Chess2"))
                .andExpect(jsonPath("$.pricePerDay").value(25));
    }

    @SneakyThrows
    @Test
    void deleteGame()  {
        mockMvc.perform(delete("/api/games/{id}", gameId))
                .andExpect(status().isNoContent());
    }
}