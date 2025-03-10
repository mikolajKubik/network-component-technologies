package pl.edu.dik.tks.repository.rent;
import pl.edu.dik.tks.model.rent.Rent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InactiveRentRepository {
    Optional<Rent> findById(UUID id);

    List<Rent> findAll();

    List<Rent> getRentsByAccountId(UUID clientId);

}
