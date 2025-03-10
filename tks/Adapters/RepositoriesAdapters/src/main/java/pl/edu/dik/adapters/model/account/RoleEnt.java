package pl.edu.dik.adapters.model.account;

import org.bson.codecs.pojo.annotations.BsonProperty;

public enum RoleEnt {
    @BsonProperty("ADMIN")
    ADMIN,

    @BsonProperty("CLIENT")
    CLIENT,

    @BsonProperty("EMPLOYEE")
    EMPLOYEE
}
