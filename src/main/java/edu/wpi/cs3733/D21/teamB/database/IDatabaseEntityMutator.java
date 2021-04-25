package edu.wpi.cs3733.D21.teamB.database;


import edu.wpi.cs3733.D21.teamB.entities.IStoredEntity;

import java.sql.SQLException;

public interface IDatabaseEntityMutator<T extends IStoredEntity> {
    public void addEntity(T entity) throws SQLException;
    public void removeEntity(String id) throws SQLException;
    public void updateEntity(T entity) throws SQLException;
}
