package rs.ac.bg.fon.nprog.chatapp;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import rs.ac.bg.fon.nprog.chatapp.client.SignIn;
import rs.ac.bg.fon.nprog.chatapp.server.Server;

public class SignInTest {
	private SignIn signIn;
	
	
	
	@Before
	public void setUp() throws Exception {
		
		signIn = new SignIn();
		
		
	}

	@After
	public void tearDown() throws Exception {
		diskonektovanje();
		signIn = null;
		
		
	}

	@Test
	public void testProveraUserPassPrazanUsername() {
		assertEquals(false, signIn.proveraUserPass("", "nesto"));
		
	}

	@Test
	public void testProveraUserPassPrazanUsernameLblError() {
		signIn.proveraUserPass("", "nesto");
		assertEquals(true, signIn.getLblUsernameError().getText().equals("Prazan username"));
	}

	@Test
	public void testProveraUserPassPrazanPassword() {
		assertEquals(false, signIn.proveraUserPass("nesto", ""));
	}

	@Test
	public void testProveraUserPassPrazanPasswordLblError() {
		signIn.proveraUserPass("nesto", "");
		assertEquals(true, signIn.getLblPasswordError().getText().equals("Prazan password"));
	}

	@Test
	public void testProveraUserPassUsernameManjeOd4() {
		assertEquals(false, signIn.proveraUserPass("abc", "nesto"));
	}

	@Test
	public void testProveraUserPassPasswordManjeOd4() {
		assertEquals(false, signIn.proveraUserPass("nesto", "abc"));
	}

	@Test
	public void testProveraUserPassUsernameManjeOd4LblError() {
		signIn.proveraUserPass("abc", "nesto");

		assertEquals(true, signIn.getLblUsernameError().getText().equals("Username ne sme biti manji od 4"));
	}

	@Test
	public void testProveraUserPassPasswordManjeOd4LblError() {
		signIn.proveraUserPass("nesto", "abc");

		assertEquals(true, signIn.getLblPasswordError().getText().equals("Password ne sme biti manji od 4"));
	}

	@Test
	public void testUsernameNePostoji() {
	
		konektovanje();
		
		signIn.getTxtUsername().setText("dragan");

		signIn.getTxtPassword().setText("dragan");

		signIn.getBtnSignIn().doClick();

		assertEquals(true, signIn.getLblUsernameError().getText().equals("username ne postoji"));
		diskonektovanje();
	}
	
	@Test
	public void testSifraNetacna() {
	
		konektovanje();
		
		signIn.getTxtUsername().setText("milan");

		signIn.getTxtPassword().setText("dragan");

		signIn.getBtnSignIn().doClick();

		assertEquals(true, signIn.getLblPasswordError().getText().equals("netacan password"));
		diskonektovanje();
	}
	
	

	
	public void konektovanje() {
		int port = 2222;
		
		try {
			signIn.setSoketZaKom(new Socket("localhost", port)); 
			signIn.setIzlazniTokKaServeru(new PrintStream(signIn.getSoketZaKom().getOutputStream()));
			signIn.setUlazniTokOdServera(new BufferedReader(new InputStreamReader(signIn.getSoketZaKom().getInputStream())));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void diskonektovanje() {
		
		try {
			signIn.getSoketZaKom().close();
			signIn.getIzlazniTokKaServeru().close();
			signIn.getUlazniTokOdServera().close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
}
