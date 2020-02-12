package ChessAyeEye;

import java.util.ArrayList;
import java.util.List;

public class Player {
    public String color = "W";
    
    private int heldPiece = -1;
    private boolean holdingPiece = false;
    public List<Integer> pieceList  = new ArrayList<>(16);
    
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
