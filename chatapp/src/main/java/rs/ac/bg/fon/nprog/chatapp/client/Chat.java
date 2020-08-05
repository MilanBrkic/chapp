package rs.ac.bg.fon.nprog.chatapp.client;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

public class Chat extends JFrame {

	private JPanel contentPane;
	private Socket soketZaKomunkaciju = null;
	private PrintStream izlazniTokKaServeru = null;
	private BufferedReader ulazniTokOdServera = null;
	private String username;
	private Socket soketZaKomunikaciju;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
//					Chat frame = new Chat();
//					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Chat(String username, Socket soketZaKomunikaciju, BufferedReader ulazniTokOdServera, PrintStream izlazniTokKaServeru) {
		this.username = username;
		this.soketZaKomunikaciju = soketZaKomunikaciju;
		this.ulazniTokOdServera = ulazniTokOdServera;
		this.izlazniTokKaServeru = izlazniTokKaServeru;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblDobrodosao = new JLabel("Dobrodosao "+username);
		lblDobrodosao.setBounds(155, 105, 109, 14);
		contentPane.add(lblDobrodosao);
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Chat.this.izlazniTokKaServeru.println("izlaz");
				
				
			}
		});
	}
	
	
}
