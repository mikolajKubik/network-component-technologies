package pl.edu.dik.ports.infrastructure.game;

import java.util.UUID;

public interface DeleteGamePort {

    void deleteById(UUID id);

}
