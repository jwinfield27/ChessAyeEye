package ChessAyeEye;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Controller {
    public static Board GAMEBOARD; //this is THE board that is actually being played on.
    
    private Player PLAYER1;
    private Player PLAYER2;
    public Player currentPlayer;
    
    public List<Integer> legalMoves = new ArrayList<>();
    public List<Integer> illegalSquaresWhiteKing = new ArrayList<>();
    public List<Integer> illegalSquaresBlackKing = new ArrayList<>();
    
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
                
        for (Map.Entry<Integer, Square> e : this.GAMEBOARD.Squares.entrySet()) {
            int id = (int)e.getKey();
            Piece p = this.GAMEBOARD.Squares.get(id).Piece;
            if (p.type != Piece.Type.NONE) {
                if (p.color == "B") {
                    this.PLAYER2.pieceList.add(id);
                }
                else {
                    this.PLAYER1.pieceList.add(id);
                }
            }
        }
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
            this.legalMoves.addAll(this.getLegalMoves(heldPiece));

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
                        
                        
                        //EN PASSANT-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~
                        //If we move 2 squares with a pawn, update the en passant square and piece to their correct values
                        if (this.GAMEBOARD.Squares.get(heldPiece).Piece.type == Piece.Type.PAWN && (Math.abs(distance) == (this.GAMEBOARD.Squares.get(heldPiece).Piece.moves[0] * 2))) {
                           this.enPassantSquare = heldPiece + (distance/2); 
                           this.enPassantPiece = squareID;
//                           System.out.println("En Passant: \t" + this.enPassantSquare);
                        }
                        
                        //if we click on the en passant square we invoke the rule and dish out sweet justice to the foolish pawn
                        if (squareID == this.enPassantSquare) {
                            this.capturePieceEnPassant(heldPiece, squareID);
                        }
                        
                        else if (this.GAMEBOARD.Squares.get(heldPiece).Piece.type == Piece.Type.KING && 
                                (distance == this.GAMEBOARD.Squares.get(heldPiece).Piece.getCastlingMoves()[0] ||
                                distance == this.GAMEBOARD.Squares.get(heldPiece).Piece.getCastlingMoves()[1])) {
                            this.movePieceCaslting(heldPiece, distance);
                            
                        }
                        //otherwise we just move to the blank square
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
                this.legalMoves.addAll(this.getLegalMoves(this.currentPlayer.getHeld()));
            }
        }
    }


    private void movePiece(int leavingSquare, int destination) {
        if (this.GAMEBOARD.Squares.get(leavingSquare).Piece.onSpot) {this.GAMEBOARD.Squares.get(leavingSquare).Piece.setMoved();}

        //This is work around for AI vs Player movePiece so AI dont shit his pants
        if (this.currentPlayer.isHolding()) {this.currentPlayer.dropPiece();}

        this.GAMEBOARD.Squares.get(destination).Piece = this.GAMEBOARD.Squares.get(leavingSquare).Piece;
        this.GAMEBOARD.Squares.get(leavingSquare).Piece = new Piece(); //creates a new "NONE" piece
        
                        String c = this.currentPlayer.color;
                        if (c == "B") {
                            // im using removeif because .remove() is overloaded, index or object and our object is an int so testing is needed
                            this.PLAYER2.pieceList.removeIf(p -> p.equals(leavingSquare)); 
                            this.PLAYER2.pieceList.add(destination);
                        }
                        else {
                            this.PLAYER1.pieceList.removeIf(p -> p.equals(leavingSquare));
                            this.PLAYER1.pieceList.add(destination);
                        }
                
        this.endTurn();
    }

    private void movePieceCaslting(int leavingSquare, int dir) {
        this.GAMEBOARD.Squares.get(leavingSquare).Piece.setMoved();

        //This is work around for AI vs Player movePiece so AI dont shit his pants
        if (this.currentPlayer.isHolding()) {this.currentPlayer.dropPiece();}
        
        //first we move our king to his new square
        this.GAMEBOARD.Squares.get(leavingSquare + dir).Piece = this.GAMEBOARD.Squares.get(leavingSquare).Piece;
        this.GAMEBOARD.Squares.get(leavingSquare).Piece = new Piece(); //creates a new "NONE" piece
        
        //then we move our rook to their new square.
        if (dir > 0) {
            this.GAMEBOARD.Squares.get(leavingSquare + 1).Piece = this.GAMEBOARD.Squares.get(leavingSquare + 3).Piece;
            this.GAMEBOARD.Squares.get(leavingSquare + 3).Piece = new Piece();
        }
        else {
            this.GAMEBOARD.Squares.get(leavingSquare - 2).Piece = this.GAMEBOARD.Squares.get(leavingSquare - 4).Piece;
            this.GAMEBOARD.Squares.get(leavingSquare - 4).Piece = new Piece();
        }
        
                    String c = this.currentPlayer.color;
                    if (c == "B") {
                        // im using removeif because .remove() is overloaded, index or object and our object is an int so testing is needed
                        //king
                        this.PLAYER2.pieceList.removeIf(p -> p.equals(leavingSquare)); 
                        this.PLAYER2.pieceList.add(leavingSquare + dir);
                        if (dir > 0) { //rook
                            this.PLAYER2.pieceList.removeIf(p -> p.equals(leavingSquare + 3)); 
                            this.PLAYER2.pieceList.add(leavingSquare + 1);
                        }
                        else {
                            this.PLAYER2.pieceList.removeIf(p -> p.equals(leavingSquare - 4)); 
                            this.PLAYER2.pieceList.add(leavingSquare - 2);
                        }
                    }
                    else {
                        //king
                        this.PLAYER1.pieceList.removeIf(p -> p.equals(leavingSquare)); 
                        this.PLAYER1.pieceList.add(leavingSquare + dir);
                        //rook
                        if (dir > 0) {
                            this.PLAYER1.pieceList.removeIf(p -> p.equals(leavingSquare + 3)); 
                            this.PLAYER1.pieceList.add(leavingSquare + 1);
                        }
                        else {
                            this.PLAYER1.pieceList.removeIf(p -> p.equals(leavingSquare - 4)); 
                            this.PLAYER1.pieceList.add(leavingSquare - 2);
                        }
                    }
        
        this.endTurn();
    }

    private void capturePiece(int attackingSquare, int capturedSquare) {
        if (this.GAMEBOARD.Squares.get(this.currentPlayer.getHeld()).Piece.onSpot) {this.GAMEBOARD.Squares.get(this.currentPlayer.getHeld()).Piece.setMoved();}

        //This is work around for AI vs Player movePiece so AI dont shit his pants
        if (this.currentPlayer.isHolding()) {this.currentPlayer.dropPiece();}

        this.GAMEBOARD.Squares.get(capturedSquare).Piece = this.GAMEBOARD.Squares.get(attackingSquare).Piece;
        this.GAMEBOARD.Squares.get(attackingSquare).Piece = new Piece();
        
                    String c = this.currentPlayer.color;
                    if (c == "B") {
                        // im using removeif because .remove() is overloaded, index or object and our object is an int so testing is needed
                        //king
                        this.PLAYER2.pieceList.removeIf(p -> p.equals(attackingSquare)); 
                        this.PLAYER2.pieceList.add(capturedSquare);
                        this.PLAYER1.pieceList.removeIf(p -> p.equals(capturedSquare));
                    }
                    else {
                        this.PLAYER1.pieceList.removeIf(p -> p.equals(attackingSquare)); 
                        this.PLAYER1.pieceList.add(capturedSquare);
                        this.PLAYER2.pieceList.removeIf(p -> p.equals(capturedSquare));
                    }
        
        this.endTurn();
    }
    
    //Right now we need this because handleClick() sorts between empty squares and occupied squares for moves/ attacks
    //and en passant attacks by moving to an empty square so yeah
    private void capturePieceEnPassant(int attackingSquare, int capturedSquare) {
        if (this.GAMEBOARD.Squares.get(this.currentPlayer.getHeld()).Piece.onSpot) {this.GAMEBOARD.Squares.get(this.currentPlayer.getHeld()).Piece.setMoved();}

        //This is work around for AI vs Player movePiece so AI dont shit his pants
        if (this.currentPlayer.isHolding()) {this.currentPlayer.dropPiece();}

        this.GAMEBOARD.Squares.get(capturedSquare).Piece = this.GAMEBOARD.Squares.get(attackingSquare).Piece;
        this.GAMEBOARD.Squares.get(attackingSquare).Piece = new Piece();
        this.GAMEBOARD.Squares.get(this.enPassantPiece).Piece = new Piece();
        
                    String c = this.currentPlayer.color;
                    if (c == "B") {
                        // im using removeif because .remove() is overloaded, index or object and our object is an int so testing is needed
                        //king
                        this.PLAYER2.pieceList.removeIf(p -> p.equals(attackingSquare)); 
                        this.PLAYER2.pieceList.add(capturedSquare);
                        this.PLAYER1.pieceList.removeIf(p -> p.equals(this.enPassantPiece));
                    }
                    else {
                        this.PLAYER1.pieceList.removeIf(p -> p.equals(attackingSquare)); 
                        this.PLAYER1.pieceList.add(capturedSquare);
                        this.PLAYER2.pieceList.removeIf(p -> p.equals(this.enPassantPiece));
                    }
        
        this.endTurn();
    }

    /*
        This swaps between white and black moves, and clears the en passant 
        square / piece unless it was just set by the player whose turn it is.
    */
    
    private void endTurn() {
        if ("W".equals(this.currentPlayer.color)) {

            this.currentPlayer = this.PLAYER2;
            if (this.enPassantPiece >=0) {
                if (this.GAMEBOARD.Squares.get(this.enPassantPiece).Piece.color == "B") {
                    this.enPassantPiece = -1;
                    this.enPassantSquare = -1;
                }
           }
        this.getCheckSquares();
        System.out.println(this.currentPlayer.color + " bad moves"); 
        for (int s : this.illegalSquaresBlackKing) {
            System.out.println(s);
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
            this.getCheckSquares();
            
            System.out.println(this.currentPlayer.color + " bad moves"); 
            for (int s : this.illegalSquaresWhiteKing) {
                System.out.println(s);
            }
        }
    }
    
    public void getCheckSquares() {
        //for player black
        if (this.currentPlayer == this.PLAYER2) {
            this.illegalSquaresBlackKing.clear();
            for (int p : this.PLAYER1.pieceList) {
                if (this.GAMEBOARD.Squares.get(p).Piece.type != Piece.Type.PAWN) {
                    List<Integer> pMoves = this.getLegalMoves(p);
                    for (int s : pMoves) {
                        this.illegalSquaresBlackKing.add(p + s);
                    }
                }
                else {
                    List<Integer> mooves = new ArrayList<>();
                    Piece cocksucker = this.GAMEBOARD.Squares.get(p).Piece;
                    int[] pawnAttacks = cocksucker.getPawnAttacks();
                    for (int i = 0; i < pawnAttacks.length; i++) {
                        int move = pawnAttacks[i] * -1;
                        //if the square we are checking for moving to exists
                        if (this.GAMEBOARD.FLAT_EARTH.contains(p + move)) {
                            mooves.add(move);
                        }
                    }
                    for (int s : mooves) {
                        this.illegalSquaresBlackKing.add(p + s);
                    }
                }
            }
        }
        //for player white
        else {
            this.illegalSquaresWhiteKing.clear();
            for (int p : this.PLAYER2.pieceList) {
                if (this.GAMEBOARD.Squares.get(p).Piece.type != Piece.Type.PAWN) {
                    List<Integer> pMoves = this.getLegalMoves(p);
                    for (int s : pMoves) {
                        this.illegalSquaresWhiteKing.add(p + s);
                    }
                }
                else {
                    List<Integer> mooves = new ArrayList<>();
                    Piece cocksucker = this.GAMEBOARD.Squares.get(p).Piece;
                    int[] pawnAttacks = cocksucker.getPawnAttacks();
                    for (int i = 0; i < pawnAttacks.length; i++) {
                        int move = pawnAttacks[i];
                        //if the square we are checking for moving to exists
                        if (this.GAMEBOARD.FLAT_EARTH.contains(p + move)) {
                            mooves.add(pawnAttacks[i]);
                        }
                    }
                    for (int s : mooves) {
                        this.illegalSquaresWhiteKing.add(p + s);
                    }
                }
            }
        }
    }
    
    public List<Integer> getLegalMoves(int squareID) {
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
                    default:    System.out.println("Something fucky wucky Controller line ~366");
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
                //if the square we are checking for moving to exists
                if (this.GAMEBOARD.FLAT_EARTH.contains(squareID + move)) {
                    //if square is occupied by an enemy piece OR it is the enpassant square for an enemy piece
                    if ((this.GAMEBOARD.Squares.get(squareID + move).isOccupied() && 
                        this.GAMEBOARD.Squares.get(squareID + move).Piece.color != p.color) ||
                        ((squareID + move) == this.enPassantSquare) && this.GAMEBOARD.Squares.get(squareID + move).Piece.color != p.color) {
                        pawnMoves.add(pawnAttacks[i] * colorModifier);
                    }
                }
            }
            
            return pawnMoves;
//            Iterator itPawnMoves = pawnMoves.iterator();
//            while(itPawnMoves.hasNext()) {
//                int m = (int)itPawnMoves.next();
//                this.legalMoves.add(m);
//            }
        }
        /*
            KING -~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~
        */
        else if (p.type == Piece.Type.KING) {
            List<Integer> kingMoves = new ArrayList<Integer>();
            
            for (int i = 0; i < p.moves.length; i++) {
                if (this.GAMEBOARD.FLAT_EARTH.contains(squareID + p.moves[i]) && !this.kingMovesChecker(this.currentPlayer.color, squareID + p.moves[i]) &&
                        (!this.GAMEBOARD.Squares.get(squareID + p.moves[i]).isOccupied() ||
                            this.GAMEBOARD.Squares.get(squareID + p.moves[i]).Piece.color != p.color)) {
                    kingMoves.add(p.moves[i]);
                }
                if (this.GAMEBOARD.FLAT_EARTH.contains(squareID + (p.moves[i] * -1)) && !this.kingMovesChecker(this.currentPlayer.color, squareID + (p.moves[i] * -1)) &&
                        (!this.GAMEBOARD.Squares.get(squareID + (p.moves[i] * -1)).isOccupied() ||
                            this.GAMEBOARD.Squares.get(squareID + (p.moves[i] * -1)).Piece.color != p.color)) {
                    kingMoves.add(p.moves[i] * -1);
                }
            }
            
            /*
                check for legality of castling here.
            */
            
            if (p.onSpot) {
                if (this.GAMEBOARD.Squares.get(squareID + 3).Piece.onSpot && 
                        (!this.GAMEBOARD.Squares.get(squareID + 1).isOccupied() && !this.GAMEBOARD.Squares.get(squareID + 2).isOccupied())
                            && !this.kingMovesChecker(this.currentPlayer.color, squareID + 2)) {
                    kingMoves.add(p.getCastlingMoves()[0]);
                }
                if (this.GAMEBOARD.Squares.get(squareID - 4).Piece.onSpot && 
                        (!this.GAMEBOARD.Squares.get(squareID - 1).isOccupied() && !this.GAMEBOARD.Squares.get(squareID - 2).isOccupied() && !this.GAMEBOARD.Squares.get(squareID - 3).isOccupied())
                            && !this.kingMovesChecker(this.currentPlayer.color, squareID - 3)) {
                    kingMoves.add(p.getCastlingMoves()[1]);
                }
            }

            return kingMoves;
//            Iterator itKingMoves = kingMoves.iterator();
//            while(itKingMoves.hasNext()) {
//                int m = (int)itKingMoves.next();
//                this.legalMoves.add(m);
//            }
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
            
            return knightMoves;
//            Iterator itkNightMoves = knightMoves.iterator();
//            while(itkNightMoves.hasNext()) {
//                int m = (int)itkNightMoves.next();
//                this.legalMoves.add(m);
//            }
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
            
            return pieceMoves;
//            Iterator itPieceMoves = pieceMoves.iterator();
//            while(itPieceMoves.hasNext()) {
//                int m = (int)itPieceMoves.next();
//                this.legalMoves.add(m);
//            }
        }
    }

    private boolean kingMovesChecker(String color, int squareToCheck) {
        if (color == "W") {
            return this.illegalSquaresWhiteKing.contains(squareToCheck);
        }
        else {
            return this.illegalSquaresBlackKing.contains(squareToCheck);
        }
    }
    
    public int moveAI(int squareID){
      this.legalMoves = getLegalMoves(squareID);
      movePiece(squareID,legalMoves.get(legalMoves.size()-1)+squareID);
      return (squareID + legalMoves.get(legalMoves.size()-1));
    }
}
