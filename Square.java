package ChessAyeEye;

/*
Each square on the chessboard knows its color and position, and knows what piece
is currently on it if any.
*/

public class Square {
    public String color, rankfile;
    public Piece piece;
    
    public Square(String color, String rankfile, Piece piece) {
        this.color = color;
        this.rankfile = rankfile;
        this.piece = piece;
    } 
}
