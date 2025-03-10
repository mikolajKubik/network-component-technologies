package pl.edu.dik.adapters.aggregate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.dik.adapters.repository.rent.RentRepository;
import pl.edu.dik.domain.model.game.Game;
import pl.edu.dik.domain.model.rent.Rent;
import pl.edu.dik.ports.infrastructure.game.CreateGamePort;
import pl.edu.dik.ports.infrastructure.game.DeleteGamePort;
import pl.edu.dik.ports.infrastructure.game.ReadGamePort;
import pl.edu.dik.ports.infrastructure.game.UpdateGamePort;
import pl.edu.dik.ports.infrastructure.rent.DeleteRentPort;
import pl.edu.dik.ports.infrastructure.rent.ReadRentPort;
import pl.edu.dik.ports.infrastructure.rent.UpdateRentPort;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RentRepositoryAdapter implements CreateGamePort, DeleteRentPort, ReadRentPort, UpdateRentPort {

    private final RentRepository rentRepository;

    @Override
    public Game save(Game game) {
        return null;
    }

    @Override
    public void deleteById(Object id) {

    }

    @Override
    public Optional<Rent> findById(Object id) {
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

    @Override
    public Rent update(Rent rent) {
        return null;
    }
}
