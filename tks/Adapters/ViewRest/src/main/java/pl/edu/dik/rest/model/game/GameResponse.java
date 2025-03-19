package pl.edu.dik.rest.model.game;

import lombok.Data;

import java.util.UUID;

@Data
public class GameResponse {
    private UUID id;
    private String name;
    private int pricePerDay;
    private int minPlayers;
    private int maxPlayers;
    private int rentalStatusCount;
}
