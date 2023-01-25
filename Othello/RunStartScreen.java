package Othello;

import java.awt.*;
import java.io.File;
import javax.swing.*;

public class RunStartScreen implements Runnable {
    //file for loading game and saving game state
    static final String PATH_TO_SAVE_MOVES = "files/saved_moves.txt";

    @Override
    public void run() {

        //Menu window that first shows up
        final JFrame frame = new JFrame("Othello Menu");
        frame.setVisible(true);
        frame.getContentPane().setBackground(new Color(0,100,0));
        frame.setLocation(100, 100);
        frame.setSize(500,500);
        frame.setLayout(null);
        frame.setResizable(false);

        //Game title
        final JLabel title = new JLabel("OTHELLO");
        title.setFont(new Font("Monospaced",Font.BOLD,60));
        title.setForeground(Color.white);
        title.setBounds(120, 60, 400,60);
        frame.add(title);

        //Instruction button
        final JButton instruction = new JButton("HOW TO PLAY");
        instruction.setFont(new Font("SansSerif",Font.BOLD,13));
        instruction.setForeground(Color.black);
        instruction.setBackground(new Color(255,255,204));
        instruction.addActionListener(e -> new Instruction());
        instruction.setBounds(150, 130, 200,40);
        frame.add(instruction);

        //Label
        final JLabel text0 = new JLabel("Enter your name!");
        text0.setForeground(Color.PINK);
        text0.setFont(new Font("SansSerif",Font.BOLD,20));
        text0.setBounds(170, 205, 200,20);
        frame.add(text0);

        final JLabel text1 = new JLabel("Player 1 :");
        text1.setForeground(Color.PINK);
        text1.setFont(new Font("SansSerif",Font.BOLD,14));
        text1.setBounds(80, 230, 80,20);
        frame.add(text1);

        final JLabel text2 = new JLabel("Player 2 :");
        text2.setForeground(Color.PINK);
        text2.setFont(new Font("SansSerif",Font.BOLD,14));
        text2.setBounds(80, 255, 80,20);
        frame.add(text2);

        //boxes that players should type their name in
        final JPanel inputNamePanel = new JPanel();
        inputNamePanel.setBounds(150, 230,200,50);
        GridLayout layout = new GridLayout(2,1);
        layout.setHgap(25);
        inputNamePanel.setLayout(layout);
        final JTextField player1 = new JTextField();
        player1.setFont(new Font("SansSerif",Font.BOLD,14));
        player1.setBackground(Color.PINK);
        final JTextField player2 = new JTextField();
        player2.setFont(new Font("SansSerif",Font.BOLD,14));
        player2.setBackground(Color.PINK);
        inputNamePanel.add(player1);
        inputNamePanel.add(player2);
        frame.add(inputNamePanel);

        //Button to start a new game
        final JButton start = new JButton("START");
        start.setFont(new Font("SansSerif",Font.BOLD,13));
        start.setForeground(Color.black);
        start.setBackground(new Color(255,255,204));
        start.addActionListener(e -> {
            String playerName1 = player1.getText().toUpperCase();
            String playerName2 = player2.getText().toUpperCase();
            if (playerName1 == null ||
                    playerName2 == null ||
                    playerName1.isEmpty() ||
                    playerName2.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Enter name!");
            } else if (playerName1.equals(playerName2)) {
                JOptionPane.showMessageDialog(frame, "Do not enter the same name");
            } else {
                new OthelloScreen(playerName1, playerName2, true);
                frame.setVisible(false);
            }
        });
        start.setBounds(150, 280, 200,35);
        frame.add(start);

        //Button to load game
        final JButton loadGame = new JButton("LOAD GAME");
        loadGame.setFont(new Font("SansSerif",Font.BOLD,13));
        loadGame.setForeground(Color.black);
        loadGame.setBackground(new Color(255,255,204));
        loadGame.setBounds(150, 360, 200,40);
        loadGame.addActionListener(e -> {
            if (!((new File(PATH_TO_SAVE_MOVES).exists()))) {
                JOptionPane.showMessageDialog(frame,
                        "There are no saved games to load. Start a new game!");
            } else {
                frame.setVisible(false);
                new OthelloScreen("", "", false);
            }
        });
        frame.add(loadGame);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}