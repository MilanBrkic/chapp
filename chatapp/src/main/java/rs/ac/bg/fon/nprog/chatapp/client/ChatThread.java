package rs.ac.bg.fon.nprog.chatapp.client;

import java.io.BufferedReader;

import javax.swing.JList;
import javax.swing.JTextArea;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ChatThread extends Thread{
	BufferedReader ulazniTokOdServera;
	JTextArea textArea;
	JList lista;
	Gson gson;
	
	public ChatThread(BufferedReader ulazniTokOdServera, JTextArea textArea, JList lista) {
		this.ulazniTokOdServera = ulazniTokOdServera;
		this.textArea = textArea;
		this.lista = lista;
		gson = new GsonBuilder().create();
	}
	@Override
	public void run() {
		String linijaOdServer;
		try {
			
			while((linijaOdServer = ulazniTokOdServera.readLine())!=null) {
				String partner = lista.getSelectedValue().toString();
				System.out.println(partner+" "+linijaOdServer);
				
				TextMessage txtmsg = gson.fromJson(linijaOdServer, TextMessage.class);
				if(txtmsg.getReceiver().equals(partner)) {
					textArea.append("You: "+txtmsg.getMessage()+"\n");
				}
				else if(txtmsg.getSender().equals(partner)) {						
					textArea.append(partner+": "+txtmsg.getMessage()+"\n");
				}
				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
