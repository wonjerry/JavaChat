

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
			System.out.println("������ ���۵Ǿ����ϴ�.");
			while (true) {
                socket = serverSocket.accept();
                System.out.println("[" + socket.getInetAddress() + ":"
                        + socket.getPort() + "]" + "���� �����Ͽ����ϴ�.");
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
				sendToAll("#"+name+" ���� �����̽��ϴ�\n");
				clients.put(name, out);
				System.out.println("���� ���� ������ ���� "+clients.size()+" �Դϴ�.");
				while(in != null){
					sendToAll(in.readUTF());
				}
			}catch(IOException e){
				
			}finally{
				if(clients.size() != 0 ){
					sendToAll("#"+name+" ���� �����̽��ϴ�\n");
				}
				clients.remove(name);
				System.out.println("["+socket.getInetAddress()+":"+socket.getPort()+"]"+"���� ������ �����ϼ̽��ϴ�");
				System.out.println("���� ���� ������ ���� "+clients.size()+" �Դϴ�.");
				
			}
			
		}
	}

}
