package model.world;

public class Assassin extends Human{
	private	boolean visible;
	
	public Assassin(int maxHealth) {
		super(maxHealth);
		visible = true;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	

}
