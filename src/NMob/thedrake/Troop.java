package NMob.thedrake;

import java.util.List;

public class Troop {
    private final String name;
    private final Offset2D aversPivot;
    private final Offset2D reversePivot;
    private final List<TroopAction> aversActions;
    private final List<TroopAction> reversActions;

    public Troop(String name,
                 Offset2D aversPivot,
                 Offset2D reversePivot,
                 List<TroopAction> aversActions,
                 List<TroopAction> reversActions) {
        this.name = name;
        this.aversPivot = aversPivot;
        this.reversePivot = reversePivot;
        this.aversActions = aversActions;
        this.reversActions = reversActions;
    }

    public Troop(String name, Offset2D pivot, List<TroopAction> aversActions, List<TroopAction> reversActions) {
        this.name = name;
        this.aversPivot = pivot;
        this.reversePivot = pivot;
        this.aversActions = aversActions;
        this.reversActions = reversActions;
    }

    public Troop(String name, List<TroopAction> aversActions, List<TroopAction> reversActions) {
        this.name = name;
        this.aversPivot = new Offset2D(1, 1);
        this.reversePivot = new Offset2D(1, 1);
        this.aversActions = aversActions;
        this.reversActions = reversActions;
    }

    public String name() {
        return name;
    }

    public Offset2D pivot(TroopFace face) {
        return face == TroopFace.AVERS ? aversPivot : reversePivot;
    }

    //Vrací seznam akcí pro zadanou stranu jednotky
    public List<TroopAction> actions(TroopFace face) {
        return face.equals(TroopFace.AVERS) ? this.aversActions : this.reversActions;
    }
}
