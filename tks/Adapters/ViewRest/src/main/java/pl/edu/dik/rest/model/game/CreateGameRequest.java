package pl.edu.dik.rest.model.game;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateGameRequest {
    @NotNull(message = "Not null")
    @NotEmpty(message = "Name is required")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;

    @Min(value = 1, message = "Price per day must be at least 1")
    @Max(value = 100, message = "Price per day must be at most 100")
    private int pricePerDay;

    @Min(value = 1, message = "Min players must be at least 1")
    private int minPlayers;

    @Min(value = 1, message = "Max players must be at least 1")
    private int maxPlayers;
}
