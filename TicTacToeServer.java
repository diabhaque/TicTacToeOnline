import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This program implements a simple TicTacToeServer. It connects to two TicTacToe Clients simultaneously and allows communication between the two clients.
 * 
 * @author Diabul Haque
 * @version 1.0
 */

public class TicTacToeServer {
	ArrayList<PrintWriter> clientOutputStreams;
	int clients=0;
	public class ClientHandler implements Runnable{
		BufferedReader reader;
		Socket sock;
		
		/**
		 * This is the constructor method that creates an input stream from the client to the server
		 * 
		 * @param clientSocket The socket connection between the client and the server
		 */
		public ClientHandler(Socket clientSocket) {
			try {
				sock =clientSocket;
				InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
				reader= new BufferedReader(isReader);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		
		/**
		 * This method reads a message from a particular client and prints the input to all the clients through their output stream
		 */
		public void run() {
			String message = null;
			try {
				while((message=reader.readLine())!=null) {
					tellEveryone(message);
				}
			}catch(Exception ex) {
				if(!message.equals("0")) {
					ex.printStackTrace();
				}
			}
		}
	}
	/**
	 * This is the main method. It calls the function go()
	 * @param args
	 */
	public static void main(String[] args) {
		new TicTacToeServer().go();
	}
	/**
	 * This functions creates the server socket and listens for clients. Once a connection is made, it starts individual threads for the clients.
	 */
	public void go() {
		clientOutputStreams =new ArrayList<PrintWriter>();
		try {
			ServerSocket serverSock = new ServerSocket(5000);
			System.out.println("Server is running...");
			
			while(clients<=2) {
				Socket clientSocket = serverSock.accept();
				PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
				clientOutputStreams.add(writer);
				String startGame=Integer.toString(clients+1);
				writer.println(startGame);
				writer.flush();
				Thread t = new Thread(new ClientHandler(clientSocket));
				t.start();
				clients++;
				
			}
			serverSock.close();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	/**
	 * This function takes a message string and prints it out to all the clients
	 * @param message
	 */
	public void tellEveryone(String message) {
		Iterator<PrintWriter> it =clientOutputStreams.iterator();
		while(it.hasNext()){
			try {
				PrintWriter writer = (PrintWriter) it.next();
				writer.println(message);
				writer.flush();
			}catch(Exception ex) {
				if(!message.equals("0")) {
					ex.printStackTrace();
				}
				
			}
		}
	}
}
