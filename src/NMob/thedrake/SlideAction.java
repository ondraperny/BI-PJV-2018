package NMob.thedrake;

import java.util.ArrayList;
import java.util.List;

public class SlideAction extends TroopAction {

    public SlideAction(int offsetX, int offsetY) {
        super(offsetX, offsetY);
    }

    public SlideAction(Offset2D offset) {
        super(offset);
    }

    @Override
    public List<Move> movesFrom(BoardPos origin, PlayingSide side, GameState state) {
        List<Move> result = new ArrayList<>();
        TilePos target = origin.stepByPlayingSide(offset(), side);

        if (target == TilePos.OFF_BOARD)
            return result;

        int i = target.i(), j = target.j();

        while(i < state.board().dimension() && i >= 0 && j < state.board().dimension() && j >= 0) {
            BoardPos nextTarget = new BoardPos(state.board().dimension(), i, j);
            if (state.canStep(origin, nextTarget)) {
                result.add(new StepOnly(origin, nextTarget));
            } else if (state.canCapture(origin, nextTarget)) {
                result.add(new StepAndCapture(origin, nextTarget));
            } else {
                return result;
            }

            i += target.i() - origin.i();
            j += target.j() - origin.j();
        }

        return result;
    }
}
