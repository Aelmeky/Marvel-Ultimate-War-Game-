package model.world;
import java.awt.*;

public class Cover {
    private int currentHP;// this value is always >0// i.e exception in the getter.
    private Point location;

    public Point getLocation() {
        return location;
    }
    public int getCurrentHP() {
        return currentHP;
    }
    public void setCurrentHP(int currentHP) {
        if(currentHP>=100){
            this.currentHP = currentHP;
        }else{
            ;
        }

    }
    public Cover(int x, int y){
        this.location=new Point(x,y);
        this.currentHP=(int)(Math.random() * ((1000 - 100) + 1)) + 100;
    }

}
