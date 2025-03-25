package pl.edu.dik.application.services.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.dik.domain.model.rent.Rent;
import pl.edu.dik.ports._interface.RentService;
import pl.edu.dik.ports.exception.business.RentNotFoundException;
import pl.edu.dik.ports.exception.business.StartDateBeforeEndDateException;
import pl.edu.dik.ports.infrastructure.rent.CreateRentPort;
import pl.edu.dik.ports.infrastructure.rent.DeleteRentPort;
import pl.edu.dik.ports.infrastructure.rent.ReadRentPort;
import pl.edu.dik.ports.infrastructure.rent.UpdateRentPort;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RentServiceTestMockTest {

    @Mock
    private CreateRentPort createRentPort;
    @Mock
    private ReadRentPort readRentPort;
    @Mock
    private UpdateRentPort updateRentPort;
    @Mock
    private DeleteRentPort deleteRentPort;

    private RentService rentService;

    private UUID rentId;
    private UUID gameId;
    private UUID accountId;
    private Rent rent;

    @BeforeEach
    void setUp() {
        rentService = new RentServiceImpl(createRentPort, readRentPort, updateRentPort, deleteRentPort);

        rentId = UUID.randomUUID();
        gameId = UUID.randomUUID();
        accountId = UUID.randomUUID();
        rent = new Rent(rentId, LocalDate.now(), LocalDate.now().plusDays(1), null, null, 100);
    }

    @SneakyThrows
    @Test
    void createRent() {
        when(createRentPort.save(LocalDate.now(), LocalDate.now().plusDays(1), null, null)).thenReturn(rent);

        Rent result = rentService.createRent(LocalDate.now(), LocalDate.now().plusDays(1), null, null);

        assertThat(result).isEqualTo(rent);

        verify(createRentPort, times(1)).save(LocalDate.now(), LocalDate.now().plusDays(1), null, null);
    }

    @SneakyThrows
    @Test
    void creatRentStartEndDatesErrorTest() {
        assertThatThrownBy(() -> rentService.createRent(LocalDate.now().plusDays(10), LocalDate.now().plusDays(1), null, null))
                .isInstanceOf(StartDateBeforeEndDateException.class);

        verify(createRentPort, never()).save(LocalDate.now().plusDays(10), LocalDate.now().plusDays(1), null, null);
    }

    @SneakyThrows
    @Test
    void findRentById() {
        when(readRentPort.findById(rentId)).thenReturn(Optional.of(rent));

        Rent result = rentService.findRentById(rentId);

        assertEquals(rent, result);
        verify(readRentPort, times(1)).findById(rentId);
    }

    @Test
    void findRentByIdRentNotFound() {
        when(readRentPort.findById(rentId)).thenReturn(Optional.empty());

        assertThrows(RentNotFoundException.class, () -> rentService.findRentById(rentId));
    }

    @Test
    void getAllRents() {
        when(readRentPort.findAll()).thenReturn(List.of(rent));

        List<Rent> result = rentService.getAllRents();

        assertEquals(1, result.size());
        verify(readRentPort, times(1)).findAll();
    }

    @Test
    void getActiveRentsByClientId() {
        when(readRentPort.getRentsByAccountId(accountId)).thenReturn(List.of(rent));

        List<Rent> result = rentService.getActiveRentsByClientId(accountId);

        assertEquals(1, result.size());
        verify(readRentPort, times(1)).getRentsByAccountId(accountId);
    }



    @SneakyThrows
    @Test
    void endRent() {
        rentService.endRent(rentId);

        verify(deleteRentPort, times(1)).deleteById(rentId);
    }

    @SneakyThrows
    @Test
    void endRentNotFoundException() {
        doThrow(new RentNotFoundException("Rent not found"))
                .when(deleteRentPort).deleteById(rentId);
        assertThrows(RentNotFoundException.class, () -> rentService.endRent(rentId));
    }

    @Test
    void updateRent() {
        when(updateRentPort.update(rent)).thenReturn(rent);

        Rent result = rentService.updateRent(rent);

        assertEquals(rent, result);
        verify(updateRentPort, times(1)).update(rent);
    }
}