package rs.ac.bg.fon.nprog.chatapp.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

import rs.ac.bg.fon.nprog.chatapp.client.TextMessage;

public class Server{
	static ServerNit klijenti[] = new ServerNit[10];
	static LinkedList<TextMessage> poruke = new LinkedList<TextMessage>();
	public static void main(String[] args) {
		int port = 2222;
		
		Socket klijentSoket = null;
		
		try {
			ServerSocket serverSoket = new ServerSocket(port);
			
			while(true) {
				klijentSoket = serverSoket.accept();
				
				for (int i = 0; i <=9 ; i++) {
					if(klijenti[i]==null) {
						klijenti[i] = new ServerNit(klijentSoket, klijenti);
						klijenti[i].start();
						break;
					}
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
	
	
	
		
}
	
	

