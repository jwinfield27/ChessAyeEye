package ChessAyeEye;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Controller {
    public static Board GAMEBOARD; //this is THE board that is actually being played on.
    public static Boolean whiteFirstTurn = True;
    public static Boolean blackFirstTurn = True;
    public String color = "W";

    private int heldPiece = -1;
    private boolean holdingPiece = false;
//    List<Board> possibleMoves = new ArrayList<>();

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

    public void handleClick(int squareID) {
        //if we are holding a piece
        if (this.holdingPiece) {

//            System.out.println("HOLDING: " + this.heldPiece);

            //If we click on the square where the piece started
            if (this.heldPiece == squareID) {
                this.dropPiece();
            }

            //If we click on an empty square
            else if (this.GAMEBOARD.Squares.get(squareID).piece.name == "NONE") {

                //get distance to click
                int distance = Math.abs(this.heldPiece - squareID);
                int[] moves = this.GAMEBOARD.Squares.get(this.heldPiece).piece.moves;

                //if the distance is in the move list then it can move
                for (int element : this.GAMEBOARD.Squares.get(this.heldPiece).piece.moves) {
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
                    int distance = Math.abs(this.heldPiece - squareID);
                    int[] moves = this.GAMEBOARD.Squares.get(this.heldPiece).piece.moves;

                    //if the distance is in the move list then it can move
                    for (int element : this.GAMEBOARD.Squares.get(this.heldPiece).piece.moves) {
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

    protected void movePiece(int leavingSquare, int destination) {
        this.dropPiece();
        this.GAMEBOARD.Squares.get(destination).piece = this.GAMEBOARD.Squares.get(leavingSquare).piece;
        this.GAMEBOARD.Squares.get(leavingSquare).piece = new Piece(); //creates a new "NONE" piece
    }

    protected void capturePiece(int attackingSquare, int capturedSquare) {
        this.dropPiece();
        this.GAMEBOARD.Squares.get(capturedSquare).piece = this.GAMEBOARD.Squares.get(attackingSquare).piece;
        this.GAMEBOARD.Squares.get(attackingSquare).piece = new Piece();
    }
}
