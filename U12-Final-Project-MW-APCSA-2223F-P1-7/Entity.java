import java.awt.*;

public abstract class Entity {
    public static final int GRAVITY = 1;
    private int x, y, width, height, xVel, yVel, HP, maxHP;
    public final static int TERMINAL_Y_VEL = 15;
    private boolean isDead;

    // for which direction the entity is facing, used for display purposes
    // 0 for facing front (standing still), 1 for facing left, 2 for facing right,
    private int direction;

    // constructors
    public Entity(int x, int y) {
        this.x = x;
        this.y = y;
        width = 10;
        height = 10;
        HP = 1;
        maxHP = 1;
        xVel = 0;
        yVel = 0;
        direction = -1;
    }

    public Entity(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        width = w;
        height = h;
        HP = 1;
        maxHP = 1;
        xVel = 0;
        yVel = 0;
        direction = -1;
    }

    public Entity(int x, int y, int w, int h, int he) {
        this.x = x;
        this.y = y;
        width = w;
        height = h;
        HP = he;
        maxHP = he;
        xVel = 0;
        yVel = 0;
        direction = -1;
    }

    // getters & setters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getHP() {
        return HP;
    }

    public int getDirection() {
        return direction;
    }

    public int getXVel() {
        return xVel;
    }

    public int getYVel() {
        return yVel;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setHP(int hp) {
        HP = hp;
    }

    public void setDirection(int d) {
        direction = d;
    }

    public void setIsDead(boolean d) {
        isDead = d;
    }

    public boolean getIsDead() {
        return isDead;
    }

    public void setXVel(int xV) {
        xVel = xV;
    }

    public void setYVel(int yV) {
        yVel = yV;
    }

    // draw method to be called each frame
    public abstract void draw(Graphics g);

    // checks for collision with other Entities
    public boolean didCollide(Entity other) {
        if (this.getX() + this.getWidth() < other.getX() || other.getX() + other.getWidth() < this.getX())
            return false;
        if (this.getY() + this.getHeight() < other.getY() || other.getY() + other.getHeight() < this.getY())
            return false;
        return true;
    }

    public void decrementHP() {
        setHP(getHP() - 1);
    }

    // changes isDead to true if the entity is no longer existent
    public abstract void checkIsDead();

    public String toString() {
        return "[" + x + ", " + y + ", " + width + ", " + height + "]";
    }
}