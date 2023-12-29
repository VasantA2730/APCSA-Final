import javax.swing.JFrame;
import java.awt.Component;
import java.util.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Canvas;

public class Level
  {
    //Variables
    
    //ArrayList of platforms populating level
    private ArrayList<Platform> PLATFORMS;
    //ArrayList of enemies populating level
    private ArrayList<Enemy> ENEMIES;
    //Flag
    private Flag FLAG;
    
    //Level constructor, takes in ArrayLists of platforms and enemies, presumably
    //read from a level layout file
    public Level (ArrayList<Platform> P, ArrayList<Enemy> E, Flag F)
    {
      PLATFORMS = P;
      ENEMIES = E;
      FLAG = F;
      //This will be used for instantiating the 4 border walls on the edges of the screen
      //For now this can be left out
      //Cursed by the ghost of replit - doesnt work 75% of the time because reasons
      PLATFORMS.add(new Platform(0, 0, 800, 10, Color.GRAY));
      PLATFORMS.add(new Platform(0, 0, 10, 600, Color.GRAY));
      PLATFORMS.add(new Platform(790, 0, 10, 600, Color.GRAY));
      PLATFORMS.add(new Platform(0, 570, 800, 10, Color.GRAY));
    }

    //Draw method
    public void draw(Graphics window)
    {
      for (Platform P : PLATFORMS)
        {
          P.draw(window);
        }
      for (Enemy E : ENEMIES)
        {
          E.draw(window);
        }
      FLAG.draw(window);
    }

    //Return methods for enemies and platforms and flag
    public ArrayList<Platform> getPlatforms()
    {
      return PLATFORMS;
    }

    public ArrayList<Enemy> getEnemies()
    {
      return ENEMIES;
    }

    public Flag getFlag()
    {
      return FLAG;
    }

    public boolean allEnemiesDead()
    {
      return ENEMIES.size() == 0;
    }
  }