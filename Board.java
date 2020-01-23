package ChessAyeEye;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
/*
This class keeps track of:
    Board state
    setting up the board

TODO(?):
    - probably has several utilities to determine: legal moves, check, draw,
    checkmate, ???
    - records the games? https://en.wikipedia.org/wiki/Chess_notation
*/

public class Board{
    Map<Integer, Square> Squares = new HashMap<Integer, Square>();
    
    //FLAT_EARTH is used in Controller to let getLegalMoves know what squares 
    //exist because we skip some numbers for example numbers 8-15 which can cause
    //a problem when we have a piece on the edge of a board trying to move off the board, since 
    //for example a rook at 80 moving left (-1) looking for square 79. 
    //Name negotiable
    public List<Integer> FLAT_EARTH = List.of(
        0,1,2,3,4,5,6,7,
        16,17,18,19,20,21,22,23,
        32,33,34,35,36,37,38,39,
        48,49,50,51,52,53,54,55,
        64,65,66,67,68,69,70,71,
        80,81,82,83,84,85,86,87,
        96,97,98,99,100,101,102,103,
        112,113,114,115,116,117,118,119);

    public Board() {
        this.newBoard();
    }

    //Now this is hot
    /*
    Map.entry("a8", 0), Map.entry("b8", 1), Map.entry("c8", 2), Map.entry("d8", 3), Map.entry("e8", 4), Map.entry("f8", 5), Map.entry("g8", 6), Map.entry("h8", 7),
    Map.entry("a7", 16), Map.entry("b7", 17), Map.entry("c7", 18), Map.entry("d7", 19), Map.entry("e7", 20), Map.entry("f7", 21), Map.entry("g7", 22), Map.entry("h7", 23),
    Map.entry("a6", 32), Map.entry("b6", 33), Map.entry("c6", 34), Map.entry("d6", 35), Map.entry("e6", 36), Map.entry("f6", 37), Map.entry("g6", 38), Map.entry("h6", 39),
    Map.entry("a5", 48), Map.entry("b5", 49), Map.entry("c5", 50), Map.entry("d5", 51), Map.entry("e5", 52), Map.entry("f5", 53), Map.entry("g5", 54), Map.entry("h5", 55),
    Map.entry("a4", 64), Map.entry("b4", 65), Map.entry("c4", 66), Map.entry("d4", 67), Map.entry("e4", 68), Map.entry("f4", 69), Map.entry("g4", 70), Map.entry("h4", 71),
    Map.entry("a3", 80), Map.entry("b3", 81), Map.entry("c3", 82), Map.entry("d3", 83), Map.entry("e3", 84), Map.entry("f3", 85), Map.entry("g3", 86), Map.entry("h3", 87),
    Map.entry("a2", 96), Map.entry("b2", 97), Map.entry("c2", 98), Map.entry("d2", 99), Map.entry("e2", 100), Map.entry("f2", 101), Map.entry("g2", 102), Map.entry("h2", 103),
    Map.entry("a1", 112), Map.entry("b1", 113), Map.entry("c1", 114), Map.entry("d1", 115), Map.entry("e1", 116), Map.entry("f1", 117), Map.entry("g1", 118), Map.entry("h1", 119)
    */

    //Populates the board with everything in the right place
    private void newBoard() {
        Squares.put(0, new Square("W", "a8", new Piece("ROOK", "B")));     //black back row
        Squares.put(1, new Square("B", "b8", new Piece("KNIGHT", "B")));
        Squares.put(2, new Square("W", "c8", new Piece("BISHOP", "B")));
        Squares.put(3, new Square("B", "d8", new Piece("QUEEN", "B")));
        Squares.put(4, new Square("W", "e8", new Piece("KING", "B")));
        Squares.put(5, new Square("B", "f8", new Piece("BISHOP", "B")));
        Squares.put(6, new Square("W", "g8", new Piece("KNIGHT", "B")));
        Squares.put(7, new Square("B", "h8", new Piece("ROOK", "B")));     //black back row
        Squares.put(16, new Square("B", "a7", new Piece("PAWN", "B")));    //black pawns
        Squares.put(17, new Square("W", "b7", new Piece("PAWN", "B")));
        Squares.put(18, new Square("B", "c7", new Piece("PAWN", "B")));
        Squares.put(19, new Square("W", "d7", new Piece("PAWN", "B")));
        Squares.put(20, new Square("B", "e7", new Piece("PAWN", "B")));
        Squares.put(21, new Square("W", "f7", new Piece("PAWN", "B")));
        Squares.put(22, new Square("B", "g7", new Piece("PAWN", "B")));
        Squares.put(23, new Square("W", "h7", new Piece("PAWN", "B")));    //black pawns
        Squares.put(32, new Square("W", "a6", new Piece()));
        Squares.put(33, new Square("B", "b6", new Piece()));
        Squares.put(34, new Square("W", "c6", new Piece()));
        Squares.put(35, new Square("B", "d6", new Piece()));
        Squares.put(36, new Square("W", "e6", new Piece()));
        Squares.put(37, new Square("B", "f6", new Piece()));
        Squares.put(38, new Square("W", "g6", new Piece()));
        Squares.put(39, new Square("B", "h6", new Piece()));
        Squares.put(48, new Square("B", "a5", new Piece()));
        Squares.put(49, new Square("W", "b5", new Piece()));
        Squares.put(50, new Square("B", "c5", new Piece()));
        Squares.put(51, new Square("W", "d5", new Piece()));
        Squares.put(52, new Square("B", "e5", new Piece()));
        Squares.put(53, new Square("W", "f5", new Piece()));
        Squares.put(54, new Square("B", "g5", new Piece()));
        Squares.put(55, new Square("W", "h5", new Piece()));
        Squares.put(64, new Square("W", "a4", new Piece()));
        Squares.put(65, new Square("B", "b4", new Piece()));
        Squares.put(66, new Square("W", "c4", new Piece()));
        Squares.put(67, new Square("B", "d4", new Piece()));
        Squares.put(68, new Square("W", "e4", new Piece()));
        Squares.put(69, new Square("B", "f4", new Piece()));
        Squares.put(70, new Square("W", "g4", new Piece()));
        Squares.put(71, new Square("B", "h4", new Piece()));
        Squares.put(80, new Square("B", "a3", new Piece()));
        Squares.put(81, new Square("W", "b3", new Piece()));
        Squares.put(82, new Square("B", "c3", new Piece()));
        Squares.put(83, new Square("W", "d3", new Piece()));
        Squares.put(84, new Square("B", "e3", new Piece()));
        Squares.put(85, new Square("W", "f3", new Piece()));
        Squares.put(86, new Square("B", "g3", new Piece()));
        Squares.put(87, new Square("W", "h3", new Piece()));
        Squares.put(96, new Square("W", "a2", new Piece("PAWN", "W")));      //white pawns
        Squares.put(97, new Square("B", "b2", new Piece("PAWN", "W")));
        Squares.put(98, new Square("W", "c2", new Piece("PAWN", "W")));
        Squares.put(99, new Square("B", "d2", new Piece("PAWN", "W")));
        Squares.put(100, new Square("W", "e2", new Piece("PAWN", "W")));
        Squares.put(101, new Square("B", "f2", new Piece("PAWN", "W")));
        Squares.put(102, new Square("W", "g2", new Piece("PAWN", "W")));
        Squares.put(103, new Square("B", "h2", new Piece("PAWN", "W")));     //white pawns
        Squares.put(112, new Square("B", "a1", new Piece("ROOK", "W")));
        Squares.put(113, new Square("W", "b1", new Piece("KNIGHT", "W")));
        Squares.put(114, new Square("B", "c1", new Piece("BISHOP", "W")));
        Squares.put(115, new Square("W", "d1", new Piece("QUEEN", "W")));
        Squares.put(116, new Square("B", "e1", new Piece("KING", "W")));
        Squares.put(117, new Square("W", "f1", new Piece("BISHOP", "W")));
        Squares.put(118, new Square("B", "g1", new Piece("KNIGHT", "W")));
        Squares.put(119, new Square("W", "h1", new Piece("ROOK", "W")));   //white back row
    }


    //prints the board
    //TODO fix how board prints to make tracing moves easier on the eye and my brain :)
    public void printBoard() {
        int mentalStabilityCount = 0;
        for (Map.Entry<Integer, Square> e : this.Squares.entrySet()) {
          if (mentalStabilityCount%8 == 0){
            System.out.println("\n");
          }
            System.out.print(e.getValue().rankfile + " : " + e.getValue().Piece.type + " : " + e.getValue().Piece.color + " | ");
            mentalStabilityCount += 1;
        }
//        System.out.println(String.valueOf(this.Squares.get(0).piece.type.BISHOP_BLACK_MOVES[0]));
    }

    public Board(Board board){
      this.Squares = board.Squares;
    }
}
