package Othello;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameBoard extends JPanel {

    private Othello othello; //model for the game
    private JLabel status; // current status text
    private JLabel score1; // player 1 score
    private JLabel score2; // player 2 score
    private String player1; // player 1 name
    private String player2; // player 2 name


    //Game constants
    public static final int BOARD_WIDTH = 400;
    public static final int BOARD_HEIGHT = 400;

    private static final Color DARK_GREEN = new Color(0,102,0);

    //initializing the game board
    public GameBoard(JLabel statusInit, JLabel score1Init, JLabel score2Init,
                     String player1, String player2, Boolean isNewGame) {

        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setBackground(DARK_GREEN);
        setFocusable(true); //enable keyboard focus on the game board

        status = statusInit;
        score1 = score1Init;
        score2 = score2Init;

        othello = new Othello(player1, player2, isNewGame); //initializing model

        this.player1 = othello.getName1(); //getting player name from the model
        this.player2 = othello.getName2();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Point p = e.getPoint();

                othello.playTurn(p.x / 50, p.y / 50);

                othello.updateValidChoices();
                updateStatus();
                repaint();
                if (othello.skipped()) {
                    othello.updateValidChoices(); //update valid moves for the next player
                    updateStatus();
                    repaint();
                }
            }
        });
    }

    //Setting the game board to initial
    public void restart() {
        othello.restart(); //initializing the game state

        status.setText(player1 + "'s Turn");//player 1 starts first
        score1.setText("(B) " + player1 + " :  " + othello.getScore1());
        score2.setText("     |       " + "(W) " + player2 + " :  " + othello.getScore2());
        repaint();

        //keyboard and mouse focus
        requestFocusInWindow();
    }

    //loading game
    public void load() {
        if (othello.getCurrentPlayer() == 1) {
            status.setText(player1 + "'s Turn");
        } else {
            status.setText(player2 + "'s Turn");
        }
        score1.setText("(B) " + player1 + " :  " + othello.getScore1());
        score2.setText("     |       " + "(W) " + player2 + " :  " + othello.getScore2());
        repaint();

        //keyboard and mouse focus
        requestFocusInWindow();
    }

    //When 'save' button is pressed from othello screen
    public void save() {
        othello.save();
    }

    //When 'records' button is pressed from othello screen
    public void getRecords() {
        new Records(othello.getRank(), othello.getUserScores());
    }

    //Update status of othello screen after each turn
    private void updateStatus() {
        if (othello.getCurrentPlayer() == 1) {
            status.setText(player1 + "'s Turn");
        } else {
            status.setText(player2 + "'s Turn");
        }

        //update score of two players
        score1.setText("(B) " + player1 + " :  " + othello.getScore1());
        score2.setText("     |       " + "(W) " + player2 + " :  " + othello.getScore2());

        int winner = othello.checkWinner();
        //if game ended (1,2, or 3)
        if (winner != 0) {
            othello.endGame();
            if (winner == 1) {
                status.setText(player1 + " wins!!! " + "Winner's score is " + othello.getScore1());
            } else if (winner == 2) {
                status.setText(player2 + " wins!!! " + "Winner's score is " + othello.getScore2());
            } else if (winner == 3) {
                status.setText("It's a tie.");
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //Draws board grid
        g.drawLine(50, 0, 50, 400);
        g.drawLine(100, 0, 100, 400);
        g.drawLine(150, 0, 150, 400);
        g.drawLine(200, 0, 200, 400);
        g.drawLine(250, 0, 250, 400);
        g.drawLine(300, 0, 300, 400);
        g.drawLine(350, 0, 350, 400);
        g.drawLine(0, 50, 400, 50);
        g.drawLine(0, 100, 400, 100);
        g.drawLine(0, 150, 400, 150);
        g.drawLine(0, 200, 400, 200);
        g.drawLine(0, 250, 400, 250);
        g.drawLine(0, 300, 400, 300);
        g.drawLine(0, 350, 400, 350);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                boolean valid = othello.isValid(j,i);
                int state = othello.getCell(j, i);
                //Black is for player 1
                if (state == 1) {
                    g.setColor(Color.BLACK);
                    g.fillOval(5 + 50 * j, 5 + 50 * i, 40, 40);
                //White is for player 2
                } else if (state == 2) {
                    g.setColor(Color.WHITE);
                    g.fillOval(5 + 50 * j, 5 + 50 * i, 40, 40);
                }
                //shows valid moves with yellow boxes
                if (valid) {
                    g.setColor(Color.yellow);
                    g.fillRect(5 + 50 * j, 5 + 50 * i, 40, 40);
                }
            }
        }
    }
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}

