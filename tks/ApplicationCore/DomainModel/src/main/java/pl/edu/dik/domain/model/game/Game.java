package pl.edu.dik.domain.model.game;

import lombok.*;

import java.util.UUID;

@Data
@Getter @Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Game {

    private UUID id;

    private String name;

    private int pricePerDay;

    private int rentalStatusCount;

    private int minPlayers;

    private int maxPlayers;
}
