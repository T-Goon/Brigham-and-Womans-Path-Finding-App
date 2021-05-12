package edu.wpi.cs3733.D21.teamB.database;

import edu.wpi.cs3733.D21.teamB.entities.Embedding;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class FaceMutator implements IDatabaseEntityMutator<Embedding>{

    private final DatabaseHandler db;

    public FaceMutator(DatabaseHandler db){
        this.db = db;
    }

    @Override
    public void addEntity(Embedding entity) throws SQLException {
        String query = "INSERT INTO Embeddings VALUES ";
        for(int i = 0; i < entity.getEmbedding().size(); i++){
            query += "('" + entity.getUsername() +
                    "', '"+ i +
                    "', '"+ entity.getEmbedding().get(i) +
                    "'),";
        }
        db.runStatement(query.substring(0,query.length()-1), false);
    }

    @Override
    public void removeEntity(String id) throws SQLException {
            String query = "DELETE FROM Embeddings WHERE username = '" +id+ "'";
            db.runStatement(query, false);
    }

    @Override
    public void updateEntity(Embedding entity) throws SQLException {
        this.removeEntity(entity.getUsername());
        this.addEntity(entity);
    }

    public HashMap<String, ArrayList<Double>> getEmbeddings() throws SQLException {
        HashMap<String, ArrayList<Double>> embeddings = new HashMap<>();
        String query = "SELECT * FROM Embeddings";
        ResultSet embeddingResultSet = db.runStatement(query,true);

        if(embeddingResultSet == null){
            return embeddings;
        }

        do{
            if(!embeddings.containsKey(embeddingResultSet.getString("username"))){
                embeddings.put(embeddingResultSet.getString("username"), new ArrayList<>());
            }

            embeddings.get(embeddingResultSet.getString("username")).add(embeddingResultSet.getDouble("value"));
        } while(embeddingResultSet.next());
        return embeddings;
    }
}
