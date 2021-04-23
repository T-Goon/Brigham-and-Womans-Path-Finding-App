package edu.wpi.teamB.database;

import edu.wpi.teamB.entities.IStoredEntity;
import edu.wpi.teamB.entities.User;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserPasswordMatch implements IStoredEntity {
    User user;
    String password;
}
