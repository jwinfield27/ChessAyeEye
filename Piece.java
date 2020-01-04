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
  
//check for first move state
int[] PAWN_MOVES = {16,32};
int[] PAWN_ATTACKS = {15,17};

int[] ROOK_MOVES = {
    1,2,3,4,5,6,7,              //left right
    16,32,48,64,80,96,112};     //up down


int[] KNIGHT_MOVES = {14,31,33,18};

int[] BISHOP_MOVES_WHITE = {
    17,34,51,68,85,102,119,     //top left to bottom right
    15,30,45,60,75,90};         //inner top right to upper bottom left

int[] BISHOP_MOVES_BLACK = {
    15,30,45,60,75,90,105,      //top right to botton left
    17,34,51,68,85,102};        //inner top left to upper bottom right

int[] QUEEN_MOVES = {
    1,2,3,4,5,6,7,              //left right
    16,32,48,64,80,96,112,      //up down
    15,30,45,60,75,90,105,      //black long diagonal
    17,34,51,68,85,102,119};    //white long diagonal
   
int[] KING_MOVES = {1,15,16,17};
int[] KING_CASTLE = {2};
}


public class Piece {
    public String color;
    public String name;

    public void Piece() {

    }
}
