package pl.edu.dik.adapters.aggregate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.dik.adapters.model.rent.RentEnt;
import pl.edu.dik.adapters.repository.inactiveRent.InactiveRentRepository;
import pl.edu.dik.domain.model.rent.Rent;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InactiveRentRepositoryAdapterMockTest {

    @Mock
    private InactiveRentRepository inactiveRentRepository;
    private InactiveRentRepositoryAdapter inactiveRentRepositoryAdapter;

    private UUID rentId;
    private RentEnt rentEnt;

    @BeforeEach
    void setUp() {
        inactiveRentRepositoryAdapter = new InactiveRentRepositoryAdapter(inactiveRentRepository, null);

        rentId = UUID.randomUUID();
        rentEnt = new RentEnt(rentId, LocalDate.now(), LocalDate.now().plusDays(1), any(), any(), any());
    }

    @Test
    void findByIdTest() {
        when(inactiveRentRepository.findById(rentId)).thenReturn(Optional.ofNullable(rentEnt));

        Optional<Rent> result = inactiveRentRepositoryAdapter.findById(rentId);

        assertThat(result)
                .isPresent();

        verify(inactiveRentRepository, times(1)).findById(rentId);
    }

    @Test
    void getRentsByAccountId() {

    }
}