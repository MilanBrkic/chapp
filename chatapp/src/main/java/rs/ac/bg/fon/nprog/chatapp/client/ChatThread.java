package rs.ac.bg.fon.nprog.chatapp.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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
			
				if(linijaOdServer.equals("message")) {
					String linijaOdServerPom = ulazniTokOdServera.readLine();
					String partner = lista.getSelectedValue().toString();
					System.out.println(partner+" "+linijaOdServerPom);
					
					TextMessage txtmsg = gson.fromJson(linijaOdServerPom, TextMessage.class);
					if(txtmsg.getReceiver().equals(partner)) {
						textArea.append("You: "+txtmsg.getMessage()+"\n");
					}
					else if(txtmsg.getSender().equals(partner)) {						
						textArea.append(partner+": "+txtmsg.getMessage()+"\n");
					}
				}
				else if(linijaOdServer.equals("dajcet")) {
					System.out.println("usao");
					String json = "";
					try {
						json = ulazniTokOdServera.readLine();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					List<TextMessage> poruke = Arrays.asList(gson.fromJson(json, TextMessage[].class));
					String partner = lista.getSelectedValue().toString();
					textArea.setText("");
					for (TextMessage textMessage : poruke) {
						if(!textMessage.getSender().equals(partner)) {
							textArea.append("You: "+textMessage.getMessage()+"\n");
						}
						else {
							textArea.append(partner+": "+textMessage.getMessage()+"\n");
						}
							
					}
				}
				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
