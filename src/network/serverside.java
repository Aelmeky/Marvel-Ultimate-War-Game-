package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.util.Set;

import engine.*;

public class serverside implements Runnable{

	
	
    private Socket socket;
    private Player player;
	

	public serverside(Socket socket,Player player) {
		this.socket = socket;
		this.player = player;
		
		
	} 
	public void start() {
		new Thread(this).start(); // start running of the server
	}
		
	@Override
	public void run() {
		
	}
	
}
