package pl.edu.dik.adapters.aggregate;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import pl.edu.dik.adapters.model.rent.RentEnt;
import pl.edu.dik.adapters.repository.rent.RentRepository;
import pl.edu.dik.domain.model.rent.Rent;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class RentRepositoryAdapterMockTest {

    @Mock
    private RentRepository rentRepository;
    private RentRepositoryAdapter rentRepositoryAdapter;

    private Rent rent;
    private RentEnt rentEnt;
    private UUID rentId;
    private UUID accountId;
    private UUID gameId;

    @BeforeEach
    void setUp() {
        rentRepositoryAdapter = new RentRepositoryAdapter(rentRepository, new ModelMapper());

        rentId = UUID.randomUUID();
        gameId = UUID.randomUUID();
        rentEnt = new RentEnt(rentId, LocalDate.now(), LocalDate.now().plusDays(1), null, null, 100);
        rent = new Rent(rentId, LocalDate.now(), LocalDate.now().plusDays(1), null, null, 100);
    }

    @SneakyThrows
    @Test
    void save() {
        when(rentRepository.save(LocalDate.now(), LocalDate.now().plusDays(1), "login", gameId)).thenReturn(rentEnt);

        Rent result = rentRepositoryAdapter.save(LocalDate.now(), LocalDate.now().plusDays(1), "login", gameId);

        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(rentEnt);

        verify(rentRepository, times(1)).save(LocalDate.now(), LocalDate.now().plusDays(1), "login", gameId);
    }

    @SneakyThrows
    @Test
    void deleteById() {
        rentRepositoryAdapter.deleteById(rentId);

        verify(rentRepository, times(1)).deleteById(rentId);
    }

    @Test
    void findById() {
        when(rentRepository.findById(rentId)).thenReturn(Optional.ofNullable(rentEnt));

        Optional<Rent> result = rentRepositoryAdapter.findById(rentId);

        assertThat(result)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(rentEnt);

        verify(rentRepository, times(1)).findById(rentId);
    }

    @Test
    void getRentsByAccountId() {
        accountId = UUID.randomUUID();
        when(rentRepository.getRentsByAccountId(accountId)).thenReturn(List.of(rentEnt));

        List<Rent> result = rentRepositoryAdapter.getRentsByAccountId(accountId);

        assertThat(result).isNotNull();

        verify(rentRepository, times(1)).getRentsByAccountId(accountId);
    }

    @Test
    void update() {
        when(rentRepository.update(rentEnt)).thenReturn(rentEnt);

        Rent result = rentRepositoryAdapter.update(rent);

        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(rentEnt);
    }
}