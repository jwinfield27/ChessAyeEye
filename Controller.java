package ChessAyeEye;

public class Controller {
  Board curBoard;
// maybe give it a board to look at?? as input on creation?
// otherwise add a new class for AI or for controlling the input from AI and User
  public Controller(Board board){
    newController(board);
  }


  private void newController(Board board){
      curBoard = board;
  }
}
