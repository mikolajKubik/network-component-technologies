package pl.edu.dik.ports.infrastructure.game;

import pl.edu.dik.domain.model.game.Game;

public interface UpdateGamePort {

    Game update(Game updatedEntity);
}
