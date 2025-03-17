package pl.edu.dik.adapters.aggregate;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import pl.edu.dik.adapters.exception.*;
import pl.edu.dik.adapters.model.rent.RentEnt;
import pl.edu.dik.adapters.repository.rent.RentRepository;
import pl.edu.dik.domain.model.rent.Rent;
import pl.edu.dik.ports.exception.business.*;
import pl.edu.dik.ports.infrastructure.rent.CreateRentPort;
import pl.edu.dik.ports.infrastructure.rent.DeleteRentPort;
import pl.edu.dik.ports.infrastructure.rent.ReadRentPort;
import pl.edu.dik.ports.infrastructure.rent.UpdateRentPort;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RentRepositoryAdapter implements CreateRentPort, DeleteRentPort, ReadRentPort, UpdateRentPort {

    private final RentRepository rentRepository;
    private final ModelMapper modelMapper;

    @Override
    public Rent save(LocalDate startDate, LocalDate endDate, String login, UUID gameId) throws ClientNotAvailableForRentException, GameNotAvailableForRentException {
        try {
            return modelMapper.map(rentRepository.save(startDate, endDate, login, gameId), Rent.class);
        } catch (ClientNotAvailableForRentRepositoryException e) {
            throw new ClientNotAvailableForRentException(e.getMessage());
        } catch (GameNotAvailableForRentRepositoryException e) {
            throw new GameNotAvailableForRentException(e.getMessage());
        }
    }

    @Override
    public void deleteById(UUID id) throws ClientRentCancellationException, GameRentCancellationException, RentNotFoundException {
        try {
            rentRepository.deleteById(id);
        } catch (ClientRentCancellationRepositoryException e) {
            throw new ClientRentCancellationException(e.getMessage());
        } catch (GameRentCancellationRepositoryException e) {
            throw new GameRentCancellationException(e.getMessage());
        } catch (RentNotFoundRepositoryException e) {
            throw new RentNotFoundException(e.getMessage());
        }
    }

    @Override
    public Optional<Rent> findById(UUID id) {
        return Optional.ofNullable(modelMapper.map(rentRepository.findById(id), Rent.class));
    }

    @Override
    public List<Rent> findAll() {
        return rentRepository.findAll().stream().map(rentEntity -> modelMapper.map(rentEntity, Rent.class)).collect(Collectors.toList());
    }

    @Override
    public List<Rent> getRentsByAccountId(UUID clientId) {
        return rentRepository.getRentsByAccountId(clientId).stream().map(rentEntity -> modelMapper.map(rentEntity, Rent.class)).collect(Collectors.toList());
    }

    @Override
    public Rent update(Rent rent) {
        return modelMapper.map(rentRepository.update(modelMapper.map(rent, RentEnt.class)), Rent.class);
    }
}
