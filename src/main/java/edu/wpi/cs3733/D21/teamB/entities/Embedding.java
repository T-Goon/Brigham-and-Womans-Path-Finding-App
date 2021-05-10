package edu.wpi.cs3733.D21.teamB.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class Embedding implements IStoredEntity {
    String username;
    ArrayList<Double> embedding;
}
