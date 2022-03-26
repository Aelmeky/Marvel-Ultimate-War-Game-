package model.world;
import java.awt.*;
import exceptions.GameActionException;

public class Cover {
    private int currentHP;// this value is always >0// i.e exception in the setter.
    private Point location;

    public Point getLocation() {
        return location;
    }
    public int getCurrentHP() {
        return currentHP;
    }
    public void setCurrentHP(int currentHP) {
//    	try {
//    		if(currentHP>=0){
//    			this.currentHP=currentHP;
//    		}else {
//    			throw new GameActionException("cover health cannot be smaller than 0");
//    		}
//    	}catch(){
//    		setCurrentHP(0);
//    	}
    	this.currentHP=currentHP;
    }
    public Cover(int x, int y){
        this.location=new Point(x,y);
        this.currentHP=(int)(Math.random() * ((1000 - 100) + 1)) + 100;
    }

}
