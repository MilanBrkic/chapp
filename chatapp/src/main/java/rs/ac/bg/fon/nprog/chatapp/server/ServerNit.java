package rs.ac.bg.fon.nprog.chatapp.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServerNit extends Thread{
	BufferedReader ulazniTokOdKlijenta= null;
	PrintStream izlazniTokKaKlijentu = null;
	Socket soketZaKom = null;
	ServerNit[] klijenti = null;
	String[] useri = {"milan", "milica", "kaca"};
	Map<String, String> sifre = new  HashMap<String, String>();
	
	
	
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
		sifre.put("milan", "milan");
		sifre.put("milica", "milica");
		sifre.put("kaca", "kaca");
		
		
		try {
			ulazniTokOdKlijenta = new BufferedReader(new InputStreamReader(soketZaKom.getInputStream()));
			izlazniTokKaKlijentu = new PrintStream(soketZaKom.getOutputStream());
			while(true) {
				//citanje username od klijenta
				username = ulazniTokOdKlijenta.readLine();
				username.trim();
				
				
				if(!daLiPostoji(username)) {//provera da li se user nalazi u bazi
					izlazniTokKaKlijentu.println("username ne postoji");
					System.out.println("Nije dobar username: "+username);
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
					}
					else {
						izlazniTokKaKlijentu.println("tacan password");
						System.out.println("tacan password");
					}
					
				}
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
}
