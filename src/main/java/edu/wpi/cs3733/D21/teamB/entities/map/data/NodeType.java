package edu.wpi.cs3733.D21.teamB.entities.map.data;

import edu.wpi.cs3733.D21.teamB.entities.requests.Request;

public enum NodeType {
    SERV,
    REST,
    LABS,
    ELEV,
    DEPT,
    CONF,
    INFO,
    RETL,
    BATH,
    EXIT,
    STAI,
    PARK;

    public static NodeType deprettify(String nodeType) {
        switch (nodeType) {
            case "Services":
                return SERV;
            case "Restrooms":
                return REST;
            case "Lab Rooms":
                return LABS;
            case "Elevators":
                return ELEV;
            case "Departments":
                return DEPT;
            case "Conference Rooms":
                return CONF;
            case "Information Locations":
                return INFO;
            case "Retail Locations":
                return RETL;
            case "Bathroom":
                return BATH;
            case "Entrances":
                return EXIT;
            case "Stairs":
                return STAI;
            case "Parking Spots":
                return PARK;
            default:
                throw new IllegalStateException("How did we get here?");
        }
    }
}
