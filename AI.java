package ChessAyeEye;
import java.util.List;
//en.wikipedia.org/wiki/Minimax

public class AI extends Controller{
  Board currentBoard;
  List<Square> AILivingPiecesID;
  List<Square> PlayerLivingPiecesID;
  public AI(){
    takeTurn();
  }
  public void takeTurn(){
//testing the value check of a possible move
//grabbing the pawn from A7 and moving it 4 spaces for tests sake then checking the possible
//responses from the opponent
    int pawnPosition = moveAI(16);
    pawnPosition = moveAI(pawnPosition);
    pawnPosition = moveAI(pawnPosition);
//places the pawn in a spot where it can now be attacked, should result in a negative result from
//scanning for pieces that can remove our lowly pawn from the board
    damageReport();
  }

//gives the point evaluation of the board to hopefully minimize the possible damage the enemy could do
// to our army.
  private int damageReport(){

  }
}
