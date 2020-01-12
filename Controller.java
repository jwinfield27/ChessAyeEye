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
//            System.out.println("HOLDING: " + this.heldPiece);

            //If we click on the square where the piece started
            if (this.heldPiece == squareID) {
                this.dropPiece();
            }

            //If we click on an empty square
            else if (this.GAMEBOARD.Squares.get(squareID).piece.name == "NONE") {

                //get distance to click
                int distance = (squareID - this.heldPiece);
                
                //if the distance is in the move list then it can move
                for (int element : this.legalMoves) {
                    if (element == distance) {
                        this.movePiece(this.heldPiece, squareID);
                    }
                }
            }

            //If we click on an occupied square
            else if (this.GAMEBOARD.Squares.get(squareID).piece.name != "NONE") {

                //If we click on a piece of the same color do nothing
                //Maybe this is where we could check for castling?
                if (this.GAMEBOARD.Squares.get(squareID).piece.color == this.GAMEBOARD.Squares.get(this.heldPiece).piece.color) {
                    this.dropPiece();
                }

                //if we are holding a piece and click on an enemy piece
                else {
                    //get distance to click
                    int distance = (squareID - this.heldPiece);

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
            if ((this.GAMEBOARD.Squares.get(squareID).piece.name != "NONE") && (this.GAMEBOARD.Squares.get(squareID).piece.color == this.color)) {
                this.heldPiece = squareID;
                this.holdingPiece = true;
            }
        }
    }

    public int getHeld() {
        return this.heldPiece;
    }

    private void dropPiece() {
        System.out.println("Droped Piece: " + this.GAMEBOARD.Squares.get(this.heldPiece).piece.name);
        this.heldPiece = -1;
        this.holdingPiece = false;
    }

    private void movePiece(int leavingSquare, int destination) {
        if (this.GAMEBOARD.Squares.get(leavingSquare).piece.onSpot) {this.GAMEBOARD.Squares.get(leavingSquare).piece.setMoved();}
        
        //This is work around for AI vs Player movePiece so AI dont shit his pants
        if (this.holdingPiece) {this.dropPiece();}
        
        this.GAMEBOARD.Squares.get(destination).piece = this.GAMEBOARD.Squares.get(leavingSquare).piece;
        this.GAMEBOARD.Squares.get(leavingSquare).piece = new Piece(); //creates a new "NONE" piece
    }
    
    private void capturePiece(int attackingSquare, int capturedSquare) {
        if (this.GAMEBOARD.Squares.get(this.heldPiece).piece.onSpot) {this.GAMEBOARD.Squares.get(this.heldPiece).piece.setMoved();}
        
        //This is work around for AI vs Player movePiece so AI dont shit his pants
        if (this.holdingPiece) {this.dropPiece();} 
        
        this.GAMEBOARD.Squares.get(capturedSquare).piece = this.GAMEBOARD.Squares.get(attackingSquare).piece;
        this.GAMEBOARD.Squares.get(attackingSquare).piece = new Piece();
    }

    public void getLegalMoves(int squareID) {
        this.legalMoves.clear();
        
        Piece p = this.GAMEBOARD.Squares.get(squareID).piece;
        
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
            List<Integer> pawnAttacks = new ArrayList<Integer>();
                pawnAttacks.add(p.moves[3] * colorModifier);
                pawnAttacks.add(p.moves[4] * colorModifier);
            
            if (p.onSpot) {
                pawnMoves.add(p.moves[0] * colorModifier);
                pawnMoves.add(p.moves[1] * colorModifier); 
            }
            else {
                pawnMoves.add(p.moves[0] * colorModifier);
            }
            
            Iterator itPawnMoves = pawnMoves.iterator();
            while(itPawnMoves.hasNext()) {
                int m = (int)itPawnMoves.next();
                
                if (!this.GAMEBOARD.FLAT_EARTH.contains(squareID + m)) {
                    continue;
                }
                
                else if (this.GAMEBOARD.Squares.get(squareID + m).piece.name == "NONE") {
                    this.legalMoves.add(m);
                }
            }
            
            Iterator itPawnAttacks = pawnAttacks.iterator();
            while(itPawnAttacks.hasNext()) {
                int a = (int)itPawnAttacks.next();
                
                if (!this.GAMEBOARD.FLAT_EARTH.contains(squareID + a)) {
                    continue;
                }
                
                if ((this.GAMEBOARD.Squares.get(squareID + a).piece.type != Piece.Type.NONE) && (p.color != this.GAMEBOARD.Squares.get(squareID + a).piece.color)) {
                    this.legalMoves.add(a);
                }
            }
        }
        
        else if (p.type == Piece.Type.KING) {
            List<Integer> kingMoves = new ArrayList<Integer>(p.moves.length);
            
            if (p.onSpot) {
                for (int i = 0; i < p.moves.length; i++) {
                    kingMoves.add(p.moves[i]);
                    kingMoves.add(p.moves[i] * -1);
                }
            }
            else {
                for (int i = 0; i < p.moves.length - 1; i++) {
                    kingMoves.add(p.moves[i]);
                    kingMoves.add(p.moves[i] * -1);
                }
            }
            
            Iterator itKingMoves = kingMoves.iterator();
            while(itKingMoves.hasNext()) {
                int m = (int)itKingMoves.next();
                
                if (!this.GAMEBOARD.FLAT_EARTH.contains(squareID + m)) {
                    continue;
                }
                
                else if ((this.GAMEBOARD.Squares.get(squareID + m).piece.name == "NONE") || (p.color != this.GAMEBOARD.Squares.get(squareID + m).piece.color)) {
                    this.legalMoves.add(m);
                }
            }
        }
        
        else if (p.type == Piece.Type.QUEEN) {
            List<Integer> queenMoves = new ArrayList<Integer>(p.moves.length);
            
            for (int i = 0; i < p.moves.length; i++) {
                queenMoves.add(p.moves[i]);
                queenMoves.add(p.moves[i] * -1);
                System.out.println("Added (" + p.moves[i] + ", " + -p.moves[i] + ")");
            }

            Iterator itQueenMoves = queenMoves.iterator();
            while(itQueenMoves.hasNext()) {
                int m = (int)itQueenMoves.next();
               
                
                if (!this.GAMEBOARD.FLAT_EARTH.contains(squareID + m)) {
                    continue;
                }
                else  if ((this.GAMEBOARD.Squares.get(squareID + m).piece.name == "NONE") || (p.color != this.GAMEBOARD.Squares.get(squareID + m).piece.color)) {
                    this.legalMoves.add(m);
                }
            }
        }
        
        else if (p.type == Piece.Type.BISHOP_BLACK) {
            List<Integer> bishopBlackMoves = new ArrayList<Integer>(p.moves.length);
            
            for (int i = 0; i < p.moves.length; i++) {
                bishopBlackMoves.add(p.moves[i]);
                bishopBlackMoves.add(p.moves[i] * -1);
            }

            Iterator itBishopBlackMoves = bishopBlackMoves.iterator();
            while(itBishopBlackMoves.hasNext()) {
                int m = (int)itBishopBlackMoves.next();
                
                if (!this.GAMEBOARD.FLAT_EARTH.contains(squareID + m)) {
                    continue;
                }
                
                else if ((this.GAMEBOARD.Squares.get(squareID + m).piece.name == "NONE") || (p.color != this.GAMEBOARD.Squares.get(squareID + m).piece.color)) {
                    this.legalMoves.add(m);
                }
            }
        }
        
        else if (p.type == Piece.Type.BISHOP_WHITE) {
            List<Integer> bishopWhiteMoves = new ArrayList<Integer>(p.moves.length);
            
            for (int i = 0; i < p.moves.length; i++) {
                bishopWhiteMoves.add(p.moves[i]);
                bishopWhiteMoves.add(p.moves[i] * -1);
            }

            Iterator itBishopWhiteMoves = bishopWhiteMoves.iterator();
            while(itBishopWhiteMoves.hasNext()) {
                int m = (int)itBishopWhiteMoves.next();
                
                if (!this.GAMEBOARD.FLAT_EARTH.contains(squareID + m)) {
                    continue;
                }
                
                else if ((this.GAMEBOARD.Squares.get(squareID + m).piece.name == "NONE") || (p.color != this.GAMEBOARD.Squares.get(squareID + m).piece.color)) {
                    this.legalMoves.add(m);
                }
            }
        }
        
        else if (p.type == Piece.Type.ROOK) {
            List<Integer> rookMoves = new ArrayList<Integer>(p.moves.length);
            
            for (int i = 0; i < p.moves.length; i++) {
                rookMoves.add(p.moves[i]);
                rookMoves.add(p.moves[i] * -1);
            }

            Iterator itRookMoves = rookMoves.iterator();
            while(itRookMoves.hasNext()) {
                int m = (int)itRookMoves.next();
                
                if (!this.GAMEBOARD.FLAT_EARTH.contains(squareID + m)) {
                    continue;
                }
                
                else if ((this.GAMEBOARD.Squares.get(squareID + m).piece.name == "NONE") || (p.color != this.GAMEBOARD.Squares.get(squareID + m).piece.color)) {
                    this.legalMoves.add(m);
                }
            }
        }
        
        else if (p.type == Piece.Type.KNIGHT) {
            List<Integer> knightMoves = new ArrayList<Integer>(p.moves.length);
            
            for (int i = 0; i < p.moves.length; i++) {
                knightMoves.add(p.moves[i]);
                knightMoves.add(p.moves[i] * -1);
            }

            Iterator itKnightMoves = knightMoves.iterator();
            while(itKnightMoves.hasNext()) {
                int m = (int)itKnightMoves.next();
                
                if (!this.GAMEBOARD.FLAT_EARTH.contains(squareID + m)) {
                    continue;
                }
                
                else if ((this.GAMEBOARD.Squares.get(squareID + m).piece.name == "NONE") || (p.color != this.GAMEBOARD.Squares.get(squareID + m).piece.color)) {
                    this.legalMoves.add(m);
                }
            }
        }
    }
}
