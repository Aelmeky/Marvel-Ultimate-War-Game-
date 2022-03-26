package model.world;
import model.world.Champion;

public class Hero extends Champion {
    public Hero(String name, int maxHP, int mana, int maxActions, int speed, int attackRange,int attackDamage){
        super(name,maxHP,mana,maxActions,speed,attackRange,attackDamage);
    }
}