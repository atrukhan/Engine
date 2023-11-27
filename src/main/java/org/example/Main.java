package org.example;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;


public class Main {

    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static final  Dimension size = new Dimension(1553, 881);

    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame();
        Screen screen = new Screen(screenSize);
        frame.addKeyListener(screen);
        frame.add(screen);
        frame.setUndecorated(true);
        frame.setSize(screenSize);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

}