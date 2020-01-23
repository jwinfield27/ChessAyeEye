package ChessAyeEye;

/*
Each square on the chessboard knows its color and position, and knows what piece
is currently on it if any.
*/

public class Square {
    public String color, rankfile;
    public Piece Piece;
    
    public Square(String color, String rankfile, Piece piece) {
        this.color = color;
        this.rankfile = rankfile;
        this.Piece = piece;
    } 
    
    
    public boolean isOccupied() {
        if (this.Piece.name != "NONE") {
            return true;
        }
        else return false;
    }
}
