import java.awt.Color;
import java.awt.Graphics;

public class Platform extends Entity 
  {
    private Color c;
    private boolean isBreakable;

    
    public Platform(int xP, int yP, int w, int h)
    {
      super(xP,yP,w,h);
      c = Color.YELLOW;
    }

    public Platform(int xP, int yP, int w, int h,Color c)
    {
      super(xP,yP,w,h);
      this.c = c;
    }
    
    public boolean didCollide(Entity other)
    {
      if(this.getX()+this.getWidth()<other.getX() || other.getX()+other.getWidth()<this.getX())
        return false;
      if(this.getY()+this.getHeight()<other.getY() || other.getY()+other.getHeight()<this.getY())
        return false;
      return true;
  }
    
  
    public void checkIsDead()
    {
      if(getHP() == 0)
        setIsDead(true);
    }
    

    public void draw(Graphics window)
    {
      window.setColor(c);
      window.fillRect(getX(),getY(),getWidth(),getHeight());
    }

    public String toString() {
      return getX() + " : " + getY() + " : " + getWidth() + " : " + getHeight();
    }
  }