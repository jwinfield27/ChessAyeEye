package ChessAyeEye;

public class Main {


    public static void main(String[] args) {
        Board board = new Board();
        Controller controller = new Controller(board);
        AI testAI = new AI();
        System.out.println("\n Test completed at: " + System.currentTimeMillis());
    }
}
