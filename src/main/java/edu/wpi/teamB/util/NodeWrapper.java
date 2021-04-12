package edu.wpi.teamB.util;

import edu.wpi.teamB.entities.Node;
import javafx.scene.control.Label;
import lombok.Getter;

@Getter
public class NodeWrapper {

    private Label id;
    private Label name;
    private Label type;
    private Label edges;

    public NodeWrapper(Node n){
        this.id = new Label(n.getNodeID());
        this.name = new Label(n.getLongName());
        this.type = new Label(n.getNodeType());

    }
}
