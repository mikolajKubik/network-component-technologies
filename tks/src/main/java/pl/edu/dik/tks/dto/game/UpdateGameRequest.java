package pl.edu.dik.tks.dto.game;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.UUID;

@Data
public class UpdateGameRequest {
    @NotNull(message = "Id is required")
    private UUID id;

    @NotEmpty(message = "Name is required")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;

    @Min(value = 1, message = "Price per day must specified and be at least 1")
    @Max(value = 100, message = "Price per day must be specified and be at most 100")
    private int pricePerDay;
}
