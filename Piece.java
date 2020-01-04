package ChessAyeEye;

/*
This class is responsible for letting each piece who they are.

TODO(?):
    - Add some sort of 'TYPE' variable for each different type of piece (hashtable?)
    - maybe each piece knows how they can move
    - each piece has a different 'point' value for determining importance of
      each piece for the AI?
*/

enum Type{
  PAWN, KNIGHT, BISHOP, KING, QUEEN;
}


public class Piece {
    public String color;
    public String name;

    public void Piece() {

    }
}
