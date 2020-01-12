/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ChessAyeEye;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 *
 * @author nathan
 */
public class Chess extends javax.swing.JFrame {

    /*
    I think that this class should just be for interacting with the window and
    that Controller should do everything that isnt drawing the board and pieces.
    
    This way the player and AI go back and forth editing the board and this
    class can worry about the window and clicks etc.
    
    Im open to debate on this, idk if the separation is smart or if this makes
    things more complicated?
    */
//    public Board board = new Board();
    public Controller player;
    public Controller AI;
    
    private final int BOARD_SIZE = 600;
    private final int SQUARE_SIZE = 75;
    private final String IMG_DIR = "/img";
    
    private Map<String, BufferedImage> pieceIMG = new HashMap<String, BufferedImage>();
    
    int PANEL_ORIGIN_X;
    int PANEL_ORIGIN_Y;
    
    Graphics bufferGraphics;
    Image buffer2;
    
    int mouseX = 0;
    int mouseY = 0;
    /**
     * Creates new form Cheese
     */
    public Chess() {
        initComponents();
        
        this.player = new Controller();
        this.AI = new Controller(this.player.GAMEBOARD);
        
        /*
        Instead of images we could use a text field for each square on the 
        chessboard which would make the icons look not dogshit, but 64 seperate
        text fields seems cancerous
        */
        
        try { //loads all our images so they can be displayed
            
            /*
            MANUAL
            LOADS FROM FOLDER 'img' IN THE SAM DIRECTORY AS CHESSAYEEYE.JAR
            */
                    
//                    File dir = new File("img");
//                    File[] images = dir.listFiles();
//                    if (images != null) {
//                        for (File f : images) {
//                            pieceIMG.put(f.getName().replace(".png", ""), ImageIO.read(f));
//                        }
//                    }

            /*
            IDE
            LOADS FROM RESOURCE FOLDER (resource folder at same level as 'java' in src
            https://i.imgur.com/mFp8jCd.png
            */

//                    String path = Chess.class.getResource(IMG_DIR).getPath();
//                    File[] files = new File(path).listFiles();
//
//                    for (File f : files) {
//                        File temp = new File(Chess.class.getResource(IMG_DIR + File.separatorChar + f.getName()).toString());
//                        System.out.println(temp.toString());
//                        pieceIMG.put(f.getName().replace(".png", ""), ImageIO.read(temp));
//                    } 
                    
            pieceIMG.put("PAWNW", ImageIO.read(Chess.class.getResource("/img/PAWNW.png")));
            pieceIMG.put("PAWNB", ImageIO.read(Chess.class.getResource("/img/PAWNB.png")));
            pieceIMG.put("KINGW", ImageIO.read(Chess.class.getResource("/img/KINGW.png")));
            pieceIMG.put("KINGB", ImageIO.read(Chess.class.getResource("/img/KINGB.png")));
            pieceIMG.put("QUEENW", ImageIO.read(Chess.class.getResource("/img/QUEENW.png")));
            pieceIMG.put("QUEENB", ImageIO.read(Chess.class.getResource("/img/QUEENB.png")));
            pieceIMG.put("BISHOPW", ImageIO.read(Chess.class.getResource("/img/BISHOPW.png")));
            pieceIMG.put("BISHOPB", ImageIO.read(Chess.class.getResource("/img/BISHOPB.png")));
            pieceIMG.put("KNIGHTW", ImageIO.read(Chess.class.getResource("/img/KNIGHTW.png")));
            pieceIMG.put("KNIGHTB", ImageIO.read(Chess.class.getResource("/img/KNIGHTB.png"))); 
            pieceIMG.put("ROOKW", ImageIO.read(Chess.class.getResource("/img/ROOKW.png")));
            pieceIMG.put("ROOKB", ImageIO.read(Chess.class.getResource("/img/ROOKB.png")));
            
            
        } catch (IOException e) {
            System.out.println(e);
        }
        
        
        /*
        This needs changed to work for any os unless by some miracle its perfect for your machine too. 
        the rootPane.getParent().getSize() seems to be the host OS window including the window title bar
        but just how much of the Frame/ top margin is eaten up by the window bar is impossible to calculate
        and shall forever remain an enigma
        */

        this.PANEL_ORIGIN_X = 20; //on my machine the horizontal size of the window appears to be 650 (panel+margins+10) for the window, 5 pixels on either side
        this.PANEL_ORIGIN_Y = this.rootPane.getParent().getSize().height - 620;
//        this.PANEL_ORIGIN_Y = 73; //on my machine window height is 693p = 640 margins + 30 window title bar + 23 menu bar
//        this.PANEL_ORIGIN_Y = 51;
//        System.out.println(this.rootPane.getParent().getSize().height);

        buffer2 = createImage(this.rootPane.getWidth()+30, this.rootPane.getHeight()+30);
        bufferGraphics = buffer2.getGraphics();
    }
 
    @Override
    public void paint(Graphics g) {
        super.paintComponents(bufferGraphics);
        
        bufferGraphics.clearRect(this.PANEL_ORIGIN_X,this.PANEL_ORIGIN_Y,this.BOARD_SIZE,this.BOARD_SIZE);
      
        List<Integer> pieces = new ArrayList<Integer>();
        
        //Board
        //************************************************************************************************
        
        for (int i = 0; i < 64; i++) {
            int x = 0;
            int y = 0;
            int mentalStabilityCount = 0;
            for (Map.Entry<Integer, Square> e : this.player.GAMEBOARD.Squares.entrySet()) {
                if (e.getValue().color == "W") {
                    bufferGraphics.setColor(Color.WHITE);
                }
                else {
                    bufferGraphics.setColor(Color.GRAY);
                }
                
                bufferGraphics.fillRect(x + this.PANEL_ORIGIN_X, y + this.PANEL_ORIGIN_Y, SQUARE_SIZE, SQUARE_SIZE);
                
                if (e.getValue().piece.name != "NONE") {
                    pieces.add(e.getKey());
                }
                
                mentalStabilityCount += 1;
                x += SQUARE_SIZE;
                
                if (mentalStabilityCount%8 == 0){
                    y += SQUARE_SIZE;
                    x = 0;
                }
            }
        }
        
        //Pieces
        //***********************************************************************************************
        Iterator it = pieces.iterator();
        while(it.hasNext()) {
            int key = (int)it.next();
            Square s = this.player.GAMEBOARD.Squares.get(key);
            
            //if we holding a piece then we can have it float all cool like :D
            if (key == this.player.getHeld()) {
                bufferGraphics.drawImage(pieceIMG.get(s.piece.name + s.piece.color), this.mouseX + this.PANEL_ORIGIN_X - (this.SQUARE_SIZE/2), this.mouseY + this.PANEL_ORIGIN_Y - (this.SQUARE_SIZE/2), this.rootPane); 
            }
            else {
                bufferGraphics.drawImage(pieceIMG.get(s.piece.name + s.piece.color), squareToCoord(key)[0] + this.PANEL_ORIGIN_X, squareToCoord(key)[1] + this.PANEL_ORIGIN_Y, this.rootPane);
            } 
        }
        pieces.clear();
        g.drawImage(buffer2, 0, 0, this.rootPane);
    }
    
    @Override
    public void update(Graphics g) {
        paint(g);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelChessboard = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        menuFileNewGame = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ChessAyeEye");
        setBackground(new java.awt.Color(0, 0, 0));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setForeground(java.awt.Color.black);
        setMaximumSize(new java.awt.Dimension(1000, 1000));
        setMinimumSize(new java.awt.Dimension(640, 640));
        setName("ChessAyeEye"); // NOI18N
        setResizable(false);

        panelChessboard.setBackground(new java.awt.Color(0, 0, 0));
        panelChessboard.setForeground(new java.awt.Color(0, 0, 0));
        panelChessboard.setMaximumSize(new java.awt.Dimension(600, 600));
        panelChessboard.setMinimumSize(new java.awt.Dimension(600, 600));
        panelChessboard.setName("panelChessboard"); // NOI18N
        panelChessboard.setPreferredSize(new java.awt.Dimension(600, 600));
        panelChessboard.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                panelChessboardMouseMoved(evt);
            }
        });
        panelChessboard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelChessboardMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelChessboardLayout = new javax.swing.GroupLayout(panelChessboard);
        panelChessboard.setLayout(panelChessboardLayout);
        panelChessboardLayout.setHorizontalGroup(
            panelChessboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );
        panelChessboardLayout.setVerticalGroup(
            panelChessboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );

        jMenu1.setText("File");

        menuFileNewGame.setText("New Game");
        menuFileNewGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuFileNewGameActionPerformed(evt);
            }
        });
        jMenu1.add(menuFileNewGame);

        jMenuBar1.add(jMenu1);
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(panelChessboard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(panelChessboard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        getAccessibleContext().setAccessibleName("frameGame");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    //when we click on the panel we can do things
    private void panelChessboardMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelChessboardMouseClicked
        int squareKey = coordToSquare(this.mouseX, this.mouseY);
        this.mouseX = evt.getX();
        this.mouseY = evt.getY();
        this.player.handleClick(squareKey);
        repaint(); //this calls the paint() function again
        
    }//GEN-LAST:event_panelChessboardMouseClicked

    private void menuFileNewGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFileNewGameActionPerformed
        newGame();
    }//GEN-LAST:event_menuFileNewGameActionPerformed

    private void panelChessboardMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelChessboardMouseMoved
        this.mouseX = evt.getX();
        this.mouseY = evt.getY();
        repaint();
    }//GEN-LAST:event_panelChessboardMouseMoved

    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Chess.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Chess.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Chess.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Chess.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Chess().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem menuFileNewGame;
    private javax.swing.JPanel panelChessboard;
    // End of variables declaration//GEN-END:variables
 
    /*
    This function takes an x, y and returns the HashMap key for the square that
    was clicked on.
    this function DOES NOT need an adjustment from Frame x,y to Panel x,y
    because the MouseClicked event is on the panel itself, and gives us an x,y
    relative to the panel.
    */
    private int coordToSquare(int x, int y) {
        int scaledX = x / SQUARE_SIZE;
        int scaledY = y / SQUARE_SIZE;
        
        switch (scaledY) {
            case 0: return scaledX;
            case 1: return 16 + scaledX;
            case 2: return 32 + scaledX;
            case 3: return 48 + scaledX;
            case 4: return 64 + scaledX;
            case 5: return 80 + scaledX;
            case 6: return 96 + scaledX;
            case 7: return 112 + scaledX;
            default: System.out.println("o hell nah (coordToSquare switch)");
                break;
        }
        return -1;
    }
    
    /*
    This function takes a board.Squares key (id of a square) and returns the
    upper left corner of that square as an x,y pair for drawing purposes
    */
    private int[] squareToCoord(int s) {
        int x = (s%8) * SQUARE_SIZE;
        int y = 0;
        
        if (s < 16) {
            y = 0;
        }
        else if (s < 32) {
            y = 1 * SQUARE_SIZE;
        }
        else if (s < 48) {
            y = 2 * SQUARE_SIZE;
        }
        else if (s < 64) {
            y = 3 * SQUARE_SIZE;
        }
        else if (s < 80) {
            y = 4 * SQUARE_SIZE;
        }
        else if (s < 96) {
            y = 5 * SQUARE_SIZE;
        }
        else if (s < 112) {
            y = 6 * SQUARE_SIZE;
        }
        else {
            y = 7 * SQUARE_SIZE;
        }
        
        int[] coord = {x, y};
        return coord;
    }

    private void newGame() {
        this.player = new Controller();
        this.AI = new Controller(this.player.GAMEBOARD);
        repaint();
    }
}
