package pl.edu.dik.adapters.aggregate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.dik.adapters.repository.inactiveRent.InactiveRentRepository;
import pl.edu.dik.domain.model.rent.Rent;
import pl.edu.dik.ports.infrastructure.inactiveRent.ReadInactiveRentPort;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class InactiveRentRepositoryAdapter implements ReadInactiveRentPort {

    private final InactiveRentRepository inactiveRentRepository;

    @Override
    public Optional<Rent> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public List<Rent> findAll() {
        return List.of();
    }

    @Override
    public List<Rent> getRentsByAccountId(UUID clientId) {
        return List.of();
    }
}
