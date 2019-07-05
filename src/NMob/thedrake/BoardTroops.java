package NMob.thedrake;

import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class BoardTroops implements JSONSerializable {

    private final PlayingSide playingSide;
    private final Map<BoardPos, TroopTile> troopMap;
    private final TilePos leaderPosition;
    private final int guards;

    public BoardTroops(PlayingSide playingSide) {
        this.playingSide = playingSide;
        this.troopMap = Collections.emptyMap();
        this.leaderPosition = TilePos.OFF_BOARD;
        this.guards = 0;
    }

    public BoardTroops(
            PlayingSide playingSide,
            Map<BoardPos, TroopTile> troopMap,
            TilePos leaderPosition,
            int guards) {
        this.playingSide = playingSide;
        this.troopMap = troopMap;
        this.leaderPosition = leaderPosition;
        this.guards = guards;
    }

    // touhle funkci si nejsem uplně jistý
    public Optional<TroopTile> at(TilePos pos) {
        if(troopMap.containsKey(pos)) {
            return Optional.of(troopMap.get(pos));
        } else {
            return Optional.empty();
        }
    }

    public PlayingSide playingSide() {
        return playingSide;
    }

    public TilePos leaderPosition() {
        return isLeaderPlaced() ? this.leaderPosition : TilePos.OFF_BOARD;
    }

    public int guards() {
        return this.guards;
    }

    public boolean isLeaderPlaced() {
        return leaderPosition != TilePos.OFF_BOARD;
    }

    public boolean isPlacingGuards() {
        return isLeaderPlaced() && (guards < 2);
    }

    public Set<BoardPos> troopPositions() {
        return this.troopMap.keySet();
    }

    public BoardTroops placeTroop(Troop troop, BoardPos target) {
        if (at(target).isPresent()) {
            throw new IllegalArgumentException();
        }

        TroopTile troopTile = new TroopTile(troop, this.playingSide, TroopFace.AVERS);
        Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
        newTroops.put(target, troopTile);

        if (!isLeaderPlaced()) {
            return new BoardTroops(playingSide(), newTroops, target, guards);
        } else if (isLeaderPlaced() && isPlacingGuards()) {
            return new BoardTroops(playingSide(), newTroops, leaderPosition, guards+1);
        }

        return new BoardTroops(playingSide(), newTroops, leaderPosition, guards);
    }

    public BoardTroops troopStep(BoardPos origin, BoardPos target) {
        if (!isLeaderPlaced()) {
            throw new IllegalStateException("Cannot move troops before the leader is placed.");
        }

        if (isPlacingGuards()) {
            throw new IllegalStateException("Cannot move troops before guards are placed.");
        }

        if (!at(origin).isPresent() || at(target).isPresent()) {
            throw new IllegalArgumentException();
        }

        Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
        TroopTile tile = newTroops.remove(origin);
        newTroops.put(target, tile.flipped());

        if (leaderPosition.equals(origin)) {
            return new BoardTroops(playingSide(), newTroops, target, guards);
        }

        return new BoardTroops(playingSide(), newTroops, leaderPosition, guards);
    }

    public BoardTroops troopFlip(BoardPos origin) {
        if (!isLeaderPlaced()) {
            throw new IllegalStateException(
                    "Cannot move troops before the leader is placed.");
        }

        if (isPlacingGuards()) {
            throw new IllegalStateException(
                    "Cannot move troops before guards are placed.");
        }

        if (!at(origin).isPresent()) {
            throw new IllegalArgumentException();
        }

        Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
        TroopTile tile = newTroops.remove(origin);
        newTroops.put(origin, tile.flipped());

        return new BoardTroops(playingSide(), newTroops, leaderPosition, guards);
    }

    public BoardTroops removeTroop(BoardPos target) {
        if (!isLeaderPlaced()) {
            throw new IllegalStateException("Cannot move troops before the leader is placed.");
        }

        if (isPlacingGuards()) {
            throw new IllegalStateException("Cannot move troops before guards are placed.");
        }

        if (!at(target).isPresent()) {
            throw new IllegalArgumentException();
        }


        Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
        newTroops.remove(target);

        if (leaderPosition.equals(target)) {
            return new BoardTroops(playingSide(), newTroops, TilePos.OFF_BOARD, guards);
        }

        return new BoardTroops(playingSide(), newTroops, leaderPosition, guards);
    }

    @Override
    public void toJSON(PrintWriter writer) {
        writer.print("\"leaderPosition\":\"" + this.leaderPosition() + "\",\"guards\":" + this.guards() +
                ",\"troopMap\":{" + this.getJSONTroopMap() + "}");
    }

    private String getJSONTroopMap() {
        StringBuilder sb = new StringBuilder();
        boolean b = true;
        
        // sort map 
        SortedMap<BoardPos, TroopTile> tmpMap = new TreeMap<BoardPos, TroopTile>(new Comparator<BoardPos>() {
            public int compare(BoardPos o1, BoardPos o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });

        for(BoardPos boardPos : this.troopPositions()) {
            tmpMap.put(boardPos, troopMap.get(boardPos));
        }

        for (BoardPos boardPos : tmpMap.keySet()) {
            if (!b) {
                sb.append(",");
            }
            sb.append(MessageFormat.format("\"{2}\":{0}\"troop\":\"{3}\",\"side\":\"{4}\",\"face\":\"{5}\"{1}",
                    '{', '}', boardPos.toString(), this.at(boardPos).get().troop().name(),
                    this.at(boardPos).get().side(), this.at(boardPos).get().face()));
            b = false;
        }

        
        return sb.toString();
    }
}
