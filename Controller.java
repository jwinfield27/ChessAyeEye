package ChessAyeEye;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Controller {
    public static Board GAMEBOARD; //this is THE board that is actually being played on.
    public static Boolean whiteFirstTurn = true; //why do we need these?
    public static Boolean blackFirstTurn = true;
    public String color = "W";

    private int heldPiece = -1;
    private boolean holdingPiece = false;
    public List<Integer> legalMoves = new ArrayList<>();

    public Controller() {
        this.GAMEBOARD = new Board();
    }

    public Controller(Board board){
        GAMEBOARD = board;
    }


    private void newController(Board board){
        //I don't think Controller will be making new controllers ver often?
        //one for player one for AI?
        //So maybe this is newBoard() instead to add boards to
        //possibleMoves for the AI?
    }

    //decides whether to move, capture, or drop a pice that is being held
    public void handleClick(int squareID) {
        //if we are holding a piece
        if (this.holdingPiece) {
            this.getLegalMoves(this.heldPiece);

            //If we click on the square where the piece started
            if (this.heldPiece == squareID) {
                this.dropPiece();
            }

            //If we click on an empty square
            else if (!this.GAMEBOARD.Squares.get(squareID).isOccupied()) {

//                get distance to click
                int distance = (squareID - this.heldPiece);
                System.out.println("Distance: " + distance);

                //if the distance is in the move list then it can move
                for (int element : this.legalMoves) {
                    if (element == distance) {
                        this.movePiece(this.heldPiece, squareID);
                    }
                }
            }

            //If we click on an occupied square
            else if (this.GAMEBOARD.Squares.get(squareID).Piece.name != "NONE") {

                //If we click on a piece of the same color do nothing
                //Maybe this is where we could check for castling?
                if (this.GAMEBOARD.Squares.get(squareID).Piece.color == this.GAMEBOARD.Squares.get(this.heldPiece).Piece.color) {
                    this.dropPiece();
                }

                //if we are holding a piece and click on an enemy piece
                else {
                    //get distance to click
                    int distance = (squareID - this.heldPiece);
                    System.out.println("Distance: " + distance);

                    //if the distance is in the move list then it can move
                    for (int element : legalMoves) {
                        if (element == distance) {
                            this.capturePiece(this.heldPiece, squareID);
                        }
                    }
                }
            }
        }

        else {
            if ((this.GAMEBOARD.Squares.get(squareID).Piece.name != "NONE") && (this.GAMEBOARD.Squares.get(squareID).Piece.color == this.color)) {
                this.heldPiece = squareID;
                this.holdingPiece = true;
                this.getLegalMoves(this.heldPiece);
            }
        }
    }

    public int getHeld() {
        return this.heldPiece;
    }

    private void dropPiece() {
//        System.out.println("Droped Piece: " + this.GAMEBOARD.Squares.get(this.heldPiece).Piece.name);
        this.heldPiece = -1;
        this.holdingPiece = false;
    }

    private void movePiece(int leavingSquare, int destination) {
        if (this.GAMEBOARD.Squares.get(leavingSquare).Piece.onSpot) {this.GAMEBOARD.Squares.get(leavingSquare).Piece.setMoved();}

        //This is work around for AI vs Player movePiece so AI dont shit his pants
        if (this.holdingPiece) {this.dropPiece();}

        this.GAMEBOARD.Squares.get(destination).Piece = this.GAMEBOARD.Squares.get(leavingSquare).Piece;
        this.GAMEBOARD.Squares.get(leavingSquare).Piece = new Piece(); //creates a new "NONE" piece
    }

    private void capturePiece(int attackingSquare, int capturedSquare) {
        if (this.GAMEBOARD.Squares.get(this.heldPiece).Piece.onSpot) {this.GAMEBOARD.Squares.get(this.heldPiece).Piece.setMoved();}

        //This is work around for AI vs Player movePiece so AI dont shit his pants
        if (this.holdingPiece) {this.dropPiece();}

        this.GAMEBOARD.Squares.get(capturedSquare).Piece = this.GAMEBOARD.Squares.get(attackingSquare).Piece;
        this.GAMEBOARD.Squares.get(attackingSquare).Piece = new Piece();
    }

    public void getLegalMoves(int squareID) {
        this.legalMoves.clear();

        Piece p = this.GAMEBOARD.Squares.get(squareID).Piece;

        if (p.type == Piece.Type.PAWN) {

            int colorModifier = 1;
            switch (p.color) {
                    case "W":   colorModifier = -1;
                                            break;
                    case "B":   colorModifier = 1;
                                            break;
                    default:    System.out.println("Something fucky wucky Controller line ~130");
            }
            
            List<Integer> pawnMoves = new ArrayList<Integer>();
            
            if (this.GAMEBOARD.FLAT_EARTH.contains(squareID + (p.moves[0] * colorModifier)) && 
                    !this.GAMEBOARD.Squares.get(squareID + (p.moves[0] * colorModifier)).isOccupied()) {
                pawnMoves.add(p.moves[0] * colorModifier);
            }
            if (p.onSpot && !this.GAMEBOARD.Squares.get(squareID + (p.moves[0] * colorModifier * 2)).isOccupied()) {
                pawnMoves.add(p.moves[0] * (colorModifier * 2));
            }
            
            int[] pawnAttacks = p.getPawnAttacks();
            for (int i = 0; i < pawnAttacks.length; i++) {
                int move = pawnAttacks[i] * colorModifier;
                System.out.println(move);
                if (this.GAMEBOARD.FLAT_EARTH.contains(squareID + move) && 
                        (this.GAMEBOARD.Squares.get(squareID + move).isOccupied() && 
                        this.GAMEBOARD.Squares.get(squareID + move).color != p.color)) {
                    pawnMoves.add(pawnAttacks[i] * colorModifier);
                }
            }
            
            Iterator itPawnMoves = pawnMoves.iterator();
            while(itPawnMoves.hasNext()) {
                int m = (int)itPawnMoves.next();
                this.legalMoves.add(m);
            }
        }

        else if (p.type == Piece.Type.KING) {
            List<Integer> kingMoves = new ArrayList<Integer>();
            
            for (int i = 0; i < p.moves.length; i++) {
                if (this.GAMEBOARD.FLAT_EARTH.contains(squareID + p.moves[i]) &&
                        (!this.GAMEBOARD.Squares.get(squareID + p.moves[i]).isOccupied() ||
                            this.GAMEBOARD.Squares.get(squareID + p.moves[i]).color != p.color)) {
                    kingMoves.add(p.moves[i]);
                }
                if (this.GAMEBOARD.FLAT_EARTH.contains(squareID + (p.moves[i] * -1)) &&
                        (!this.GAMEBOARD.Squares.get(squareID + (p.moves[i] * -1)).isOccupied() ||
                            this.GAMEBOARD.Squares.get(squareID + (p.moves[i] * -1)).color != p.color)) {
                    kingMoves.add(p.moves[i] * -1);
                }
            }
            
            if (p.onSpot) {
                System.out.println("CONTROLLER::~180 \t | \t Haha, haven't written castling yet.");
            }

            Iterator itKingMoves = kingMoves.iterator();
            while(itKingMoves.hasNext()) {
                int m = (int)itKingMoves.next();
                this.legalMoves.add(m);
            }
        }
        
        else if (p.type == Piece.Type.KNIGHT) {
            List<Integer> knightMoves = new ArrayList<Integer>();
            
            for (int i = 0; i < p.moves.length; i++) {
                if (this.GAMEBOARD.FLAT_EARTH.contains(squareID + p.moves[i]) &&
                        this.GAMEBOARD.Squares.get(squareID + p.moves[i]).color != p.color) {
                    knightMoves.add(p.moves[i]);
                }
                if (this.GAMEBOARD.FLAT_EARTH.contains(squareID + (p.moves[i] * -1)) &&
                        this.GAMEBOARD.Squares.get(squareID + (p.moves[i] * -1)).color != p.color) {
                    knightMoves.add(p.moves[i] * -1);
                }
            }

            Iterator itkNightMoves = knightMoves.iterator();
            while(itkNightMoves.hasNext()) {
                int m = (int)itkNightMoves.next();
                this.legalMoves.add(m);
            }
        }

        else {
            List<Integer> pieceMoves = new ArrayList<Integer>();

            for (int i = 0; i < p.moves.length; i++) {
                
                int distance = p.moves[i];
                int ogresHaveLayers = 1;
                
                while (this.GAMEBOARD.FLAT_EARTH.contains(squareID + (distance * ogresHaveLayers))) {
                    if (!this.GAMEBOARD.Squares.get(squareID + (distance * ogresHaveLayers)).isOccupied()) {
                        pieceMoves.add(distance * ogresHaveLayers);
                    }
                    else if (this.GAMEBOARD.Squares.get(squareID + (distance * ogresHaveLayers)).color != p.color) {
                        pieceMoves.add(distance * ogresHaveLayers);
                        break;
                    }
                    else {
                        break;
                    }
                    ogresHaveLayers += 1;
                }
                
                ogresHaveLayers = 1;
                
                while (this.GAMEBOARD.FLAT_EARTH.contains(squareID + (distance * ogresHaveLayers * -1))) {
                    if (!this.GAMEBOARD.Squares.get(squareID + (distance * ogresHaveLayers * -1)).isOccupied()) {
                        pieceMoves.add(distance * ogresHaveLayers * -1);
                    }
                    else if (this.GAMEBOARD.Squares.get(squareID + (distance * ogresHaveLayers * -1)).color != p.color) {
                        pieceMoves.add(distance * ogresHaveLayers * -1);
                        break;
                    }
                    else {
                        break;
                    }
                    ogresHaveLayers += 1;
                }
            }

            Iterator itPieceMoves = pieceMoves.iterator();
            while(itPieceMoves.hasNext()) {
                int m = (int)itPieceMoves.next();
                this.legalMoves.add(m);
            }
        }
        
        Iterator DEBUGMOVES = this.legalMoves.iterator();
        System.out.println("MOVES FOR: " + this.heldPiece);
        while (DEBUGMOVES.hasNext()) {
            System.out.println((int)DEBUGMOVES.next());
        }
    }

    public int moveAI(int squareID){
      getLegalMoves(squareID);
      movePiece(squareID,legalMoves.get(legalMoves.size()-1)+squareID);
      return (squareID + legalMoves.get(legalMoves.size()-1));
    }
//                for (int i = 0; i < p.moves.length; i++) {
//                    
//                }
}
