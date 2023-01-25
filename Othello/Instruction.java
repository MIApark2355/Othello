package Othello;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;


public class Instruction extends JPanel {

    //instruction window
    final JFrame frame = new JFrame("HOW TO PLAY");

    //using an image file
    public static final String IMG_FILE = "files/instructions.png";
    private static BufferedImage img;

    public Instruction() {

        // visible when the object is made
        frame.setVisible(true);

        try {
            if (img == null) {
                img = ImageIO.read(new File(IMG_FILE));
            }
            frame.setContentPane(new JPanel() {
                @Override
                public void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(img,0,0, 636,522,this);
                }
            });
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocation(150, 150);
        frame.setSize(650, 550);
        frame.setLayout(null);

        //Button to go back to previous screen (menu screen or othello screen)
        final JButton backMenu = new JButton("BACK");
        backMenu.setFont(new Font("SansSerif", Font.BOLD, 13));
        backMenu.setForeground(Color.black);
        backMenu.setBackground(new Color(0,170,0));
        backMenu.addActionListener(e -> frame.setVisible(false));
        backMenu.setBounds(10, 10, 170, 30);
        frame.add(backMenu);
    }
}
