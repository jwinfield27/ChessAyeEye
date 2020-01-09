package ChessAyeEye;

/*
This class is responsible for letting each piece who they are.

TODO(?):
    - Add some sort of 'TYPE' variable for each different type of piece (hashtable?)
    - maybe each piece knows how they can move
    - each piece has a different 'point' value for determining importance of
      each piece for the AI?
*/

public class Piece {
    private int[] moves;
    enum Type{
      PAWN, ROOK, KNIGHT, BISHOP_WHITE, BISHOP_BLACK, KING, QUEEN, NONE;

    // '0' is to split moves and attacks for pawns

    private static int[] PAWN_MOVES = {16,32,0,15,17};

    private static int[] ROOK_MOVES = {
        1,2,3,4,5,6,7,              //left right
        16,32,48,64,80,96,112};     //up down


    private static int[] KNIGHT_MOVES = {14,31,33,18};

    private static int[] BISHOP_WHITE_MOVES = {
        17,34,51,68,85,102,119,     //top left to bottom right
        15,30,45,60,75,90};         //inner top right to upper bottom left

    private static int[] BISHOP_BLACK_MOVES = {
        15,30,45,60,75,90,105,      //top right to botton left
        17,34,51,68,85,102};        //inner top left to upper bottom right

    private static int[] QUEEN_MOVES = {
        1,2,3,4,5,6,7,              //left right
        16,32,48,64,80,96,112,      //up down
        15,30,45,60,75,90,105,      //black long diagonal
        17,34,51,68,85,102,119};    //white long diagonal

    //the move 2 case is a special case for something i dont understand
    private static int[] KING_MOVES = {1,2,15,16,17};
    }

    private int[] getMoveArray(Type type){
      switch(type){
        case PAWN:
          return Type.PAWN_MOVES;
        case ROOK:
          return Type.ROOK_MOVES;
        case KNIGHT:
          return Type.KNIGHT_MOVES;
        case BISHOP_WHITE:
          return Type.BISHOP_WHITE_MOVES;
        case BISHOP_BLACK:
          return Type.BISHOP_BLACK_MOVES;
        case KING:
          return Type.KING_MOVES;
        case QUEEN:
          return Type.QUEEN_MOVES;
      }
      int[] dumbArray = {0};
      return dumbArray;
    }
    public int[] getMoves(){
      return moves;
    }


    public String color = "W";
    public String name = "NONE";
    Type type;

    public Piece(String piece, String color) {
        this.color = color;
        this.name = piece;
        this.type = Type.valueOf(piece);
        this.moves = getMoveArray(this.type);

    }

    public Piece(String name, String type, String color) {
        this.color = color;
        this.name = name;
        this.type = Type.valueOf(type);
        this.moves = getMoveArray(this.type);
    }

    public Piece() {
        this.color = "NONE";
        this.name = "NONE";
        this.type = Type.valueOf("NONE");
        this.moves = getMoveArray(this.type);
    }


}
