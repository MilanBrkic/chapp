package rs.ac.bg.fon.nprog.chatapp.server;

import java.io.BufferedReader;
import java.io.IOException;
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
	private String username;
	
	
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
		
		
		try {
			ulazniTokOdKlijenta = new BufferedReader(new InputStreamReader(soketZaKom.getInputStream()));
			izlazniTokKaKlijentu = new PrintStream(soketZaKom.getOutputStream());
			while(true) {
				rezim = ulazniTokOdKlijenta.readLine();
				if(rezim.equals("Sign-In")) {
					username = proveraSignIn();
					setUsername(username);
				}
				else if(rezim.equals("izlaz")) {
					break;
				}
				else if(rezim.equals("getUsers")) {
					izlazniTokKaKlijentu.println(useri.length);
					for (int i = 0; i < useri.length; i++) {
						izlazniTokKaKlijentu.println(useri[i]);
					}
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
	
	public void setUsername(String username) {
		this.username = username;
	}
}
