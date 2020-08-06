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
import java.awt.Dimension;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.border.BevelBorder;
import java.awt.Rectangle;
import java.awt.Component;
import javax.swing.SwingConstants;
import net.miginfocom.swing.MigLayout;
import javax.swing.SpringLayout;
import java.awt.CardLayout;
import javax.swing.JList;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.ScrollPaneConstants;
import javax.swing.ListSelectionModel;
import javax.swing.JComboBox;
import javax.swing.UIManager;

public class Chat extends JFrame {

	private JPanel contentPane;
	private Socket soketZaKomunkaciju = null;
	private PrintStream izlazniTokKaServeru = null;
	private BufferedReader ulazniTokOdServera = null;
	private String username;
	private Socket soketZaKomunikaciju;
	private JTextField txtWriter;

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
		setResizable(false);
		this.username = username;
		this.soketZaKomunikaciju = soketZaKomunikaciju;
		this.ulazniTokOdServera = ulazniTokOdServera;
		this.izlazniTokKaServeru = izlazniTokKaServeru;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(300, 200, 660, 410);
		contentPane = new JPanel();
		contentPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(10, 11, 120, 349);
		contentPane.add(scrollPane);
		
		JPanel panel = new JPanel();
		scrollPane.setViewportView(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JLabel lblNewLabel = new JLabel("<html>Logged in as: <B>"+username+"</B></html>");
		panel.add(lblNewLabel);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 10));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane_1.setToolTipText("");
		panel.add(scrollPane_1);
		
		JPanel panel_1 = new JPanel();
		scrollPane_1.setViewportView(panel_1);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));
		
		DefaultListModel<String> l1 = new DefaultListModel<String>();
		l1.addElement("da");
		l1.addElement("ne");
		try {
			l1 = getUsersFromServer();
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		l1.addElement("da");
		l1.addElement("da");
		l1.addElement("ne");l1.addElement("da");
		l1.addElement("ne");
		
		JLabel lblNewLabel_1 = new JLabel("Available chats:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 10));
		panel_1.add(lblNewLabel_1);
		
		JList<String> list = new JList<String>(l1);
		list.setBackground(UIManager.getColor("Panel.background"));
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		panel_1.add(list);
		
		txtWriter = new JTextField();
		txtWriter.setBounds(139, 340, 405, 22);
		contentPane.add(txtWriter);
		txtWriter.setColumns(10);
		
		JButton btnSend = new JButton("Send");
		btnSend.setBounds(554, 339, 80, 23);
		contentPane.add(btnSend);
		
		
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Chat.this.izlazniTokKaServeru.println("izlaz");
				
				
			}
		});
	}
	
	public DefaultListModel<String> getUsersFromServer() throws NumberFormatException, IOException {
		DefaultListModel<String> l1 = new DefaultListModel<String>();
		String user;
		izlazniTokKaServeru.println("getUsers");
		int n =	Integer.parseInt(ulazniTokOdServera.readLine()); 
		for (int i = 0; i < n; i++) {
			user = ulazniTokOdServera.readLine();
			if(!user.equals(username)) {
				l1.addElement(user);
			}
			
		}
		return l1;
		
	}
}
