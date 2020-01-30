package ChessAyeEye;

/*
Each square on the chessboard knows its color and position, and knows what piece
is currently on it if any.
*/

public class Square {
    public String squareColor, rankfile;
    public Piece Piece;
    
    public Square(String color, String rankfile, Piece piece) {
        this.squareColor = color;
        this.rankfile = rankfile; //I think this should be KB1, KB2 (Kings bishop 1, 2)
                                  //https://en.wikipedia.org/wiki/Descriptive_notation
        this.Piece = piece;
    } 
    
    
    public boolean isOccupied() {
        if (this.Piece.name != "NONE") {
            return true;
        }
        else return false;
    }
}
