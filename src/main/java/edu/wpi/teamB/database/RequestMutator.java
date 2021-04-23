package edu.wpi.teamB.database;

import edu.wpi.teamB.entities.User;
import edu.wpi.teamB.entities.requests.*;
import edu.wpi.teamB.pathfinding.Graph;

import javax.xml.crypto.Data;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RequestMutator implements IDatabaseEntityMutator<Request> {

    /**
     * Adds a request to Requests and the table specific to the given request
     *
     * @param request the request to add
     */
    public void addEntity(Request request) throws SQLException {
        Statement statement = DatabaseHandler.getDatabaseHandler("main.db").getStatement();

        User user = DatabaseHandler.getDatabaseHandler("main.db").getAuthenticationUser();
        String username = user.getUsername();
        if (username == null) {
            username = "null";
        }

        String query = "INSERT INTO Requests VALUES " +
                "('" + request.getRequestID()
                + "', '" + request.getRequestType()
                + "', '" + request.getDate()
                + "', '" + request.getTime()
                + "', '" + request.getComplete()
                + "', '" + request.getEmployeeName()
                + "', '" + request.getLocation()
                + "', '" + request.getDescription().replace("'", "''")
                + "', '" + username
                + "')";

        String current = null;
        try {
            assert statement != null;
            current = query;
            statement.execute(query);
        } catch (SQLException e) {
            System.out.println(current);
            e.printStackTrace();
        }

        switch (request.getRequestType()) {
            case SANITATION:
                SanitationRequest sanitationRequest = (SanitationRequest) request;
                query = "INSERT INTO SanitationRequests VALUES " +
                        "('" + sanitationRequest.getRequestID()
                        + "', '" + sanitationRequest.getSanitationType()
                        + "', '" + sanitationRequest.getSanitationSize()
                        + "', '" + (sanitationRequest.getHazardous())
                        + "', '" + (sanitationRequest.getBiologicalSubstance())
                        + "', '" + (sanitationRequest.getOccupied())
                        + "')";
                break;
            case MEDICINE:
                MedicineRequest medicineRequest = (MedicineRequest) request;
                query = "INSERT INTO MedicineRequests VALUES " +
                        "('" + medicineRequest.getRequestID()
                        + "', '" + medicineRequest.getPatientName().replace("'", "''")
                        + "', '" + medicineRequest.getMedicine().replace("'", "''")
                        + "')";
                break;
            case INTERNAL_TRANSPORT:
                InternalTransportRequest internalTransportRequest = (InternalTransportRequest) request;
                query = "INSERT INTO InternalTransportRequests VALUES " +
                        "('" + internalTransportRequest.getRequestID()
                        + "', '" + internalTransportRequest.getPatientName().replace("'", "''")
                        + "', '" + internalTransportRequest.getTransportType()
                        + "', '" + (internalTransportRequest.getUnconscious())
                        + "', '" + (internalTransportRequest.getInfectious())
                        + "')";
                break;
            case RELIGIOUS:
                ReligiousRequest religiousRequest = (ReligiousRequest) request;
                query = "INSERT INTO ReligiousRequests VALUES " +
                        "('" + religiousRequest.getRequestID()
                        + "', '" + religiousRequest.getPatientName().replace("'", "''")
                        + "', '" + religiousRequest.getReligiousDate()
                        + "', '" + religiousRequest.getStartTime()
                        + "', '" + religiousRequest.getEndTime()
                        + "', '" + religiousRequest.getFaith().replace("'", "''")
                        + "', '" + (religiousRequest.getInfectious())
                        + "')";
                break;
            case FOOD:
                FoodRequest foodRequest = (FoodRequest) request;
                query = "INSERT INTO FoodRequests VALUES " +
                        "('" + foodRequest.getRequestID()
                        + "', '" + foodRequest.getPatientName().replace("'", "''")
                        + "', '" + foodRequest.getArrivalTime()
                        + "', '" + foodRequest.getMealChoice().replace("'", "''")
                        + "')";
                break;
            case FLORAL:
                FloralRequest floralRequest = (FloralRequest) request;
                query = "INSERT INTO FloralRequests VALUES " +
                        "('" + floralRequest.getRequestID()
                        + "', '" + floralRequest.getPatientName().replace("'", "''")
                        + "', '" + floralRequest.getDeliveryDate()
                        + "', '" + floralRequest.getStartTime()
                        + "', '" + floralRequest.getEndTime()
                        + "', '" + floralRequest.getWantsRoses()
                        + "', '" + floralRequest.getWantsTulips()
                        + "', '" + floralRequest.getWantsDaisies()
                        + "', '" + floralRequest.getWantsLilies()
                        + "', '" + floralRequest.getWantsSunflowers()
                        + "', '" + floralRequest.getWantsCarnations()
                        + "', '" + floralRequest.getWantsOrchids()
                        + "')";
                break;
            case SECURITY:
                SecurityRequest securityRequest = (SecurityRequest) request;
                query = "INSERT INTO SecurityRequests VALUES " +
                        "('" + securityRequest.getRequestID()
                        + "', " + securityRequest.getUrgency()
                        + ")";
                break;
            case EXTERNAL_TRANSPORT:
                ExternalTransportRequest externalTransportRequest = (ExternalTransportRequest) request;
                query = "INSERT INTO ExternalTransportRequests VALUES " +
                        "('" + externalTransportRequest.getRequestID()
                        + "', '" + externalTransportRequest.getPatientName().replace("'", "''")
                        + "', '" + externalTransportRequest.getTransportType()
                        + "', '" + externalTransportRequest.getDestination().replace("'", "''")
                        + "', '" + externalTransportRequest.getPatientAllergies().replace("'", "''")
                        + "', '" + (externalTransportRequest.getOutNetwork())
                        + "', '" + (externalTransportRequest.getInfectious())
                        + "', '" + (externalTransportRequest.getUnconscious())
                        + "')";
                break;
            case LAUNDRY:
                LaundryRequest laundryRequest = (LaundryRequest) request;
                query = "INSERT INTO LaundryRequests VALUES " +
                        "('" + laundryRequest.getRequestID()
                        + "', '" + laundryRequest.getServiceType()
                        + "', '" + laundryRequest.getServiceSize()
                        + "', '" + (laundryRequest.getDark())
                        + "', '" + (laundryRequest.getLight())
                        + "', '" + (laundryRequest.getOccupied())
                        + "')";
                break;
            case CASE_MANAGER:
                CaseManagerRequest caseManagerRequest = (CaseManagerRequest) request;
                query = "INSERT INTO CaseManagerRequests VALUES " +
                        "('" + caseManagerRequest.getRequestID()
                        + "', '" + caseManagerRequest.getPatientName().replace("'", "''")
                        + "', '" + caseManagerRequest.getTimeForArrival()
                        + "')";
                break;
            case SOCIAL_WORKER:
                SocialWorkerRequest socialWorkerRequest = (SocialWorkerRequest) request;
                query = "INSERT INTO SocialWorkerRequests VALUES " +
                        "('" + socialWorkerRequest.getRequestID()
                        + "', '" + socialWorkerRequest.getPatientName().replace("'", "''")
                        + "', '" + socialWorkerRequest.getTimeForArrival()
                        + "')";
                break;
        }

        try {
            statement.execute(query);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes a request from Requests and the table specific to the given request
     *
     * @param requestID the request to remove, given by the request ID
     */
    public void removeEntity(String requestID) throws SQLException {
        Statement statement = DatabaseHandler.getDatabaseHandler("main.db").getStatement();
        String query = "SELECT * FROM Requests WHERE requestID = '" + requestID + "'";

        ResultSet rs = statement.executeQuery(query);
        Request request = null;
        while (rs.next()) {
            request = new Request(
                    rs.getString("requestID"),
                    Request.RequestType.valueOf(rs.getString("requestType")),
                    rs.getString("time"),
                    rs.getString("date"),
                    rs.getString("complete"),
                    rs.getString("employeeName"),
                    rs.getString("location"),
                    rs.getString("description"),
                    rs.getString("submitter")
            );
        }
        rs.close();

        String querySpecificTable = "DELETE FROM '" + Request.RequestType.prettify(request.getRequestType()).replace(" ", "") + "Requests" + "'WHERE requestID = '" + request.getRequestID() + "'";
        String queryGeneralTable = "DELETE FROM Requests WHERE requestID = '" + request.getRequestID() + "'";

        try {
            assert statement != null;
            statement.execute(querySpecificTable);
            statement.execute(queryGeneralTable);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the given request
     *
     * @param request the request to update
     */
    public void updateEntity(Request request) throws SQLException {
        Statement statement = DatabaseHandler.getDatabaseHandler("main.db").getStatement();

        String query = "UPDATE Requests SET requestType = '" + request.getRequestType()
                + "', requestDate = '" + request.getDate()
                + "', requestTime = '" + request.getTime()
                + "', complete = '" + request.getComplete()
                + "', employeeName = '" + request.getEmployeeName()
                + "', location = '" + request.getLocation().replace("'", "''")
                + "', description = '" + request.getDescription().replace("'", "''")
                + "' WHERE requestID = '" + request.getRequestID() + "'";

        try {
            assert statement != null;
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //If the given request is an instance of the less specific "Request" then dont try and update the specific tables
        if(request.getClass().equals(Request.class)){
            return;
        }

        switch (request.getRequestType()) {
            case SANITATION:
                SanitationRequest sanitationRequest = (SanitationRequest) request;
                query = "UPDATE SanitationRequests SET sanitationType = '" + sanitationRequest.getSanitationType()
                        + "', sanitationSize = '" + sanitationRequest.getSanitationSize()
                        + "', hazardous = '" + sanitationRequest.getHazardous()
                        + "', biologicalSubstance = '" + sanitationRequest.getBiologicalSubstance()
                        + "', occupied = '" + sanitationRequest.getOccupied()
                        + "' WHERE requestID = '" + sanitationRequest.getRequestID() + "'";
                break;
            case MEDICINE:
                MedicineRequest medicineRequest = (MedicineRequest) request;
                query = "UPDATE MedicineRequests SET patientName = '" + medicineRequest.getPatientName().replace("'", "''")
                        + "', medicine = '" + medicineRequest.getMedicine().replace("'", "''")
                        + "' WHERE requestID = '" + medicineRequest.getRequestID() + "'";
                break;
            case INTERNAL_TRANSPORT:
                InternalTransportRequest internalTransportRequest = (InternalTransportRequest) request;
                query = "UPDATE InternalTransportRequests SET patientName = '" + internalTransportRequest.getPatientName().replace("'", "''")
                        + "', transportType = '" + internalTransportRequest.getTransportType()
                        + "', unconscious = '" + internalTransportRequest.getUnconscious()
                        + "', infectious = '" + internalTransportRequest.getInfectious()
                        + "' WHERE requestID = '" + internalTransportRequest.getRequestID() + "'";
                break;
            case RELIGIOUS:
                ReligiousRequest religiousRequest = (ReligiousRequest) request;
                query = "UPDATE ReligiousRequests SET patientName = '" + religiousRequest.getPatientName().replace("'", "''")
                        + "', startTime = '" + religiousRequest.getStartTime()
                        + "', endTime = '" + religiousRequest.getEndTime()
                        + "', religiousDate = '" + religiousRequest.getReligiousDate()
                        + "', faith = '" + religiousRequest.getFaith().replace("'", "''")
                        + "', infectious = '" + religiousRequest.getInfectious()
                        + "' WHERE requestID = '" + religiousRequest.getRequestID() + "'";
                break;
            case FOOD:
                FoodRequest foodRequest = (FoodRequest) request;
                query = "UPDATE FoodRequests SET patientName = '" + foodRequest.getPatientName().replace("'", "''")
                        + "', arrivalTime = '" + foodRequest.getArrivalTime()
                        + "', mealChoice = '" + foodRequest.getMealChoice().replace("'", "''")
                        + "' WHERE requestID = '" + foodRequest.getRequestID() + "'";
                break;
            case FLORAL:
                FloralRequest floralRequest = (FloralRequest) request;
                query = "UPDATE FloralRequests SET patientName = '" + floralRequest.getPatientName().replace("'", "''")
                        + "', deliveryDate = '" + floralRequest.getDeliveryDate()
                        + "', startTime = '" + floralRequest.getStartTime()
                        + "', endTime = '" + floralRequest.getEndTime()
                        + "', wantsRoses = '" + floralRequest.getWantsRoses()
                        + "', wantsTulips = '" + floralRequest.getWantsTulips()
                        + "', wantsDaisies = '" + floralRequest.getWantsDaisies()
                        + "', wantsLilies = '" + floralRequest.getWantsLilies()
                        + "', wantsSunflowers = '" + floralRequest.getWantsSunflowers()
                        + "', wantsCarnations = '" + floralRequest.getWantsCarnations()
                        + "', wantsOrchids = '" + floralRequest.getWantsOrchids()
                        + "' WHERE requestID = '" + floralRequest.getRequestID() + "'";
                break;
            case SECURITY:
                SecurityRequest securityRequest = (SecurityRequest) request;
                query = "UPDATE SecurityRequests SET urgency = " + securityRequest.getUrgency()
                        + " WHERE requestID = '" + securityRequest.getRequestID() + "'";
                break;
            case EXTERNAL_TRANSPORT:
                ExternalTransportRequest externalTransportRequest = (ExternalTransportRequest) request;
                query = "UPDATE ExternalTransportRequests SET patientName = '" + externalTransportRequest.getPatientName().replace("'", "''")
                        + "', transportType = '" + externalTransportRequest.getTransportType()
                        + "', destination = '" + externalTransportRequest.getDestination().replace("'", "''")
                        + "', patientAllergies = '" + externalTransportRequest.getPatientAllergies().replace("'", "''")
                        + "', outNetwork = '" + (externalTransportRequest.getOutNetwork())
                        + "', infectious = '" + (externalTransportRequest.getInfectious())
                        + "', unconscious = '" + (externalTransportRequest.getUnconscious())
                        + "' WHERE requestID = '" + externalTransportRequest.getRequestID() + "'";
                break;
            case LAUNDRY:
                LaundryRequest laundryRequest = (LaundryRequest) request;
                query = "UPDATE LaundryRequests SET serviceType = '" + laundryRequest.getServiceType()
                        + "', serviceSize = '" + laundryRequest.getServiceSize()
                        + "', dark = '" + (laundryRequest.getDark())
                        + "', light = '" + (laundryRequest.getLight())
                        + "', occupied = '" + (laundryRequest.getOccupied())
                        + "' WHERE requestID = '" + laundryRequest.getRequestID() + "'";
                break;
            case CASE_MANAGER:
                CaseManagerRequest caseManagerRequest = (CaseManagerRequest) request;
                query = "UPDATE CaseManagerRequests SET patientName = '" + caseManagerRequest.getPatientName().replace("'", "''")
                        + "', timeForArrival = '" + caseManagerRequest.getTimeForArrival()
                        + "' WHERE requestID = '" + caseManagerRequest.getRequestID() + "'";
                break;
            case SOCIAL_WORKER:
                SocialWorkerRequest socialWorkerRequest = (SocialWorkerRequest) request;
                query = "UPDATE SocialWorkerRequests SET patientName = '" + socialWorkerRequest.getPatientName().replace("'", "''")
                        + "', timeForArrival = '" + socialWorkerRequest.getTimeForArrival()
                        + "' WHERE requestID = '" + socialWorkerRequest.getRequestID() + "'";
                break;
        }

        try {
            statement.execute(query);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Graph.getGraph().updateGraph();
    }
}
