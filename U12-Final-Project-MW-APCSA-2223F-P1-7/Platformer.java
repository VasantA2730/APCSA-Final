import java.io.*;
import javax.swing.JFrame;
import java.awt.Component;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Canvas;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import static java.lang.Character.*;
import java.awt.image.BufferedImage;
import java.awt.event.ActionListener;
import java.util.*;
import java.net.URL;
import javax.imageio.ImageIO;
import java.awt.Image;

public class Platformer extends Canvas implements KeyListener, Runnable
  {
    //the game class will draw player hp, the jumps, and the jump highscore
    //it will also call draw in the level and the player
    //the class will allow the player to choose a level at the start of the game and build it from its preset
    //the class will also handle the reading/writing of jump highscore for a given level from a file where the data is stored

    //Variables
    //The level being played
    private Level LEVEL;
   
    private int jumps;
    private int jumps_highscore;
    //Player variable
    private Player PLAYER;
  
    //Array of keys and bufferedimage
    private boolean keys[];
    private BufferedImage back;
    //Levelnum variable, for updating jump highscore later
    private int levelNum;

    private String theFile;
    private String oldLine;

    private int parallaxX;

    //Image
    private Image IMAGE;

    private ArrayList<Enemy> ENEMIES;
    private ArrayList<Platform> PLATFORMS;
    private boolean isEnd;

    //Constructor, takes a "levelnum" and gets the time highscore from the level file
    //Also instantiates the level using the data from the level file
    public Platformer   (int levelNum)
    {
      //Getting highscore time
      try
        {
          //Create FileReader and BufferedReader for getting player & time information
          FileReader fr = new FileReader(levelNum + ".txt");
          BufferedReader br = new BufferedReader(fr);
          //Create string to represent time based on first line in level.txt
          String highscoreJumps = br.readLine();
          oldLine = highscoreJumps;
          //Why does this work
          //Shave off the Highscore Time: part of the string, leaving the time
          highscoreJumps = highscoreJumps.substring(16);
          //Set TIME_HIGHSCORE to an double from highscoretime
          jumps_highscore = Integer.parseInt(highscoreJumps);
        }
      catch (Exception ex)
        {
          System.out.println(ex.getMessage());
        }
      //Jumps starts off as zero
      //jumps = 0;
      
      //Instantiates level using levelNum
      LEVEL = makeLevel(levelNum + ".txt");
      theFile = levelNum + ".txt";
      parallaxX = -50;
      
      //Instantiates player
      PLAYER = new Player(50, 560);
      //set levelnum
      this.levelNum = levelNum;

      //Initialize keys
      keys = new boolean[6];

      //Initialize image
      try 
        {
            URL url = getClass().getResource("images/background.png");
            IMAGE = ImageIO.read(url);
        } 
        catch (Exception e) 
        {
            System.out.println("Couldn't locate image file");
        }

      //if we need to initialize collisions for enemies do it here ****
      /*for (Enemy : ENEMIES)
           {
            //initialize 
           }*/
      
      //Set background to white and add key listener
      setBackground(Color.WHITE);
      setVisible(true);
                
      new Thread(this).start();
      addKeyListener(this);
    }

    private Level makeLevel (String file)
    {
      //method will create an arraylist of platforms with their dimensions and positions
      //alongside an arraylist of enemies with their positions
      //then pass the lists into a new level and return it
      PLATFORMS = new ArrayList<Platform>();
      ENEMIES = new ArrayList<Enemy>();
      Flag FLAG = new Flag(0, 0);

      //Generate random color
      int r = (int) (Math.random() * 255) + 1;
      int g = (int) (Math.random() * 255) + 1;
      int b = (int) (Math.random() * 255) + 1;
      Color col = new Color(r, g, b, 90);

      //Read file data and add platforms and enemies to array
      try
        {
          //Create FileReader and BufferedReader for use reading file data
          FileReader fr = new FileReader(file);
          BufferedReader br = new BufferedReader(fr);

          //This is the highscore, it is to be disregarded because it has no effect on level
          String disregard = br.readLine();

          //The number of platforms in the level file
          int numOfPlatforms = Integer.parseInt(br.readLine());
          //For each platform in the file, create it and add to the arraylist
          for (int i = 0; i < numOfPlatforms; i++)
            {
              //Go through 4 lines, setting values appropriately
              int xPos = Integer.parseInt(br.readLine());
              int yPos = Integer.parseInt(br.readLine());
              int width = Integer.parseInt(br.readLine());
              int height = Integer.parseInt(br.readLine());
              //Add new platform to list based on values
              PLATFORMS.add(new Platform(xPos, yPos, width, height, col));
            }
      
          //The number of enemies in the level file
          int numOfEnemies = Integer.parseInt(br.readLine());
          //For each platform in the file, create it and add to the arraylist
          for (int i = 0; i < numOfEnemies; i++)
            {
              //Go through 2 lines, setting values appropriately
              int xPos = Integer.parseInt(br.readLine());
              int yPos = Integer.parseInt(br.readLine());
              //Add new enemy to list based on values
              ENEMIES.add(new Enemy(xPos, yPos));
            }
          //Flag x and y
          int flagX = Integer.parseInt(br.readLine());
          int flagY = Integer.parseInt(br.readLine());
          FLAG = new Flag(flagX, flagY);
        }
      catch (Exception ex)
        {
          System.out.println(ex.getMessage());
        }

      //return new level based on arraylist values
      return new Level (PLATFORMS, ENEMIES, FLAG);
    }

    //Time methods, remember to start the timer at the beginning of the game
    public void updateJumps() { jumps = PLAYER.getJumps(); }

    //Update and paint method
    public void update (Graphics window)
    {
      if(isEnd)
        return;
      paint(window);
    }

    public void paint (Graphics window)
    {
      
      //Graphics
      Graphics2D twoDGraph = (Graphics2D)window;
      if (back==null)
        back = (BufferedImage)(createImage(getWidth(),getHeight()));
      Graphics graphToBack = back.createGraphics();

      //Draw parallax background on x, fixed on y
      //speed will be 1 opposite of player
      //move background by getting x of background and moving it opposite of player
      graphToBack.drawImage(IMAGE, /*X*/parallaxX, /*Y*/0, /*WIDTH*/1066, /*HEIGHT*/800, /*NULL*/null);

      //Draw level and player
      PLAYER.checkIsDead();

      //PLAYER.checkPlatforms(LEVEL.getPlatforms());
      if(PLAYER.getIsDead())
      {
        Game.print("Game over, you died--! Better luck next time!","red");
        Game.main(Game.arguments);
        isEnd = true;
      }
        
      LEVEL.draw(graphToBack);
      PLAYER.draw(graphToBack);
      
      //Draw name and current jumps & highscore jumps
      graphToBack.setColor(Color.WHITE);
      graphToBack.drawString("Platformer", 25, 40);
      graphToBack.drawString("Jump Highscore: " + jumps_highscore, 25, 60);
      jumps = PLAYER.getJumps();
      graphToBack.drawString("Jumps: " + jumps, 25, 80);
      graphToBack.drawString("Health: " + PLAYER.getHP(), 25, 100);
      
      //Game logic should go here:
      //ParallaxX gets updated based on direction of player
      PLAYER.update();
      if (PLAYER.isMovingLeft() && PLAYER.getX() > 15)
        parallaxX++;
      else if (PLAYER.isMovingRight() && PLAYER.getX() < 750)
        parallaxX--;

      //Moving/updating player and enemies
      PLAYER.move(LEVEL.getPlatforms());
      for (Enemy enemy : LEVEL.getEnemies())
      {
        enemy.update(PLAYER, LEVEL.getPlatforms());
        enemy.move(LEVEL.getPlatforms());
      }

      for(int i = 0; i < ENEMIES.size(); i++)
        {
          ENEMIES.get(i).checkIsDead();
          if(ENEMIES.get(i).getIsDead())
            ENEMIES.remove(i);
          //else
            //ENEMIES.get(i).attack();
        }

        
    for(int i = PLAYER.getBullets().getList().size()-1; i >= 0; i--) 
      {
        for(Entity e : LEVEL.getPlatforms())
        {
          if(e.didCollide(PLAYER.getBullets().getList().get(i))) 
          {
            PLAYER.getBullets().getList().get(i).setX(-1000);
            PLAYER.getBullets().getList().get(i).setX(-1000);
          }
        }
      }  

    
        
      
      //Keys logic
      if (keys[0])
      {
        //W Key - Jump
        
        PLAYER.jump();
      }
      if (keys[1])
      {
        //A Key - Left
        PLAYER.setMovingLeft(true);
      } 
      else if(!keys[1]) 
      {
        PLAYER.setMovingLeft(false);
      }
      if (keys[2])
      {
        //S Key - NOTHING GOES HERE!
        //PLAYER.move("DOWN", LEVEL.getPlatforms());
      }
      if (keys[3])
      {
        //D Key - Right
        PLAYER.setMovingRight(true);
      } 
      else if(!keys[3]) 
      {
        PLAYER.setMovingRight(false);
      }
      if (keys[4])
      {
        //E Key - Shoot
        try
        {
            PLAYER.attack();
        } 
        catch(Exception e) {}
      }
      if(keys[5])
      {
        Game.main(Game.arguments);
        isEnd = true;
      }
      int invincibility = 0;
      //check player collision with enemy
      for(int i = 0; i < ENEMIES.size(); i++)
      {
        
        if(ENEMIES.get(i).didCollide(PLAYER))
        { 
          PLAYER.decrementHP();
          
         
          //ENEMIES.get(i).decrementHP();
        }
      }

      //check enemy collision with bullet
      for(Integer i: PLAYER.check(ENEMIES))
        {
          ENEMIES.get(i).decrementHP();
        }
      
      //Player touching flag logic
      if (PLAYER.didCollide(LEVEL.getFlag()) && LEVEL.allEnemiesDead())
      {
        if (jumps < jumps_highscore)
        {
          //Do new highscore
          jumps_highscore = jumps;
          try 
          {
            Scanner sc = new Scanner(new File(theFile));
            StringBuffer buffer = new StringBuffer();
          
            while (sc.hasNextLine()) 
            {
               buffer.append(sc.nextLine() + System.lineSeparator());
            }

            String fileContents = buffer.toString();
            sc.close();
            String newLine = "Highscore Jump: " + jumps;
            fileContents = fileContents.replaceAll(oldLine, newLine);
            FileWriter writer = new FileWriter(theFile);
            writer.append(fileContents);
            writer.flush();
          }
          catch (Exception e)
          {
            //nothing
          }
          
          //----For now---- just terminate the program after drawing
          Game.print("You won with a new highscore of " + jumps + " jumps! Nice!","green");
          Game.main(Game.arguments);
          isEnd = true;
        }
        else if(jumps > jumps_highscore)
        {
          Game.print("You won! You had " + jumps + " jumps! The highscore is " + jumps_highscore + "! Better luck next time!","green");
          Game.main(Game.arguments);
          isEnd = true;
        }
        else
        {
          Game.print("You won in " + jumps + " jumps, which is the same as the highscore!","green");
          Game.main(Game.arguments);
          isEnd = true;
        }
      }
      
      twoDGraph.drawImage(back, null, 0, 0);
    }

    //Keys
    public void keyPressed(KeyEvent e)
    {
      switch(toUpperCase(e.getKeyChar()))
      {
        case 'W' : keys[0]=true; break;
        case 'A' : keys[1]=true; break;
        case 'S' : keys[2]=true; break;
        case 'D' : keys[3]=true; break;
        case 'E' : keys[4]=true; break;
        case 'R' : keys[5]=true; break;
      }
      repaint();
    }

    public void keyReleased(KeyEvent e)
    {
      switch(toUpperCase(e.getKeyChar()))
      {
        case 'W' : keys[0]=false; break;
        case 'A' : keys[1]=false; break;
        case 'S' : keys[2]=false; break;
        case 'D' : keys[3]=false; break;
        case 'E' : keys[4]=false; break;
        case 'R' : keys[5]=false; break;
      }
      repaint();
    }

  public void keyTyped (KeyEvent e) {}

  //Scary stuff... dont mess with it because it might get angry and destroy the project
  public void run()
    {
      try
      {
        while(true)
        {
          Thread.currentThread().sleep(40);
          repaint();
        }
      }
      catch(Exception e)
      {
        
      }
    }     
    
  }