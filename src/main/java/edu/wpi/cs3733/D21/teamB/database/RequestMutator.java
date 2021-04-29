package edu.wpi.cs3733.D21.teamB.database;

import edu.wpi.cs3733.D21.teamB.entities.requests.*;
import edu.wpi.cs3733.D21.teamB.entities.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class RequestMutator implements IDatabaseEntityMutator<Request> {

    private final DatabaseHandler db;

    public RequestMutator(DatabaseHandler db) {
        this.db = db;
    }

    /**
     * Displays the list of requests along with their attributes.
     *
     * @return a map of request IDs to actual requests
     */
    public Map<String, Request> getRequests() throws SQLException {
        String query = "SELECT * FROM Requests";
        ResultSet rs = db.runStatement(query, true);
        Map<String, Request> requests = new HashMap<>();
        if (rs == null) return requests;
        do {
            Request outRequest = new Request(
                    rs.getString("requestID"),
                    Request.RequestType.valueOf(rs.getString("requestType")),
                    rs.getString("requestTime"),
                    rs.getString("requestDate"),
                    rs.getString("complete"),
                    rs.getString("employeeName"),
                    rs.getString("location"),
                    rs.getString("description"),
                    rs.getString("submitter")
            );
            requests.put(rs.getString("requestID"), outRequest);
        } while (rs.next());
        rs.close();
        return requests;
    }

    /**
     * Adds a request to Requests and the table specific to the given request
     *
     * @param request the request to add
     */
    public void addEntity(Request request) throws SQLException {
        User user = db.getAuthenticationUser();
        String username = user.getUsername();
        if (username == null) username = "null";

        String query = "INSERT INTO Requests VALUES " +
                "('" + request.getRequestID()
                + "', '" + request.getRequestType()
                + "', '" + request.getDate()
                + "', '" + request.getTime()
                + "', '" + request.getProgress()
                + "', '" + request.getEmployeeName()
                + "', '" + request.getLocation()
                + "', '" + request.getDescription().replace("'", "''")
                + "', '" + username
                + "')";
        db.runStatement(query, false);

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

            case LANGUAGE:
                LanguageRequest languageRequest = (LanguageRequest) request;
                query = "INSERT INTO LanguageInterpretationRequests VALUES " +
                        "('" + languageRequest.getRequestID()
                        + "', '" + languageRequest.getPatientName().replace("'", "''")
                        + "', '" + languageRequest.getLanguage()
                        + "', '" + languageRequest.getTimeForArrival()
                        + "')";
                break;
            case GIFT:
                GiftRequest giftRequest = (GiftRequest) request;
                query = "INSERT INTO GiftRequests VALUES " +
                        "('" + giftRequest.getRequestID()
                        + "', '" + giftRequest.getPatientName().replace("'", "''")
                        + "', '" + giftRequest.getDeliveryDate()
                        + "', '" + giftRequest.getStartTime()
                        + "', '" + giftRequest.getEndTime()
                        + "', '" + giftRequest.getWantsBalloons()
                        + "', '" + giftRequest.getWantsTeddyBear()
                        + "', '" + giftRequest.getWantsChocolate()
                        + "')";
                break;
            case EMERGENCY:
                EmergencyRequest emergencyRequest = (EmergencyRequest) request;
                query = "INSERT INTO EmergencyRequests VALUES " +
                        "('" + emergencyRequest.getRequestID()
                        + "', '" + emergencyRequest.getMedicalEmergency()
                        + "', '" + emergencyRequest.getSecurityEmergency()
                        + "')";
                break;
            case COVID:
                CovidSurveyRequest covidSurveyRequest = (CovidSurveyRequest) request;
                query = "INSERT INTO covidSurveyRequests VALUES " +
                        "('" + covidSurveyRequest.getRequestID()
                        + "', '" + covidSurveyRequest.getStatus().toString()
                        + "', '" + covidSurveyRequest.getSymptomFever()
                        + "', '" + covidSurveyRequest.getSymptomChills()
                        + "', '" + covidSurveyRequest.getSymptomCough()
                        + "', '" + covidSurveyRequest.getSymptomShortBreath()
                        + "', '" + covidSurveyRequest.getSymptomSoreTht()
                        + "', '" + covidSurveyRequest.getSymptomHeadache()
                        + "', '" + covidSurveyRequest.getSymptomAches()
                        + "', '" + covidSurveyRequest.getSymptomNose()
                        + "', '" + covidSurveyRequest.getSymptomLostTaste()
                        + "', '" + covidSurveyRequest.getSymptomNausea()
                        + "', '" + covidSurveyRequest.getHadCloseContact()
                        + "', '" + covidSurveyRequest.getHadPositiveTest()
                        + "')";
                break;
        }
        db.runStatement(query, false);
    }

    /**
     * Updates the given request
     *
     * @param request the request to update
     */
    public void updateEntity(Request request) throws SQLException {
        String query = "UPDATE Requests SET requestType = '" + request.getRequestType()
                + "', requestDate = '" + request.getDate()
                + "', requestTime = '" + request.getTime()
                + "', complete = '" + request.getProgress()
                + "', employeeName = '" + request.getEmployeeName()
                + "', location = '" + request.getLocation().replace("'", "''")
                + "', description = '" + request.getDescription().replace("'", "''")
                + "' WHERE requestID = '" + request.getRequestID() + "'";
        db.runStatement(query, false);

        //If the given request is an instance of the less specific "Request" then dont try and update the specific tables
        if (request.getClass().equals(Request.class)) return;
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

            case LANGUAGE:
                LanguageRequest languageRequest = (LanguageRequest) request;
                query = "UPDATE LanguageInterpretationRequests SET patientName = '" + languageRequest.getPatientName().replace("'", "''")
                        + "', language = '" + languageRequest.getLanguage()
                        + "', arrivalTime = '" + languageRequest.getTimeForArrival()
                        + "' WHERE requestID = '" + languageRequest.getRequestID() + "'";
                break;
            case GIFT:
                GiftRequest giftRequest = (GiftRequest) request;
                query = "UPDATE GiftRequests SET patientName = '" + giftRequest.getPatientName().replace("'", "''")
                        + "', deliveryDate = '" + giftRequest.getDeliveryDate()
                        + "', startTime = '" + giftRequest.getStartTime()
                        + "', endTime = '" + giftRequest.getEndTime()
                        + "', wantsBalloons = '" + giftRequest.getWantsBalloons()
                        + "', wantsTeddyBear = '" + giftRequest.getWantsTeddyBear()
                        + "', wantsChocolate = '" + giftRequest.getWantsChocolate()
                        + "' WHERE requestID = '" + giftRequest.getRequestID() + "'";
                break;
            case EMERGENCY:
                EmergencyRequest emergencyRequest = (EmergencyRequest) request;
                query = "UPDATE EmergencyRequests SET medicalEmergency = '" + emergencyRequest.getMedicalEmergency()
                        + "', securityEmergency = '" + emergencyRequest.getSecurityEmergency()
                        + "' WHERE requestID = '" + emergencyRequest.getRequestID() + "'";
                break;
            case COVID:
                CovidSurveyRequest covidSurveyRequest = (CovidSurveyRequest) request;
                query = "UPDATE covidSurveyRequests SET status = '" + covidSurveyRequest.getStatus().toString()
                        + "', fever = '" + covidSurveyRequest.getSymptomFever()
                        + "', chills = '" + covidSurveyRequest.getSymptomChills()
                        + "', cough = '" + covidSurveyRequest.getSymptomCough()
                        + "', shortBreath = '" + covidSurveyRequest.getSymptomShortBreath()
                        + "', soreTht = '" + covidSurveyRequest.getSymptomSoreTht()
                        + "', headache = '" + covidSurveyRequest.getSymptomHeadache()
                        + "', aches = '" + covidSurveyRequest.getSymptomAches()
                        + "', nose = '" + covidSurveyRequest.getSymptomNose()
                        + "', lostTaste = '" + covidSurveyRequest.getSymptomLostTaste()
                        + "', nausea = '" + covidSurveyRequest.getSymptomNausea()
                        + "', closeContact = '" + covidSurveyRequest.getHadCloseContact()
                        + "', positiveTest = '" + covidSurveyRequest.getHadPositiveTest()
                        + "' WHERE requestID = '" + covidSurveyRequest.getRequestID() + "'";
                break;
        }
        db.runStatement(query, false);
    }

    /**
     * Removes a request from Requests and the table specific to the given request
     *
     * @param requestID the request to remove, given by the request ID
     */
    public void removeEntity(String requestID) throws SQLException {
        String query = "SELECT * FROM Requests WHERE requestID = '" + requestID + "'";

        ResultSet rs = db.runStatement(query, true);
        Request request;
        do {
            request = new Request(
                    rs.getString("requestID"),
                    Request.RequestType.valueOf(rs.getString("requestType")),
                    rs.getString("requestTime"),
                    rs.getString("requestDate"),
                    rs.getString("complete"),
                    rs.getString("employeeName"),
                    rs.getString("location"),
                    rs.getString("description"),
                    rs.getString("submitter")
            );
        } while (rs.next());
        rs.close();

        String querySpecificTable = "DELETE FROM '" + Request.RequestType.prettify(request.getRequestType()).replace(" ", "") + "Requests" + "'WHERE requestID = '" + request.getRequestID() + "'";
        String queryGeneralTable = "DELETE FROM Requests WHERE requestID = '" + request.getRequestID() + "'";
        db.runStatement(querySpecificTable, false);
        db.runStatement(queryGeneralTable, false);
    }

    /**
     * Get specific request information from the database given the request's ID and type
     *
     * @param requestID   the ID of the request
     * @param requestType the type of the request
     * @return the request
     */
    public Request getSpecificRequestById(String requestID, Request.RequestType requestType) throws SQLException {
        String tableName = Request.RequestType.prettify(requestType).replaceAll("\\s", "") + "Requests";
        String query = "SELECT * FROM Requests LEFT JOIN " + tableName + " ON Requests.requestID = " + tableName + ".requestID WHERE Requests.requestID = '" + requestID + "'";
        Request outRequest;
        ResultSet rs = db.runStatement(query, true);
        if (rs == null) return null;
        switch (requestType) {
            case SANITATION:
                outRequest = new SanitationRequest(
                        rs.getString("sanitationType"),
                        rs.getString("sanitationSize"),
                        rs.getString("hazardous"),
                        rs.getString("biologicalSubstance"),
                        rs.getString("occupied"),
                        rs.getString("requestID"),
                        rs.getString("requestTime"),
                        rs.getString("requestDate"),
                        rs.getString("complete"),
                        rs.getString("employeeName"),
                        rs.getString("location"),
                        rs.getString("description")
                );
                break;
            case MEDICINE:
                outRequest = new MedicineRequest(
                        rs.getString("patientName"),
                        rs.getString("medicine"),
                        rs.getString("requestID"),
                        rs.getString("requestTime"),
                        rs.getString("requestDate"),
                        rs.getString("complete"),
                        rs.getString("employeeName"),
                        rs.getString("location"),
                        rs.getString("description")
                );
                break;
            case INTERNAL_TRANSPORT:
                outRequest = new InternalTransportRequest(
                        rs.getString("patientName"),
                        rs.getString("transportType"),
                        rs.getString("unconscious"),
                        rs.getString("infectious"),
                        rs.getString("requestID"),
                        rs.getString("requestTime"),
                        rs.getString("requestDate"),
                        rs.getString("complete"),
                        rs.getString("employeeName"),
                        rs.getString("location"),
                        rs.getString("description")
                );
                break;
            case RELIGIOUS:
                outRequest = new ReligiousRequest(
                        rs.getString("patientName"),
                        rs.getString("startTime"),
                        rs.getString("endTime"),
                        rs.getString("religiousDate"),
                        rs.getString("faith"),
                        rs.getString("infectious"),
                        rs.getString("requestID"),
                        rs.getString("requestTime"),
                        rs.getString("requestDate"),
                        rs.getString("complete"),
                        rs.getString("employeeName"),
                        rs.getString("location"),
                        rs.getString("description")
                );
                break;
            case FOOD:
                outRequest = new FoodRequest(
                        rs.getString("patientName"),
                        rs.getString("arrivalTime"),
                        rs.getString("mealChoice"),
                        rs.getString("requestID"),
                        rs.getString("requestTime"),
                        rs.getString("requestDate"),
                        rs.getString("complete"),
                        rs.getString("employeeName"),
                        rs.getString("location"),
                        rs.getString("description")
                );
                break;
            case FLORAL:
                outRequest = new FloralRequest(
                        rs.getString("patientName"),
                        rs.getString("deliveryDate"),
                        rs.getString("startTime"),
                        rs.getString("endTime"),
                        rs.getString("wantsRoses"),
                        rs.getString("wantsTulips"),
                        rs.getString("wantsDaisies"),
                        rs.getString("wantsLilies"),
                        rs.getString("wantsSunflowers"),
                        rs.getString("wantsCarnations"),
                        rs.getString("wantsOrchids"),
                        rs.getString("requestID"),
                        rs.getString("requestTime"),
                        rs.getString("requestDate"),
                        rs.getString("complete"),
                        rs.getString("employeeName"),
                        rs.getString("location"),
                        rs.getString("description")
                );
                break;
            case SECURITY:
                outRequest = new SecurityRequest(
                        rs.getInt("urgency"),
                        rs.getString("requestID"),
                        rs.getString("requestTime"),
                        rs.getString("requestDate"),
                        rs.getString("complete"),
                        rs.getString("employeeName"),
                        rs.getString("location"),
                        rs.getString("description")
                );
                break;
            case EXTERNAL_TRANSPORT:
                outRequest = new ExternalTransportRequest(
                        rs.getString("patientName"),
                        rs.getString("transportType"),
                        rs.getString("destination"),
                        rs.getString("patientAllergies"),
                        rs.getString("outNetwork"),
                        rs.getString("infectious"),
                        rs.getString("unconscious"),
                        rs.getString("requestID"),
                        rs.getString("requestTime"),
                        rs.getString("requestDate"),
                        rs.getString("complete"),
                        rs.getString("employeeName"),
                        rs.getString("location"),
                        rs.getString("description")
                );
                break;
            case LAUNDRY:
                outRequest = new LaundryRequest(
                        rs.getString("serviceType"),
                        rs.getString("serviceSize"),
                        rs.getString("dark"),
                        rs.getString("light"),
                        rs.getString("occupied"),
                        rs.getString("requestID"),
                        rs.getString("requestTime"),
                        rs.getString("requestDate"),
                        rs.getString("complete"),
                        rs.getString("employeeName"),
                        rs.getString("location"),
                        rs.getString("description")
                );
                break;
            case CASE_MANAGER:
                outRequest = new CaseManagerRequest(
                        rs.getString("patientName"),
                        rs.getString("timeForArrival"),
                        rs.getString("requestID"),
                        rs.getString("requestTime"),
                        rs.getString("requestDate"),
                        rs.getString("complete"),
                        rs.getString("employeeName"),
                        rs.getString("location"),
                        rs.getString("description")
                );
                break;

            case SOCIAL_WORKER:
                outRequest = new SocialWorkerRequest(
                        rs.getString("patientName"),
                        rs.getString("timeForArrival"),
                        rs.getString("requestID"),
                        rs.getString("requestTime"),
                        rs.getString("requestDate"),
                        rs.getString("complete"),
                        rs.getString("employeeName"),
                        rs.getString("location"),
                        rs.getString("description")
                );
                break;

            case LANGUAGE:
                outRequest = new LanguageRequest(
                        rs.getString("language"),
                        rs.getString("patientName"),
                        rs.getString("arrivalTime"),
                        rs.getString("requestID"),
                        rs.getString("requestTime"),
                        rs.getString("requestDate"),
                        rs.getString("complete"),
                        rs.getString("employeeName"),
                        rs.getString("location"),
                        rs.getString("description")
                );
                break;
            case GIFT:
                outRequest = new GiftRequest(
                        rs.getString("patientName"),
                        rs.getString("deliveryDate"),
                        rs.getString("startTime"),
                        rs.getString("endTime"),
                        rs.getString("wantsBalloons"),
                        rs.getString("wantsTeddyBear"),
                        rs.getString("wantsChocolate"),
                        rs.getString("requestID"),
                        rs.getString("requestTime"),
                        rs.getString("requestDate"),
                        rs.getString("complete"),
                        rs.getString("employeeName"),
                        rs.getString("location"),
                        rs.getString("description")
                );
                break;
            case EMERGENCY:
                outRequest = new EmergencyRequest(
                        rs.getString("medicalEmergency"),
                        rs.getString("securityEmergency"),
                        rs.getString("requestID"),
                        rs.getString("requestTime"),
                        rs.getString("requestDate"),
                        rs.getString("complete"),
                        rs.getString("employeeName"),
                        rs.getString("location"),
                        rs.getString("description")
                );
                break;
            case COVID:
                outRequest = new CovidSurveyRequest(
                        rs.getString("requestID"),
                        rs.getString("requestTime"),
                        rs.getString("requestDate"),
                        rs.getString("complete"),
                        rs.getString("employeeName"),
                        rs.getString("location"),
                        rs.getString("description"),
                        rs.getString("submitter"),
                        User.CovidStatus.valueOf(rs.getString("status")),
                        rs.getString("fever"),
                        rs.getString("chills"),
                        rs.getString("cough"),
                        rs.getString("shortBreath"),
                        rs.getString("soreTht"),
                        rs.getString("headache"),
                        rs.getString("aches"),
                        rs.getString("nose"),
                        rs.getString("lostTaste"),
                        rs.getString("nausea"),
                        rs.getString("closeContact"),
                        rs.getString("positiveTest")
                );
                break;
            default:
                throw new IllegalStateException("How did we get here???????????");
        }
        rs.close();
        return outRequest;
    }
}
