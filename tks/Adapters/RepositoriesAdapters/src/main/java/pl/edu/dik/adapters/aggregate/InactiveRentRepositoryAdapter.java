package pl.edu.dik.adapters.aggregate;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import pl.edu.dik.adapters.repository.inactiveRent.InactiveRentRepository;
import pl.edu.dik.domain.model.rent.Rent;
import pl.edu.dik.ports.infrastructure.inactiveRent.ReadInactiveRentPort;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InactiveRentRepositoryAdapter implements ReadInactiveRentPort {

    private final InactiveRentRepository inactiveRentRepository;
    private final ModelMapper modelMapper;

    @Override
    public Optional<Rent> findById(UUID id) {
        return Optional.ofNullable(modelMapper.map(inactiveRentRepository.findById(id), Rent.class));
    }

    @Override
    public List<Rent> findAll() {
        return inactiveRentRepository.findAll().stream().map(inactiveRent -> modelMapper.map(inactiveRent, Rent.class)).collect(Collectors.toList());
    }

    @Override
    public List<Rent> getRentsByAccountId(UUID clientId) {
        return inactiveRentRepository.getRentsByAccountId(clientId).stream().map(inactiveRent -> modelMapper.map(inactiveRent, Rent.class)).collect(Collectors.toList());
    }
}
