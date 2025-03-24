package pl.edu.dik.adapters.aggregate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import pl.edu.dik.adapters.model.game.GameEnt;
import pl.edu.dik.adapters.repository.game.GameRepository;
import pl.edu.dik.domain.model.game.Game;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameRepositoryAdapterMockTest {

    @Mock
    private GameRepository gameRepository;
    private GameRepositoryAdapter gameRepositoryAdapter;

    private UUID gameId;
    private GameEnt gameEnt;
    private Game game;


    @BeforeEach
    void setUp() {
        gameRepositoryAdapter = new GameRepositoryAdapter(gameRepository, new ModelMapper());

        gameId = UUID.randomUUID();
        gameEnt = new GameEnt(gameId, "Game", 10, 0, 1, 10);
        game = new Game(gameId, "Game", 10, 0, 1, 10);
    }

    @Test
    void saveTest() {
        when(gameRepository.save(gameEnt)).thenReturn(gameEnt);

        Game result = gameRepositoryAdapter.save(game);

        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(gameEnt);

        verify(gameRepository).save(gameEnt);
    }

    @Test
    void deleteByIdTest() {
        gameRepositoryAdapter.deleteById(gameId);

        verify(gameRepository).deleteById(gameId);
    }

    @Test
    void findByIdTest() {
        when(gameRepository.findById(gameId)).thenReturn(Optional.ofNullable(gameEnt));

        Optional<Game> result = gameRepositoryAdapter.findById(gameId);

        assertThat(result).isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(gameEnt);

        verify(gameRepository).findById(gameId);
    }

    @Test
    void update() {
        when(gameRepository.update(gameEnt)).thenReturn(gameEnt);

        Game result = gameRepositoryAdapter.update(game);

        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(gameEnt);

        verify(gameRepository).update(gameEnt);
    }
}