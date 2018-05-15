import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Network implements Runnable{
	public static String ip = "192.168.0.";
	private static int port = 666;
	private static boolean isServerRunning = false;
	private static boolean maxIsFound = false;
	private static ServerSocket server;
	private static Socket connection;
	private static ObjectOutputStream output;
	private static ObjectInputStream input;
	private static Blockchain max = new Blockchain();
	private static Blockchain buf;
	private static int ipMax;
	public static int k = 1;
	
	@Override
	public void run() {
		try {
			while(true) {
				if(isServerRunning) {
					connection = server.accept();
					output = new ObjectOutputStream(connection.getOutputStream());
					input = new ObjectInputStream(connection.getInputStream());
					String a = (String)input.readObject();
					if(a == "gimme sum blockchain, pls") {
						output.writeObject(max);
					}
					else if(a.matches(ip + ".+")) {
						clientMode();
					}
					else if(a.matches("[0-9]+")) {
						k = Integer.parseInt(a);
					}
				}
				else if(!maxIsFound){
					for(int i = 1; i < 255; i++) {
						connection = new Socket(InetAddress.getByName(ip + i), port);
						output = new ObjectOutputStream(connection.getOutputStream());
						input = new ObjectInputStream(connection.getInputStream());
						buf = (Blockchain)input.readObject();
						k++;
						if(buf.blockchain.size() >= max.blockchain.size()) {
							max = buf;
							ipMax = i;
						}
					}
					maxIsFound = true;
					Main.blockchain = Blockchain.setBlockchain(max);
					for (int i = 1; i < ipMax; i++) {
						sendData(ip + ipMax);
					}
					for (int i = 254; i > ipMax; i--) {
						sendData(ip + ipMax);
					}
				}
				else {
					connection = new Socket(InetAddress.getByName(ip + ipMax), port);
					output = new ObjectOutputStream(connection.getOutputStream());
					input = new ObjectInputStream(connection.getInputStream());
					Main.blockchain = Blockchain.setBlockchain((Blockchain)input.readObject());
				}
			}
		}
		catch(Exception e) {
			
		}
	}
	
	public static void sendData(Object obj) {
		try {
			output.flush();
			output.writeObject(obj);
		}
		catch(Exception e) {
			
		}
	}
	
	private static void clientMode(){
		isServerRunning = false;
		maxIsFound = true;
		
		sendData("gimme sum blockchain, pls");
		
		isServerRunning = true;
		try {
			server = new ServerSocket(port);
		}
		catch(Exception e) {
			
		}
	}
	
	public static void syncrhonize() {
		isServerRunning = false;
		maxIsFound = false;
		
		sendData("gimme sum blockchain, pls");
		
		sendData(k);
		
		isServerRunning = true;
		try {
			server = new ServerSocket(port);
		}
		catch(Exception e) {
			
		}
		System.out.println("Блоки успешно синхронизированы");
	}
}