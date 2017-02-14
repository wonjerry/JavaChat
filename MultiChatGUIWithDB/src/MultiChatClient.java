
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

public class MultiChatClient {
	public ClientSender sender;
	public ClientReceiver reciever;
	
	public MultiChatClient(String id, String ip, Socket socket, javax.swing.JTextArea jTextArea){
		sender = new ClientSender(socket,id);
        reciever = new ClientReceiver(socket,jTextArea);
	}

	public static Socket connecting(String ip) {

		try {			
			Socket socket = new Socket(ip, 7777);
			System.out.println("connect!!");
			return socket;
		} catch (ConnectException ce) {
			ce.printStackTrace();
		} catch (Exception e) {
		}

		return null;
	}

	static class ClientSender extends Thread {
		DataOutputStream out;
		Socket socket;
		String name;

		ClientSender(Socket socket, String name) {
			this.socket = socket;
			try {
				out = new DataOutputStream(socket.getOutputStream());
				this.name = name;
				out.writeUTF(name);
			} catch (Exception e) {

			}
		}

		public void sendMsg(String msg) {
			try {
				if (out != null) {
					// out.writeUTF(name);
				}

				if (out != null) {
					out.writeUTF("[" + name + "] " + msg + "\n");
				}
			} catch (IOException e) {

			}
		}

	}

	static class ClientReceiver extends Thread {
		Socket socket;
		DataInputStream in;
		private javax.swing.JTextArea jTextArea;

		ClientReceiver(Socket socket, javax.swing.JTextArea jTextArea) {
			this.jTextArea = jTextArea;
			this.socket = socket;

			try {
				in = new DataInputStream(socket.getInputStream());
			} catch (IOException e) {

			}
		}

		public void run() {
			while (in != null) {
				try {
					jTextArea.append(in.readUTF());
					jTextArea.setCaretPosition(jTextArea.getDocument().getLength());
				} catch (IOException e) {

				}
			}
		}
	}

}
