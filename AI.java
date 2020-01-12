package ChessAyeEye;

//en.wikipedia.org/wiki/Minimax

public class AI extends Controller{
  Board currentBoard;
  public AI(){
    //remove this after moderator method or class is made
    TakeTurn();
  }
  public Board TakeTurn(){
    currentBoard = GAMEBOARD;
    newBoard = lookAhead(currentBoard);
    return newBoard;
  }


  public Board lookAhead(Board node){

  }
}
