package edu.wpi.cs3733.D21.teamB.database;


import edu.wpi.cs3733.D21.teamB.entities.IStoredEntity;

import java.sql.SQLException;

public interface IDatabaseEntityMutator<T extends IStoredEntity> {
    void addEntity(T entity) throws SQLException;

    void removeEntity(String id) throws SQLException;

    void updateEntity(T entity) throws SQLException;
}
