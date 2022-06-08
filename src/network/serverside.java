package network;

import java.io.IOException;
import engine.*;
import app.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.util.Set;
import java.util.concurrent.Executors;

import engine.*;
import java.net.ServerSocket;
import java.net.Socket;

public class serverside {

	
	public class theplayer implements Runnable{

		theplayer opponent;
		static Player player1;
		static Player player2;
        Socket socket;
        String input;
        String output;
        
         public theplayer(Socket socket, String name) {
        	 this.socket = socket;
        	 player1 = new Player(name);
		}
         
        
		@Override
		public void run() {
			try {
                setup();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (opponent != null && opponent.output != null) {
                    opponent.output = ("OTHER_PLAYER_LEFT");
                }
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
			
		}


		private void setup() {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
	
	
    public static void main(String[] args) throws Exception {

	try (var listener = new ServerSocket(58901)) {
        System.out.println("Server is Running...");
        var pool = Executors.newFixedThreadPool(200);
        while (true) {
            
           // pool.execute(game.new Player(listener.accept(), 'X'));
            //pool.execute(game.new Player(listener.accept(), 'O'));
        }
    }
    }
	
}
	

