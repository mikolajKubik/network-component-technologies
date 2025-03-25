package pl.edu.dik.application.services.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.dik.domain.model.rent.Rent;
import pl.edu.dik.ports._interface.InactiveRentService;
import pl.edu.dik.ports.infrastructure.inactiveRent.ReadInactiveRentPort;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InactiveRentServiceMockTest {

    @Mock
    private ReadInactiveRentPort readInactiveRentPort;

    private InactiveRentService inactiveRentService;

    private UUID rentId;
    private Rent rent;

    @BeforeEach
    void setUp() {
        inactiveRentService = new InactiveRentServiceImpl(readInactiveRentPort);

        rentId = UUID.randomUUID();
        rent = new Rent(rentId, LocalDate.now(), LocalDate.now().plusDays(1), null, null, 100);
    }

    @SneakyThrows
    @Test
    void findInactiveRentById() {
        when(readInactiveRentPort.findById(rentId)).thenReturn(Optional.of(rent));

        Rent result = inactiveRentService.findInactiveRentById(rentId);

        assertThat(result).isEqualTo(rent);

        verify(readInactiveRentPort, times(1)).findById(rentId);
    }

    @Test
    void findInactiveRentNotFoundTest() {
        when(readInactiveRentPort.findById(rentId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> inactiveRentService.findInactiveRentById(rentId))
                .isInstanceOf(Exception.class);

        verify(readInactiveRentPort, times(1)).findById(rentId);
    }

    @Test
    void getInactiveRentsByClientId() {
        when(readInactiveRentPort.getRentsByAccountId(rentId)).thenReturn(List.of(rent));

        List<Rent> result = inactiveRentService.getInactiveRentsByClientId(rentId);

        assertThat(result).isNotNull();

        verify(readInactiveRentPort, times(1)).getRentsByAccountId(rentId);
    }
}