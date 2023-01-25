package Othello;

import java.awt.*;
import javax.swing.*;

public class OthelloScreen {
    //frame with game board, status labels and various buttons
    final JFrame frame = new JFrame("Othello");

    public OthelloScreen(String player1, String player2, Boolean isNewGame) {

        //othello game playing window
        frame.setLocation(100, 100);
        frame.setResizable(false);

        //current player panel
        final JPanel current_status = new JPanel();
        current_status.setBackground(new Color(207, 180, 204));
        frame.add(current_status, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Setting up...");
        status.setFont(new Font("SansSerif", Font.BOLD, 20));
        current_status.add(status);

        //score status panel
        final JPanel score = new JPanel();
        score.setBackground(new Color(207, 180, 204));
        final JLabel score1 = new JLabel("Player 1:" + "0");
        score1.setFont(new Font("SansSerif", Font.BOLD, 20));
        final JLabel score2 = new JLabel("Player 2:" + "0");
        score2.setFont(new Font("SansSerif", Font.BOLD, 20));
        score.add(score1);
        score.add(score2);

        // Game board
        final GameBoard board = new GameBoard(status, score1, score2, player1, player2, isNewGame);
        frame.add(board, BorderLayout.CENTER);

        //--------------------------------------------------

        final JPanel control_panel = new JPanel(); //restart, menu, instruction, records button

        // Restart button (scores not automatically stored but players not changed)
        final JButton btn1 = new JButton("RESTART");
        btn1.setBackground(new Color(70, 40, 0));
        btn1.setForeground(Color.white);
        //to restart game board status and othello game model
        btn1.addActionListener(e -> board.restart());

        //instruction button
        final JButton btn2 = new JButton("INSTRUCTION");
        btn2.setBackground(new Color(70, 40, 0));
        btn2.setForeground(Color.white);
        //Instruction frame shows up, with othello screen unchanged
        btn2.addActionListener(e -> new Instruction());


        // Menu button (score not stored and should input new players)
        final JButton btn3 = new JButton("MENU");
        btn3.setBackground(new Color(70, 40, 0));
        btn3.setForeground(Color.white);
        //to start game with new players
        btn3.addActionListener(e ->
                SwingUtilities.invokeLater(new RunStartScreen())
        );
        btn3.addActionListener(e -> frame.setVisible(false));

        //records: shows 10 highest scores
        final JButton btn4 = new JButton("RECORDS");
        btn4.addActionListener(e ->
                board.getRecords()
        );
        btn4.setBackground(new Color(70, 40, 0));
        btn4.setForeground(Color.white);

        control_panel.add(btn3);
        control_panel.add(btn1);
        control_panel.add(btn2);
        control_panel.add(btn4);

        //set layout
        control_panel.setLayout(new GridLayout(2, 3));

       //----------------------------------------------------------------

        final JPanel savePanel = new JPanel(); //save and quit buttons

        final JButton btn5 = new JButton("SAVE");
        //save current game state
        btn5.addActionListener(e ->
                board.save()
        );

        final JButton btn6 = new JButton("QUIT");
        btn6.addActionListener(e ->
                System.exit(0)
        );

        savePanel.add(btn5);
        savePanel.add(btn6);

        savePanel.setLayout(new GridLayout(2, 1));

        frame.add(savePanel, BorderLayout.WEST);

        //-----------------------------------------

        final JPanel top_panel = new JPanel();
        top_panel.add(control_panel);
        top_panel.add(score);
        top_panel.setLayout(new GridLayout(2, 1));

        frame.add(top_panel, BorderLayout.NORTH);

        //------------------------------------------

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start the game or load the game
        if (isNewGame) {
            board.restart();
        } else {
            board.load();
        }
    }
}
