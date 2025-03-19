package pl.edu.dik.domain.model.rent;

import lombok.*;
import pl.edu.dik.domain.model.account.Account;
import pl.edu.dik.domain.model.game.Game;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Getter @Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Rent {

    private UUID id;

    private LocalDate startDate;

    private LocalDate endDate;

    private Account account;

    private Game game;

    private int rentalPrice;
}
