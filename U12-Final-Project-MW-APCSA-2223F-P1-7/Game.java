import java.io.*;
import javax.swing.JFrame;
import java.awt.Component;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Canvas;
import java.awt.event.*;
import static java.lang.Character.*;
import java.awt.image.BufferedImage;
import java.awt.event.ActionListener;
import java.util.*;
import java.net.URL;
import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.Font;
import javax.swing.*;
/*
import java.awt.*;
import javax.swing.*;
*/
public class Game extends JFrame //implements KeyListener
  {
    //Final variables for width and height, should be 800 & 600
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    //private boolean[] keys;
    public static String[] arguments = null;
    private static JLabel finText;

    ArrayList<JButton> buttonList; 
    JLabel tutorial; 
    JPanel panel;

    public Game(String[]args)
    {
      //Setting  size and name
      super("PLATFORMER"); 
      this.setSize(WIDTH,HEIGHT);
      this.setLocationRelativeTo(null);
      this.setVisible(true);
      
        
        // sets up buttons for starting the game
      buttonList = new ArrayList<JButton>();
      JButton btn1 = new JButton("Level 1"), btn2 = new JButton("Level 2"), btn3 = new JButton("Level 3");
      buttonList.add(btn1);
      buttonList.add(btn2);
      buttonList.add(btn3);
      
      buttonList.get(0).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt)             {
                startGame(1);
            }
        });
        
      
        buttonList.get(1).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt)             {
                startGame(2);
            }
        });
        buttonList.get(2).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt)             {
                startGame(3);
            }
        });
      
  
        // tutorial for the game
        tutorial = new JLabel("<html><p style='color: blue;'>Welcome to Platformer Project! Eliminate all the enemies and touch the flag to beat the level.<br>The controls are [a] to move left; [d] to move right; [w] to jump; [e] to shoot;[r] to restart.<p><html>");

        // sets up GUI
      setUpGUI();
    }

    public void setUpGUI()
    {
       panel = new JPanel();
        for(int i=0; i<buttonList.size(); i++)
          {
            panel.add(buttonList.get(i));
          }
        panel.add(tutorial);
        if(finText != null)
          panel.add(finText);
        this.add(panel); 
    }
    
    
    public static void print(String s,String c)
    {
        finText = new JLabel("<html><p style='color: "+c+";'>"+s+"<p><html>");
    }
    // starts the game
    private void startGame(int i) {
        Platformer game = new Platformer(i);
        ((Component)game).setFocusable(true);
        getContentPane().removeAll();
        getContentPane().add(game);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    //Main method, makes a game object
    public static void main(String[] args)
    {
      arguments = args;
      Game run = new Game(arguments);
    }
  }

