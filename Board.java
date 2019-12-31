package ChessAyeEye;

import java.util.Arrays;

/*
This class keeps track of:
    Board state
    setting up the board
        
TODO(?):
    - probably has several utilities to determine: legal moves, check, draw, 
    checkmate, ???
    - records the games? https://en.wikipedia.org/wiki/Chess_notation
*/

public class Board {
    public Square[][] Squares = new Square[8][8];
        
    public Board() { 
        this.newBoard();
        this.printBoard(false);
    }
    
            
    //puts a square in each spot in the array with the correct color and rank and file.
    //add pieces to the board somewhere else me thinks?
    private void newBoard() {  
    /* 
        Starting from top left (white square) fill the board with squares and
        set their rankfile (a-h)x(1-8) according to 
        https://en.wikipedia.org/wiki/Rules_of_chess 
        
        0,0     1,0     2,0     3,0     4,0     5,0     6,0     7,0      
    
        0,1     1,1     2,1     3,1     4,1     5,1     6,1     7,1      
    
        0,2     1,2     2,2     3,2     4,2     5,2     6,2     7,2      
    
        0,3     1,3     2,3     3,3     4,3     5,3     6,3     7,3      
    
        0,4     1,4     2,4     3,4     4,4     5,4     6,4     7,4      
    
        0,5     1,5     2,5     3,5     4,5     5,5     6,5     7,5      
    
        0,6     1,6     2,6     3,6     4,6     5,6     6,6     7,6      
    
        0,7     1,7     2,7     3,7     4,7     5,7     6,7     7,7 

    */
        
        String squareColor = "W";
        int rank = 8;
        String file = "a";

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                this.Squares[i][j] = new Square(squareColor, (file + Integer.toString(rank)), new Piece());
                squareColor = swapSquareColor(squareColor);
                rank--;
            }
            squareColor = swapSquareColor(squareColor); //have to swap twice before each column
            rank = 8;
            
            switch (file) {
                case "a":   file = "b";
                            break;
                case "b":   file = "c";
                            break;
                case "c":   file = "d";
                            break;
                case "d":   file = "e";
                            break;
                case "e":   file = "f";
                            break;
                case "f":   file = "g";
                            break;
                case "g":   file = "h";
                            break;
                case "h":   file = "a";
                            break;
                default:    System.out.println("o shit waddup, its dat default. (newBoard switch)");
            }
        }
    }
    
    //prints out the board left to right top to bottom, either showing square
    //colors (W or B) or with the rank and file
    private void printBoard(boolean printColors) {
        if (printColors) {
            String rank = "";
            for (int j = 0; j< 8; j++) {
                for (int i = 0; i < 8; i++) {
                    rank += (" " + this.Squares[i][j].color);
                }
                System.out.println(rank);
                rank = "";
            }
        }
        else {
            String rank = "";
            for (int j = 0; j< 8; j++) {
                for (int i = 0; i < 8; i++) {
                    rank += (" " + this.Squares[i][j].rankfile);
                }
                System.out.println(rank);
                rank = "";
            }
        }
    }
    
    //swaps the square color
    private String swapSquareColor(String color) {
        if (color == "W") {
            return "B";
        }
        else return "W";
    }
}
