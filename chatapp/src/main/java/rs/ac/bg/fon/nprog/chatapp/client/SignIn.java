package rs.ac.bg.fon.nprog.chatapp.client;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Label;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JPasswordField;

public class SignIn extends JFrame {
	static Socket soketZaKomunkaciju = null;
	static PrintStream izlazniTokKaServeru = null;
	static BufferedReader ulazniTokOdServera = null;
	private JPanel contentPane;
	private JTextField txtUsername;
	private JTextField txtPassword;
	private JLabel lblUsernameError;
	private JLabel lblPasswordError;
	private JButton btnSignIn;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			int port = 2222;
			
			soketZaKomunkaciju = new Socket("localhost", port);
			izlazniTokKaServeru = new PrintStream(soketZaKomunkaciju.getOutputStream());
			ulazniTokOdServera = new BufferedReader(new InputStreamReader(soketZaKomunkaciju.getInputStream()));
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SignIn frame = new SignIn();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SignIn() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Username:");
		lblNewLabel.setBounds(93, 81, 65, 14);
		contentPane.add(lblNewLabel);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(93, 129, 65, 14);
		contentPane.add(lblPassword);
		
		txtUsername = new JTextField();
		
		txtUsername.setBounds(168, 78, 146, 20);
		contentPane.add(txtUsername);
		txtUsername.setColumns(10);
		
		txtPassword = new JPasswordField();
		txtPassword.setColumns(10);
		txtPassword.setBounds(168, 126, 146, 20);
		contentPane.add(txtPassword);
		
		btnSignIn = new JButton("Sign-In");
		btnSignIn.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				
			}
		});
		btnSignIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblUsernameError.setText("");
				lblPasswordError.setText("");
				String username = txtUsername.getText();
				String password = txtPassword.getText();
				
				try {
					if(proveraUserPass(username, password)) {
						slanjeServeruNaProveru(username, password);
						
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				
				
			}
		});
		btnSignIn.setBounds(176, 179, 89, 23);
		contentPane.add(btnSignIn);
		
		txtUsername.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				      btnSignIn.doClick();
				}
			}
		});
		
		txtPassword.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				      btnSignIn.doClick();
				}
			}
		});
		
		
		lblUsernameError = new JLabel("");
		lblUsernameError.setForeground(Color.RED);
		lblUsernameError.setBounds(168, 60, 146, 14);
		contentPane.add(lblUsernameError);
		
		lblPasswordError = new JLabel("");
		lblPasswordError.setForeground(Color.RED);
		lblPasswordError.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblPasswordError.setBounds(168, 109, 146, 14);
		contentPane.add(lblPasswordError);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				izlazniTokKaServeru.println("izlaz");
				try {
					soketZaKomunkaciju.close();
					izlazniTokKaServeru.close();
					ulazniTokOdServera.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
	}
	
	public JLabel getLblUsernameError() {
		return lblUsernameError;
	}
	
	public JLabel getLblPasswordError() {
		return lblPasswordError;
	}
	
	public JTextField getTxtUsername() {
		return txtUsername;
	}
	
	public JTextField getTxtPassword() {
		return txtPassword;
	}
	
	public JButton getBtnSignIn() {
		return btnSignIn;
	}
	public Socket getSoketZaKom() {
		return soketZaKomunkaciju;
	}
	
	public void setSoketZaKom(Socket soket) {
		soketZaKomunkaciju = soket;
	}
	
	public PrintStream getIzlazniTokKaServeru() {
		return izlazniTokKaServeru;
	}
	
	public void setIzlazniTokKaServeru(PrintStream izlazniTokKaServeru) {
		this.izlazniTokKaServeru = izlazniTokKaServeru;
	}
	public BufferedReader getUlazniTokOdServera() {
		return ulazniTokOdServera;
	}
	
	public void setUlazniTokOdServera(BufferedReader ulazniTokOdServera) {
		this.ulazniTokOdServera = ulazniTokOdServera;
	}
	
	public void slanjeServeruNaProveru(String username, String password) throws IOException {
		izlazniTokKaServeru.println("Sign-In");
		izlazniTokKaServeru.println(username);
		
		String serverOdgUser = ulazniTokOdServera.readLine();
		if(serverOdgUser.equals("username ne postoji")) {
			lblUsernameError.setText("username ne postoji");
		}
		else if(serverOdgUser.equals("username postoji")){
			
			izlazniTokKaServeru.println(password);
			
			String serverOdgPass = ulazniTokOdServera.readLine();
			if(serverOdgPass.equals("netacan password")) {
				lblPasswordError.setText("netacan password");
			}
			else if(serverOdgPass.equals("tacan password")) {
				this.setVisible(false);
				JFrame frame = new Chat(username, soketZaKomunkaciju, ulazniTokOdServera, izlazniTokKaServeru);
				frame.setVisible(true);
			}
			else if(serverOdgPass.equals("user vec ulogovan")) {
				lblUsernameError.setText("User vec ulogovan");
			}
		}
	}
	
	
	public boolean proveraUserPass(String username, String password) {
		if(username.length()==0) {
			lblUsernameError.setText("Prazan username");
			return false;
		}
		
		if(password.length()==0) {
			lblPasswordError.setText("Prazan password");
			return false;
		}
		
		if(username.length()<4) {
			lblUsernameError.setText("Username ne sme biti manji od 4");
			return false;
		}
		
		if(password.length()<4) {
			lblPasswordError.setText("Password ne sme biti manji od 4");
			return false;
		}
		
		return true;
		
	}
	
	public void ubrzaj() {
		txtUsername.setText("milan");
		
	}
}
