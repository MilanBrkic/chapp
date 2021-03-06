package rs.ac.bg.fon.nprog.chatapp.client;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

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
import java.awt.TextArea;
import java.awt.Component;
import javax.swing.SwingConstants;
import net.miginfocom.swing.MigLayout;
import javax.swing.SpringLayout;
import java.awt.CardLayout;
import javax.swing.JList;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.awt.Color;
import javax.swing.ScrollPaneConstants;
import javax.swing.ListSelectionModel;
import javax.swing.JComboBox;
import javax.swing.UIManager;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Chat extends JFrame {

	private JPanel contentPane;
	private Socket soketZaKomunkaciju = null;
	private PrintStream izlazniTokKaServeru = null;
	private BufferedReader ulazniTokOdServera = null;
	private String username;
	private String partner;
	private Socket soketZaKomunikaciju;
	private JTextField txtWriter;
	private Gson gson;
	private JTextArea textArea;
	Thread A = null;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Chat(final String username, Socket soketZaKomunikaciju, BufferedReader ulazniTokOdServera, final PrintStream izlazniTokKaServeru) {
		partner = null;
		setResizable(false);
		this.username = username;
		this.soketZaKomunikaciju = soketZaKomunikaciju;
		this.ulazniTokOdServera = ulazniTokOdServera;
		this.izlazniTokKaServeru = izlazniTokKaServeru;
		gson =  new GsonBuilder().create();
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
		
		try {
			l1 = getUsersFromServer();
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		JLabel lblNewLabel_1 = new JLabel("Available chats:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 10));
		panel_1.add(lblNewLabel_1);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(140, 11, 494, 318);
		contentPane.add(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane_2 = new JScrollPane();
		panel_2.add(scrollPane_2, BorderLayout.CENTER);
		
		textArea = new JTextArea();
		scrollPane_2.setViewportView(textArea);
		textArea.setVisible(false);
		
		
		final JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!textArea.isVisible()) return;
				String txt = txtWriter.getText();
				if(txt!="" && !txt.equals("") && partner!=null) {
					TextMessage msg = new TextMessage(username, partner, txt, new GregorianCalendar());
					izlazniTokKaServeru.println("message");
					izlazniTokKaServeru.println(gson.toJson(msg));
					
					
					txtWriter.setText("");
					
				}
				txtWriter.requestFocus();
			}
		});
		
		btnSend.setBounds(554, 339, 80, 23);
		contentPane.add(btnSend);
		
		
		txtWriter = new JTextField();
		txtWriter.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				      btnSend.doClick();
				}
			}
		});
		txtWriter.setBounds(139, 340, 405, 22);
		contentPane.add(txtWriter);
		txtWriter.setColumns(10);
		
		
		
		JList<String> list = new JList<String>(l1);
		list.setBackground(UIManager.getColor("Panel.background"));
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				textArea.setText("");
				JList list = (JList) e.getSource();
				int index = list.getSelectedIndex();
				partner = list.getSelectedValue().toString();
				
				if(!textArea.isVisible()) {
					textArea.setVisible(true);
					A = new Thread(new ChatThread(Chat.this.ulazniTokOdServera, textArea, list));
					A.start();
				}
				
				
				
				txtWriter.requestFocus();
			}
		});
		panel_1.add(list);
		
		
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Chat.this.izlazniTokKaServeru.println("izlaz");
				if(A!=null && A.isAlive()) {
					A.interrupt();
				}
				
			}
		});
	}
	
	public DefaultListModel<String> getUsersFromServer() throws NumberFormatException, IOException {
		DefaultListModel<String> l1 = new DefaultListModel<String>();
		String user;
		izlazniTokKaServeru.println("getUsers");
		
		String s = ulazniTokOdServera.readLine();
		List<String> lista = Arrays.asList(gson.fromJson(s, String[].class));
		
		for (String string : lista) {
			if(!username.equals(string))
				l1.addElement(string);
		}
		

		return l1;
		
	}

//	@Override
//	public void run() {
//		String linijaOdServer;
//		try {
//			
//			while((linijaOdServer = ulazniTokOdServera.readLine())!=null) {
//				if(partner!=null) {
//					TextMessage txtmsg = gson.fromJson(linijaOdServer, TextMessage.class);
//					if(txtmsg.getReceiver().equals(partner)) {
//						textArea.append("You: "+txtmsg.getMessage());
//					}
//					else if(txtmsg.getSender().equals(partner)) {
//						textArea.append(partner+": "+txtmsg.getMessage());
//					}
//				}
//				
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//	}
}
