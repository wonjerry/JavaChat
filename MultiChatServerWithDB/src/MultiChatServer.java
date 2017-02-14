

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class MultiChatServer {
	HashMap clients;
	
	MultiChatServer() {
		clients = new HashMap();
		Collections.synchronizedMap(clients);
	}
	
	public void start(){
		ServerSocket serverSocket = null;
		Socket socket = null;
		
		try{
			serverSocket = new ServerSocket(7777);
			System.out.println("서버가 시작되었습니다.");
			while (true) {
                socket = serverSocket.accept();
                System.out.println("[" + socket.getInetAddress() + ":"
                        + socket.getPort() + "]" + "에서 접속하였습니다.");
                ServerReceiver thread = new ServerReceiver(socket);
                thread.start();
            }
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	void sendToAll(String msg){
		Iterator it = clients.keySet().iterator();
		while(it.hasNext()){
			try{
				DataOutputStream out = (DataOutputStream) clients.get(it.next());
				out.writeUTF(msg);
			}catch(IOException e){
				//e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new MultiChatServer().start();
	}
	
	class ServerReceiver extends Thread {
		private Socket socket;
		private DataInputStream in;
		private DataOutputStream out;
		private MultiChatClientDB db;
		
		ServerReceiver(Socket socket){
			this.socket = socket;
			try{
				in = new DataInputStream(socket.getInputStream());
				out = new DataOutputStream(socket.getOutputStream());
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		
		public void run(){
			String name = "";
			boolean dbCheckValue = false;
			String[] infos;
			
			
			try{
				this.db = new MultiChatClientDB();
				while(!dbCheckValue){
					infos = (in.readUTF()).split(" ");
					dbCheckValue = this.db.check(infos[0], infos[1]);
					if(!dbCheckValue)out.writeInt(0);
					else out.writeInt(1);
				}
				
				name = in.readUTF();
				sendToAll("#"+name+" 님이 들어오셨습니다\n");
				clients.put(name, out);
				System.out.println("현재 서버 접속자 수는 "+clients.size()+" 입니다.");
				while(in != null){
					sendToAll(in.readUTF());
				}
			}catch(IOException e){
				
			}finally{
				if(clients.size() != 0 ){
					sendToAll("#"+name+" 님이 나가셨습니다\n");
				}
				clients.remove(name);
				System.out.println("["+socket.getInetAddress()+":"+socket.getPort()+"]"+"에서 접속을 종료하셨습니다");
				System.out.println("현재 서버 접속자 수는 "+clients.size()+" 입니다.");
				
			}
			
		}
	}

}
