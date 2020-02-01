package ChessAyeEye;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
//en.wikipedia.org/wiki/Minimax

public class AI extends Player {
    Board currentBoard;
    List<Square> AILivingPiecesID;
    List<Square> PlayerLivingPiecesID;
    public AI(String color) {
        super(color);
        takeTurn();
    }
    public void takeTurn(){
        //this will be controller.pieceList___.length
        int randPiece = ThreadLocalRandom.current().nextInt(0, 1);
  //testing the value check of a possible move
  //grabbing the pawn from A7 and moving it 4 spaces for tests sake then checking the possible
  //responses from the opponent
//      int pawnPosition = moveAI(16);
//      pawnPosition = moveAI(pawnPosition);
//      pawnPosition = moveAI(pawnPosition);
  //places the pawn in a spot where it can now be attacked, should result in a negative result from
  //scanning for pieces that can remove our lowly pawn from the board
        damageReport();
    }

  //gives the point evaluation of the board to hopefully minimize the possible damage the enemy could do
  // to our army.
    private int damageReport(){
        return 0;
    }
}
