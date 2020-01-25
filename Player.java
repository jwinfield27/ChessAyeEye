package ChessAyeEye;

public class Player {
    public String color = "W";
    
    private int heldPiece = -1;
    private boolean holdingPiece = false;
    
    public Player(String color){
        this.color = color;
    }

    public void grabPiece(int piece) {
        this.heldPiece = piece;
        this.holdingPiece = true;
    }
    
    public void dropPiece() {
        this.heldPiece = -1;
        this.holdingPiece = false;
    }
    
    public int getHeld() {
        return this.heldPiece;
    }
    
    public boolean isHolding() {
        return this.holdingPiece;
    }
}
