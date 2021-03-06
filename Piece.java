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
    enum Type{
        PAWN, ROOK, KNIGHT, BISHOP, KING, QUEEN, NONE;

    // '0' is to split moves and attacks for pawns
    //added '0' to every piece's moves so we can write generic code

    private final static int[] PAWN_MOVES = {16};
    private final static int[] PAWN_ATTACKS = {15,17};

    private final static int[] ROOK_MOVES = {1,16};

    private final static int[] KNIGHT_MOVES = {14,31,33,18};

    private final static int[] BISHOP_MOVES = {17,15};        

    private final static int[] QUEEN_MOVES = {1,16,15,17};    //white long diagonal

    //the move 2,3 cases are a special case for castling
    //maybe moves castling moves to a different array?
    private final static int[] KING_MOVES = {1,15,16,17};
    private final static int[] KING_CASTLE = {2,-3};

    }
//    private final static int[] PAWN_MOVES = {16,32,0,15,17};
//
//    private final static int[] ROOK_MOVES = {
//        1,2,3,4,5,6,7,              //left right
//        16,32,48,64,80,96,112};     //up down
//
//    private final static int[] KNIGHT_MOVES = {14,31,33,18};
//
//    private final static int[] BISHOP_WHITE_MOVES = {
//        17,34,51,68,85,102,119,     //top left to bottom right
//        15,30,45,60,75,90};         //inner top right to upper bottom left
//
//    private final static int[] BISHOP_BLACK_MOVES = {
//        15,30,45,60,75,90,105,      //top right to botton left
//        17,34,51,68,85,102};        //inner top left to upper bottom right
//
//    private final static int[] QUEEN_MOVES = {
//        1,2,3,4,5,6,7,              //left right
//        16,32,48,64,80,96,112,      //up down
//        15,30,45,60,75,90,105,      //black long diagonal
//        17,34,51,68,85,102,119};    //white long diagonal
//
//    //the move 2 case is a special case for castling
//    private final static int[] KING_MOVES = {1,15,16,17,2};
//
//    }

    private int[] getMoveArray(Type type){
      switch(type){
        case PAWN:
          return Type.PAWN_MOVES;
        case ROOK:
          return Type.ROOK_MOVES;
        case KNIGHT:
          return Type.KNIGHT_MOVES;
        case BISHOP:
          return Type.BISHOP_MOVES;
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

    private int getPieceValue(Type type){
      switch(type){
        case PAWN:
          return 10;
        case ROOK:
          return 50;
        case KNIGHT:
          return 30;
        case BISHOP:
          return 30;
        case KING:
          return 900;
        case QUEEN:
          return 90;
    }
    return 0;
  }
    
    public int[] getPawnAttacks() {
        return Type.PAWN_ATTACKS;
    }
    
    public int[] getCastlingMoves() {
        return Type.KING_CASTLE;
    }

    public String color = "W";
    public String name = "NONE"; //I think this is where we should say "KB" (Kings Bishop) etc.
    public int value = 0;
    public int[] moves;
    public boolean onSpot = true;
    Type type;

    public Piece(String piece, String color) {
        this.color = color;
        this.name = piece;
        this.type = Type.valueOf(piece);
        this.moves = getMoveArray(this.type);
        this.value = getPieceValue(this.type);
    }

    public Piece() {
        this.color = "NONE";
        this.name = "NONE";
        this.type = Type.valueOf("NONE");
        this.moves = getMoveArray(this.type);
        this.value = getPieceValue(this.type);
    }
    
    public void setMoved() {
        this.onSpot = false;
    }
}
