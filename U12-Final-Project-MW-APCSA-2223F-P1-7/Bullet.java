import java.awt.Graphics;
import java.awt.Color;

public class Bullet extends Entity 
{
  private int speed;
  private String direction;
  
  // Constructor for bullet, requires a entitiy of origin and direction of movement
  public Bullet(Entity origin,String direction)
  {
    super(origin.getX()+(origin.getWidth()-10)/2,origin.getY()+(origin.getHeight()-5)/2);
    this.direction=direction;
    speed = 10;
  } 
  
  //logic to check for collision between a bullet and other entity
  /*
  public boolean didCollide(Entity other)
  {
    if(getX() + 10 >= other.getX() && getX() <= other.getX() + other.getWidth() && getY()-10 >= other.getY() && getY() <= other.getY() + other.getHeight())
      return true;
    return false;
  }
  */
  
  //updates bullet position and draws bullet as a yellow rectangle
  public void draw(Graphics window)
  {
    window.setColor(Color.YELLOW);
    window.fillRect(getX(), getY(), 10, 5);
    //add code to draw the ammo
  }
  public void update()
  {
    //n/a
  }
  
  //required as bullet extends entity which has an abstract method checkIsDead;
  public void checkIsDead()
  {
    if(getHP() == 0)
      setIsDead(true);
  }
  
  //updates position of bullet based on direction of movement
  public void move()
  {
    if(direction.equals("RIGHT")) 
      setX(getX() + speed);
    
    if(direction.equals("LEFT")) 
      setX(getX() - speed);
    
    if(direction.equals("UP")) 
      setY(getY() - speed);
    
    if(direction.equals("DOWN")) 
      setY(getY() + speed);
  }
}