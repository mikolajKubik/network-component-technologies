package pl.edu.dik.adapters.aggregate;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import pl.edu.dik.adapters.exception.DuplicatedKeyRepositoryException;
import pl.edu.dik.adapters.model.account.AccountEnt;
import pl.edu.dik.adapters.repository.auth.AuthRepository;
import pl.edu.dik.domain.model.account.Account;
import pl.edu.dik.ports.exception.business.DuplicatedKeyException;
import pl.edu.dik.ports.infrastructure.auth.CreateAuthPort;
import pl.edu.dik.ports.infrastructure.auth.ReadAuthPort;
import pl.edu.dik.ports.infrastructure.auth.UpdateAuthPort;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthRepositoryAdapter implements CreateAuthPort, ReadAuthPort, UpdateAuthPort {

    private final AuthRepository authRepository;
    private final ModelMapper modelMapper;

    @Override
    public Account save(Account object) throws DuplicatedKeyException {
        try {
            return modelMapper.map(authRepository.save(modelMapper.map(object, AccountEnt.class)), Account.class);
        } catch (DuplicatedKeyRepositoryException e) {
            throw new DuplicatedKeyException(e.getMessage());
        }
    }

    @Override
    public Optional<Account> findByLogin(String login) {
        return Optional.of(modelMapper.map(authRepository.findByLogin(login), Account.class));
    }

    @Override
    public boolean update(Account account) {
        return authRepository.update(modelMapper.map(account, AccountEnt.class));
    }
}
