package pl.edu.dik.adapters.model.account;

import lombok.*;
import org.bson.codecs.pojo.annotations.BsonProperty;
import pl.edu.dik.adapters.model.AbstractEntityEnt;

import java.util.UUID;

@Getter @Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class AccountEnt extends AbstractEntityEnt {

    @BsonProperty("first_name")
    private String firstName;

    @BsonProperty("last_name")
    private String lastName;

    @BsonProperty("role")
    private RoleEnt role;

    @BsonProperty("is_enable")
    private boolean isEnable;

    @BsonProperty("login")
    private String login;

    @BsonProperty("password")
    private String password;

    @BsonProperty("rental_count")
    private int rentalCount;

    public AccountEnt(UUID id, String firstName, String lastName, RoleEnt role, boolean isEnable, String login, String password, int rentalCount) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.isEnable = isEnable;
        this.login = login;
        this.password = password;
        this.rentalCount = rentalCount;
    }
}
