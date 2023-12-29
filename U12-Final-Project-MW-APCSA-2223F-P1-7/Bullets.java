import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

public class Bullets
{
  private ArrayList<Bullet> bulletList;
  //initializes an arraylist of bullets
  public Bullets()
  {
    bulletList = new ArrayList<Bullet>();
  }
  //adds a bullet object to the arraylist
  public void add(Bullet b)
  {
    bulletList.add(b);
  }

  //draws all bullets in the arraylist
  public void draw(Graphics window)
  {
    for(Bullet b : bulletList) {
      b.draw(window);
    }
  }
 // updates position of all bullets in the arraylist
  public void move()
  {
    for(Bullet b : bulletList) {
      b.move();
    }
  }
  
  //deletes any bullets which travel outside of the screen as they are no longer needed
  public void cleanUpEdges()
  {
     try {
       for(int i = bulletList.size()-1;i>=0;i--){
         if (bulletList.get(i).getY()<0)bulletList.remove(i);
         if (bulletList.get(i).getY()>Game.HEIGHT)bulletList.remove(i);
         if (bulletList.get(i).getX()<0)bulletList.remove(i);
         if (bulletList.get(i).getX()>Game.WIDTH)bulletList.remove(i);
       }
     } catch(Exception e) {}
  }
  //returns entire arrayList of bullets
  public ArrayList<Bullet> getList()
  {
    return bulletList;
  }
  //deletes a bullet in the arrayList at index i 
  public void deleteBullet(int i){
    bulletList.remove(i);
  }

  public String toString() {
      String ret = "[";
      for(Entity b : bulletList) {
          ret += b;
      }
      return ret + "]";
  }
}
