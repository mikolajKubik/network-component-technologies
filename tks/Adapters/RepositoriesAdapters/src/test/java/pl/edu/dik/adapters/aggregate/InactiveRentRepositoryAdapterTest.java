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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class InactiveRentRepositoryAdapterTest {

    @Mock
    private InactiveRentRepository inactiveRentRepository;
    private InactiveRentRepositoryAdapter inactiveRentRepositoryAdapter;

    private UUID rentId;
    private Rent rent;
    private RentEnt rentEnt;

    @BeforeEach
    void setUp() {
        inactiveRentRepositoryAdapter = new InactiveRentRepositoryAdapter(inactiveRentRepository, null);

        rentId = UUID.randomUUID();
        rentEnt = new RentEnt();
    }

    @Test
    void findByIdTest() {

    }

    @Test
    void findAll() {
    }

    @Test
    void getRentsByAccountId() {
    }
}