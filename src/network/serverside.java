package network;

import java.io.IOException;
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
        Socket socket;
        String input;
        String output;
        
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
	
	
    public static void main(String[] args) throws Exception {

	try (var listener = new ServerSocket(58901)) {
        System.out.println("Tic Tac Toe Server is Running...");
        var pool = Executors.newFixedThreadPool(200);
        while (true) {
            
           // pool.execute(game.new Player(listener.accept(), 'X'));
            //pool.execute(game.new Player(listener.accept(), 'O'));
        }
    }
    }
	
}
	

