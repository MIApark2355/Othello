package Othello;


import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.Map;

public class Records extends JPanel {

    private JFrame records = new JFrame("records");

    public Records(LinkedList<String> nameRank, Map<String, Integer> userScores) {

        records.setFocusable(true);
        records.setSize(320, 500);

        final JPanel showRecords = new JPanel();
        showRecords.setBounds(10,50,200,400);
        showRecords.setBackground(Color.white);
        records.add(showRecords,BorderLayout.CENTER);
        showRecords.setLayout(new GridLayout(10,1));

        //if there is no record (no one played yet)
        if (nameRank.size() == 0) {
            showRecords.add(
                    new JLabel("No records"));
        }

        //make sure that record only shows top 10
        for (int i = 0; (i < nameRank.size()) && (i < 10); i++) {
            int rankNum = i + 1;
            showRecords.add(
                    new JLabel(String.valueOf(rankNum) +
                            " : " + nameRank.get(i) + " ->  " +
                            //getting a specific player's name from TreeMap userScores
                            userScores.get(nameRank.get(i)))
            );
        }

        records.setVisible(true);
        records.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}