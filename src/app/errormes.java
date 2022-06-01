package app;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
public class errormes extends Alert{
	public errormes(String title,String message) {
		super(title,message);
		super.setTitle(title);
		Image icon=new Image("/Assets/Warninglogo.png");
		Image cancel=new Image("/Assets/Wrong.png");
		ImageView cancel1 = new ImageView();
		VBox imagBox = new VBox();
		imagBox.getChildren().add(cancel1);
		super.getIcons().add(icon);		
		((BorderPane)super.getScene().getRoot()).setRight(imagBox);	
		super.show();
	}

}
