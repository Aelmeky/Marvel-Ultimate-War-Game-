//package application;
//import javafx.event.Event;
//import javafx.event.EventHandler;
//import javafx.geometry.Pos;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.TextField;
//import javafx.scene.input.MouseEvent;
//import javafx.scene.layout.BorderPane;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.VBox;
//import javafx.scene.text.Font;
//import javafx.scene.text.Text;
//import javafx.stage.Stage;
//public class get {
//	static int xc;
//	static int yc;
//	static boolean good;
//	public static int[] getCoords() {
//		helper();
//		System.out.println("help");
//		int[] arr= {xc,yc};
//		return arr;
//	}
//	public static void helper() {
//		good=false;
//		Stage stage=new Stage();
//		stage.setTitle("Enter X & Y ");
//		BorderPane border = new BorderPane();
//		Scene scene = new Scene(border);
//		border.setStyle("-fx-background-color: #caffee;");
//		stage.setScene(scene);
//		stage.setWidth(400);
//		stage.setHeight(200);
//		
//		VBox vbox=new VBox();
//		HBox row1=new HBox();
//		HBox row2=new HBox();
//		Button ok=new Button("OK");
//		vbox.getChildren().add(row1);
//		vbox.getChildren().add(row2);
//		vbox.getChildren().add(ok);
//		vbox.setAlignment(Pos.CENTER);
//		
//		Text x=new Text("X:");
//		x.setFont(Font.font("verdana",15));
//		TextField f1=new TextField();
//		row1.getChildren().add(x);
//		row1.getChildren().add(f1);
//
//		Text y=new Text("Y:");
//		y.setFont(Font.font("verdana",15));
//		TextField f2=new TextField();
//		row2.getChildren().add(y);
//		row2.getChildren().add(f2);
//
//		border.setCenter(vbox);
//		stage.show();
//		ok.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<Event>() {
//			public void handle(Event arg0){
//				xc=Integer.parseInt(f1.getText());
//				yc=Integer.parseInt(f2.getText());
//				if(xc<0||xc>4||yc<0||yc>4) {
//					new errormes("error", "Please Enter x & y in Rnage [0,4]");
//				}else {
//					good=true;
//				}
//				stage.close();
//			}
//		} );
//	}
//
//}
