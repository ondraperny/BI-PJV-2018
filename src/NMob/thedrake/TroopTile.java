package NMob.thedrake;

import java.util.ArrayList;
import java.util.List;

public class TroopTile implements Tile {
    private Troop troop;
    private PlayingSide playingSide;
    private TroopFace troopFace;

    public TroopTile(Troop troop, PlayingSide side, TroopFace troopFace) {
        this.troop = troop;
        this.playingSide = side;
        this.troopFace = troopFace;
    }

    // Vrací barvu, za kterou hraje jednotka na této dlaždici
    public PlayingSide side() {
        return this.playingSide;
    }

    // Vrací stranu, na kterou je jednotka otočena
    public TroopFace face() {
        return this.troopFace;
    }

    // Jednotka, která stojí na této dlaždici
    public Troop troop() {
        return this.troop;
    }

    // Vrací False, protože na dlaždici s jednotkou se nedá vstoupit
    @Override /// s timhle override si nejsem jisty
    public boolean canStepOn() {
        return false;
    }

    // Vrací True
    @Override /// s timhle override si nejsem jisty
    public boolean hasTroop() {
        return true;
    }

    @Override
    public List<Move> movesFrom(BoardPos pos, GameState state) {
        List<Move> result = new ArrayList<>();
        for(TroopAction action : this.troop.actions(this.troopFace)) {
            result.addAll(action.movesFrom(pos, state.sideOnTurn(), state));
        }


        return result;
    }

    // Vytvoří novou dlaždici, s jednotkou otočenou na opačnou stranu
    // (z rubu na líc nebo z líce na rub)
    public TroopTile flipped() {
        return new TroopTile(this.troop,
                this.playingSide,
                this.troopFace == TroopFace.AVERS ? TroopFace.REVERS : TroopFace.AVERS);
    }
}
