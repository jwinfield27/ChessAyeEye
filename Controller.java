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
    
    private Player PLAYER1;
    private Player PLAYER2;
    public Player currentPlayer;
    
    public List<Integer> legalMoves = new ArrayList<>();
    public List<Integer> legalSquaresWhiteKing = new ArrayList<>();
    public List<Integer> legalSquaresBlackKing = new ArrayList<>();
    private int enPassantSquare = -1;
    private int enPassantPiece = -1;

    public Controller(String gameType) {
        switch (gameType) {
            case "PvP": this.PLAYER1 = new Player("W");
                        this.PLAYER2 = new Player("B");
                        this.currentPlayer = this.PLAYER1;
                        break;
            case "PvC": this.PLAYER1 = new Player("W");
                        this.PLAYER2 = new AI("B");
                        this.currentPlayer = this.PLAYER1;
                        break;
            case "CvC": this.PLAYER1 = new AI("W");
                        this.PLAYER2 = new AI("B");
                        this.currentPlayer = this.PLAYER1;
                        break;
            default:    System.out.println("Uh oh spaghettios Controller ~36");
        }
        this.GAMEBOARD = new Board();
    }

    public Controller(Board board){
        GAMEBOARD = board;
    }

    //decides whether to move, capture, or drop a pice that is being held
    public void handleClick(int squareID) {
        Player p = this.currentPlayer;
        //if we are holding a piece
        if (p.isHolding()) {
            int heldPiece = p.getHeld();
            this.getLegalMoves(heldPiece);

            //If we click on the square where the piece started
            if (heldPiece == squareID) {
                p.dropPiece();
            }

            //If we click on an empty square
            else if (!this.GAMEBOARD.Squares.get(squareID).isOccupied()) {

//                get distance to click
                int distance = (squareID - heldPiece);

                //if the distance is in the move list then it can move
                for (int element : this.legalMoves) {
                    if (element == distance) {
                        if (this.GAMEBOARD.Squares.get(heldPiece).Piece.type == Piece.Type.PAWN && (Math.abs(distance) == (this.GAMEBOARD.Squares.get(heldPiece).Piece.moves[0] * 2))) {
                           this.enPassantSquare = heldPiece + (distance/2); 
                           this.enPassantPiece = squareID;
                           System.out.println("En Passant: \t" + this.enPassantSquare);
                        }
                        if (squareID == this.enPassantSquare) {
                            this.capturePieceEnPassant(heldPiece, squareID);
                        }
                        else {
                            this.movePiece(heldPiece, squareID);
                        }
                    }
                }
            }

            //If we click on an occupied square
            else if (this.GAMEBOARD.Squares.get(squareID).Piece.name != "NONE") {

                //If we click on a piece of the same color do nothing
                //Maybe this is where we could check for castling?
                if (this.GAMEBOARD.Squares.get(squareID).Piece.color == this.GAMEBOARD.Squares.get(heldPiece).Piece.color) {
                    p.dropPiece();
                }

                //if we are holding a piece and click on an enemy piece
                else {
                    //get distance to click
                    int distance = (squareID - heldPiece);

                    //if the distance is in the move list then it can move
                    for (int element : legalMoves) {
                        if (element == distance) {
                            this.capturePiece(heldPiece, squareID);
                        }
                    }
                }
            }
        }

        else {
            if ((this.GAMEBOARD.Squares.get(squareID).Piece.name != "NONE") && (this.GAMEBOARD.Squares.get(squareID).Piece.color == p.color)) {
                p.grabPiece(squareID);
                this.getLegalMoves(squareID);
            }
        }
    }


    private void movePiece(int leavingSquare, int destination) {
        if (this.GAMEBOARD.Squares.get(leavingSquare).Piece.onSpot) {this.GAMEBOARD.Squares.get(leavingSquare).Piece.setMoved();}

        //This is work around for AI vs Player movePiece so AI dont shit his pants
        if (this.currentPlayer.isHolding()) {this.currentPlayer.dropPiece();}

        this.GAMEBOARD.Squares.get(destination).Piece = this.GAMEBOARD.Squares.get(leavingSquare).Piece;
        this.GAMEBOARD.Squares.get(leavingSquare).Piece = new Piece(); //creates a new "NONE" piece
        this.endTurn();
    }

    private void capturePiece(int attackingSquare, int capturedSquare) {
        if (this.GAMEBOARD.Squares.get(this.currentPlayer.getHeld()).Piece.onSpot) {this.GAMEBOARD.Squares.get(this.currentPlayer.getHeld()).Piece.setMoved();}

        //This is work around for AI vs Player movePiece so AI dont shit his pants
        if (this.currentPlayer.isHolding()) {this.currentPlayer.dropPiece();}

        this.GAMEBOARD.Squares.get(capturedSquare).Piece = this.GAMEBOARD.Squares.get(attackingSquare).Piece;
        this.GAMEBOARD.Squares.get(attackingSquare).Piece = new Piece();
        this.endTurn();
    }
    private void capturePieceEnPassant(int attackingSquare, int capturedSquare) {
        if (this.GAMEBOARD.Squares.get(this.currentPlayer.getHeld()).Piece.onSpot) {this.GAMEBOARD.Squares.get(this.currentPlayer.getHeld()).Piece.setMoved();}

        //This is work around for AI vs Player movePiece so AI dont shit his pants
        if (this.currentPlayer.isHolding()) {this.currentPlayer.dropPiece();}

        this.GAMEBOARD.Squares.get(capturedSquare).Piece = this.GAMEBOARD.Squares.get(attackingSquare).Piece;
        this.GAMEBOARD.Squares.get(attackingSquare).Piece = new Piece();
        this.GAMEBOARD.Squares.get(this.enPassantPiece).Piece = new Piece();
        this.endTurn();
    }

    private void endTurn() {
        if ("W".equals(this.currentPlayer.color)) {
            this.currentPlayer = this.PLAYER2;
            if (this.enPassantPiece >=0) {
                if (this.GAMEBOARD.Squares.get(this.enPassantPiece).Piece.color == "B") {
                    this.enPassantPiece = -1;
                    this.enPassantSquare = -1;
                }
           }
        }
        else {
            this.currentPlayer = this.PLAYER1;
            if (this.enPassantPiece >=0) {
                if (this.GAMEBOARD.Squares.get(this.enPassantPiece).Piece.color == "W") {
                    this.enPassantPiece = -1;
                    this.enPassantSquare = -1;
                }
           }
        }
    }

    private void getBadSquares() {
        //maybe get list of bad squares for kings here or do it in getLegalMoves
    }
    
    public void getLegalMoves(int squareID) {
        this.legalMoves.clear();
        
        /*
            PAWN -~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~
        */

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
                pawnMoves.add(p.moves[0] * colorModifier * 2);
            }
            
            int[] pawnAttacks = p.getPawnAttacks();
            for (int i = 0; i < pawnAttacks.length; i++) {
                int move = pawnAttacks[i] * colorModifier;
                if (this.GAMEBOARD.FLAT_EARTH.contains(squareID + move)) {
                    if ((this.GAMEBOARD.Squares.get(squareID + move).isOccupied() && 
                        this.GAMEBOARD.Squares.get(squareID + move).Piece.color != p.color) ||
                        ((squareID + move) == this.enPassantSquare) && this.GAMEBOARD.Squares.get(squareID + move).Piece.color != p.color) {
                        pawnMoves.add(pawnAttacks[i] * colorModifier);
                    }
                }
            }
            
            Iterator itPawnMoves = pawnMoves.iterator();
            while(itPawnMoves.hasNext()) {
                int m = (int)itPawnMoves.next();
                this.legalMoves.add(m);
            }
        }
        /*
            KING -~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~
        */
        else if (p.type == Piece.Type.KING) {
            List<Integer> kingMoves = new ArrayList<Integer>();
            
            for (int i = 0; i < p.moves.length; i++) {
                if (this.GAMEBOARD.FLAT_EARTH.contains(squareID + p.moves[i]) &&
                        (!this.GAMEBOARD.Squares.get(squareID + p.moves[i]).isOccupied() ||
                            this.GAMEBOARD.Squares.get(squareID + p.moves[i]).Piece.color != p.color)) {
                    kingMoves.add(p.moves[i]);
                }
                if (this.GAMEBOARD.FLAT_EARTH.contains(squareID + (p.moves[i] * -1)) &&
                        (!this.GAMEBOARD.Squares.get(squareID + (p.moves[i] * -1)).isOccupied() ||
                            this.GAMEBOARD.Squares.get(squareID + (p.moves[i] * -1)).Piece.color != p.color)) {
                    kingMoves.add(p.moves[i] * -1);
                }
            }
            
            /*
                check for legality of castling here.
            */
            
            if (p.onSpot) {
                System.out.println("CONTROLLER::~180 \t | \t Haha, haven't written castling yet.");
            }

            Iterator itKingMoves = kingMoves.iterator();
            while(itKingMoves.hasNext()) {
                int m = (int)itKingMoves.next();
                this.legalMoves.add(m);
            }
        }
        /*
            kNIGHT -~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~
        */
        else if (p.type == Piece.Type.KNIGHT) {
            List<Integer> knightMoves = new ArrayList<Integer>();
            
            for (int i = 0; i < p.moves.length; i++) {
                if (this.GAMEBOARD.FLAT_EARTH.contains(squareID + p.moves[i]) &&
                        (!this.GAMEBOARD.Squares.get(squareID + p.moves[i]).isOccupied() ||
                        this.GAMEBOARD.Squares.get(squareID + p.moves[i]).Piece.color != p.color)) {
                    knightMoves.add(p.moves[i]);
                }
                if (this.GAMEBOARD.FLAT_EARTH.contains(squareID + (p.moves[i] * -1)) &&
                        (!this.GAMEBOARD.Squares.get(squareID + (p.moves[i] * -1)).isOccupied() ||
                        this.GAMEBOARD.Squares.get(squareID + (p.moves[i] * -1)).Piece.color != p.color)) {
                    knightMoves.add(p.moves[i] * -1);
                }
            }

            Iterator itkNightMoves = knightMoves.iterator();
            while(itkNightMoves.hasNext()) {
                int m = (int)itkNightMoves.next();
                this.legalMoves.add(m);
            }
        }
        /*
            QUEEN, BISHOP, ROOK -~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~
        */
        else {
            List<Integer> pieceMoves = new ArrayList<Integer>();

            for (int i = 0; i < p.moves.length; i++) {
                
                int distance = p.moves[i];
                int ogresHaveLayers = 1;
                
                while (this.GAMEBOARD.FLAT_EARTH.contains(squareID + (distance * ogresHaveLayers))) {
                    if (!this.GAMEBOARD.Squares.get(squareID + (distance * ogresHaveLayers)).isOccupied()) {
                        pieceMoves.add(distance * ogresHaveLayers);
                    }
                    else if (this.GAMEBOARD.Squares.get(squareID + (distance * ogresHaveLayers)).Piece.color != p.color) {
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
                    else if (this.GAMEBOARD.Squares.get(squareID + (distance * ogresHaveLayers * -1)).Piece.color != p.color) {
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
    }

    public int moveAI(int squareID){
      getLegalMoves(squareID);
      movePiece(squareID,legalMoves.get(legalMoves.size()-1)+squareID);
      return (squareID + legalMoves.get(legalMoves.size()-1));
    }
}
