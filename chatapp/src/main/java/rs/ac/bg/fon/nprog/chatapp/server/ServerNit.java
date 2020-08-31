package rs.ac.bg.fon.nprog.chatapp.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import rs.ac.bg.fon.nprog.chatapp.client.TextMessage;

public class ServerNit extends Thread{
	BufferedReader ulazniTokOdKlijenta= null;
	PrintStream izlazniTokKaKlijentu = null;
	Socket soketZaKom = null;
	ServerNit[] klijenti = null;
	String[] useri = {"milan", "milica", "kaca", "lalkec", "dane", "acim"};
	Map<String, String> sifre = new  HashMap<String, String>();
	private String username;
	Gson gson = new GsonBuilder().create();
	
	
	public ServerNit(Socket klijentSoket, ServerNit[] klijenti) {
		this.soketZaKom = klijentSoket;
		this.klijenti = klijenti;
	}
	
	public boolean daLiPostoji(String username) {
		for (int i = 0; i < useri.length; i++) {
			
			if(useri[i].equals(username)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void run() {
		String username;
		String password;
		String rezim;
		sifre.put("milan", "milan");
		sifre.put("milica", "milica");
		sifre.put("kaca", "kaca");
		sifre.put("lalkec", "lalkec");
		sifre.put("dane","dane");
		sifre.put("acim","acim");
		
		
		try {
			
			ulazniTokOdKlijenta = new BufferedReader(new InputStreamReader(soketZaKom.getInputStream()));
			izlazniTokKaKlijentu = new PrintStream(soketZaKom.getOutputStream());
			while(true) {
				rezim = ulazniTokOdKlijenta.readLine();
				if(rezim.equals("Sign-In")) {
					username = proveraSignIn();
					setUsername(username);
					for (int i = 0; i < klijenti.length; i++) {
						if(klijenti[i]!=null) {
							System.out.println(klijenti[i]);
						}
					}
				}
				else if(rezim.equals("izlaz")) {
					break;
				}
				else if(rezim.equals("getUsers")) {
					izlazniTokKaKlijentu.println(gson.toJson(useri));

				}
				else if(rezim.equals("message")) {
					String s = ulazniTokOdKlijenta.readLine();
					izlazniTokKaKlijentu.println("message");
					TextMessage txtmsg =  gson.fromJson(s, TextMessage.class);
					System.out.println(txtmsg);
					Server.poruke.add(txtmsg);
					izlazniTokKaKlijentu.println(s);
					
					for (int i = 0; i < klijenti.length; i++) {
						if(klijenti[i]!=null && klijenti[i]!=this  && klijenti[i].username!=null
								&& ( klijenti[i].username.equals(txtmsg.getSender()) ||
								klijenti[i].username.equals(txtmsg.getReceiver()) ) ){
							
							klijenti[i].izlazniTokKaKlijentu.println("message");
							klijenti[i].izlazniTokKaKlijentu.println(s);
						}
					}
				}
				else if(rezim.equals("dajcet")) {
					
					String partner = ulazniTokOdKlijenta.readLine();
					
					LinkedList<TextMessage> porukePom = new LinkedList<TextMessage>();
					
					for (TextMessage tm : Server.poruke) {
						if((tm.getSender().equals(partner) && tm.getReceiver().equals(this.username))
								|| (tm.getSender().equals(this.username) && tm.getReceiver().equals(partner))) {
							porukePom.add(tm);
							
						}
					}
					izlazniTokKaKlijentu.println("dajcet");
					izlazniTokKaKlijentu.println(gson.toJson(porukePom));
					
				}
			}
			
			soketZaKom.close();
			ulazniTokOdKlijenta.close();
			izlazniTokKaKlijentu.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for (int i = 0; i < klijenti.length; i++) {
			if(klijenti[i]==this) {
				klijenti[i] = null;
			}
		}
	}
	
	public String proveraSignIn() throws IOException {
		//citanje username od klijenta
		String password;
		String username = ulazniTokOdKlijenta.readLine();
		username.trim();
		
		
		if(!daLiPostoji(username)) {//provera da li se user nalazi u bazi
			izlazniTokKaKlijentu.println("username ne postoji");
			System.out.println("Nije dobar username: "+username);
			return null;
		}
		else {
			izlazniTokKaKlijentu.println("username postoji");
			System.out.println("Dobar username: "+username);
			
			//citanje passworda
			password = ulazniTokOdKlijenta.readLine();
			String praviPass = sifre.get(username);
			
			if(!praviPass.equals(password)) {
				izlazniTokKaKlijentu.println("netacan password");
				System.out.println("netacan password");
				return null;
			}
			else {
				
				System.out.println("tacan password");
				
				
				//tacan password provera da li je vec ulogovan user
				for (int i = 0; i < klijenti.length; i++) {
					if(klijenti[i]!=null) {
						if(klijenti[i]!=this && klijenti[i].username!=null && klijenti[i].username.equals(username)) {
							izlazniTokKaKlijentu.println("user vec ulogovan");
							System.out.println("user vec ulogovan");
							return null;
						}
					}
				}
				izlazniTokKaKlijentu.println("tacan password");
				
				return username;
			}
			
		}
		
	}
	
	@Override
	public String toString() {
		return "ServerNit [username=" + username + "]";
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
