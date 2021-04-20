package edu.wpi.teamB.entities.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Request {

    public enum RequestType {
        CASE_MANAGER,
        EXTERNAL_TRANSPORT,
        FLORAL,
        FOOD,
        INTERNAL_TRANSPORT,
        LAUNDRY,
        MEDICINE,
        RELIGIOUS,
        SANITATION,
        SECURITY,
        SOCIAL_WORKER;

        public static String prettify(RequestType requestType) {
            switch (requestType) {
                case CASE_MANAGER:
                    return "Case Manager";
                case EXTERNAL_TRANSPORT:
                    return "External Transport";
                case FLORAL:
                    return "Floral";
                case FOOD:
                    return "Food";
                case INTERNAL_TRANSPORT:
                    return "Internal Transport";
                case LAUNDRY:
                    return "Laundry";
                case MEDICINE:
                    return "Medicine";
                case RELIGIOUS:
                    return "Religious";
                case SANITATION:
                    return "Sanitation";
                case SECURITY:
                    return "Security";
                case SOCIAL_WORKER:
                    return "Social Worker";
                default:
                    throw new IllegalStateException("How did we get here?");
            }
        }

        public static RequestType deprettify(String requestType) {
            switch (requestType) {
                case "CASE_MANAGER":
                    return CASE_MANAGER;
                case "EXTERNAL_":
                    return EXTERNAL_TRANSPORT;
                case "Floral":
                    return FLORAL;
                case "Food":
                    return FOOD;
                case "Internal Transport":
                    return INTERNAL_TRANSPORT;
                case "Laundry":
                    return LAUNDRY;
                case "Medicine":
                    return MEDICINE;
                case "Religious":
                    return  RELIGIOUS;
                case "Sanitation":
                    return SANITATION;
                case "Security":
                    return SECURITY;
                case "Social Worker":
                    return SOCIAL_WORKER;
                default:
                    throw new IllegalStateException("How did we get here?");
            }
        }
    }

    private String requestID;

    private RequestType requestType;

    private String time;

    private String date;

    private String complete;

    private String employeeName;

    private String location;

    private String description;
}
