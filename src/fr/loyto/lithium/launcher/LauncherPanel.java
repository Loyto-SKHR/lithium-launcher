package fr.loyto.lithium.launcher;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import fr.litarvan.openauth.AuthenticationException;
import fr.theshark34.openlauncherlib.launcher.util.UsernameSaver;
import fr.theshark34.swinger.Swinger;
import fr.theshark34.swinger.event.SwingerEvent;
import fr.theshark34.swinger.event.SwingerEventListener;
import fr.theshark34.swinger.textured.STexturedButton;

@SuppressWarnings("serial")
public class LauncherPanel extends JPanel implements SwingerEventListener {
	
	private Image background = Swinger.getResource("background.png");
	
	private UsernameSaver saver = new UsernameSaver(Launcher.LT_INFOS);
	
	private JTextField usernameField = new JTextField(saver.getUsername(""));
	private JTextField passwordField = new JPasswordField();
	
	private STexturedButton playButton = new STexturedButton(Swinger.getResource("play.png"));
	private STexturedButton quitButton = new STexturedButton(Swinger.getResource("quit.png"));
	private STexturedButton hideButton = new STexturedButton(Swinger.getResource("hide.png"));
	
	public LauncherPanel() {
		this.setLayout(null);
		
		usernameField.setForeground(Color.white);
		usernameField.setFont(usernameField.getFont().deriveFont(20.0F));
		usernameField.setCaretColor(Color.white);
		usernameField.setOpaque(false);
		usernameField.setBorder(null);
		usernameField.setBounds(505, 233, 294, 44);
		this.add(usernameField);
		
		passwordField.setForeground(Color.white);
		passwordField.setFont(passwordField.getFont().deriveFont(20.0F));
		passwordField.setCaretColor(Color.white);
		passwordField.setOpaque(false);
		passwordField.setBorder(null);
		passwordField.setBounds(505, 340, 294, 44);
		this.add(passwordField);
		
		playButton.setBounds(602, 410);
		playButton.addEventListener(this);
		this.add(playButton);
		
		quitButton.setBounds(941, 2);
		quitButton.addEventListener(this);
		this.add(quitButton);
		
		hideButton.setBounds(907, 2);
		hideButton.addEventListener(this);
		this.add(hideButton);
	}
	
	@Override
	public void onEvent(SwingerEvent e) {
		if(e.getSource() == playButton) {
			setFieldsEnabled(false);
			
			if(usernameField.getText().replaceAll(" ", "").length() == 0 || passwordField.getText().length() == 0 ) {
				JOptionPane.showMessageDialog(this, "Erreur, veuillez entrez une addresse mail et un mot de passe valides.", "Erreur", JOptionPane.ERROR_MESSAGE);
				setFieldsEnabled(true);
				return;
			}
			
			Thread t = new Thread() {
				@Override
				public void run() {
					try {
						Launcher.auth(usernameField.getText(), passwordField.getText());
					} catch(AuthenticationException e) {
						JOptionPane.showMessageDialog(LauncherPanel.this, "Erreur, impossible de se conecter: " + e.getErrorModel().getErrorMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
						setFieldsEnabled(true);
						return;
					}
					
					System.out.println("Ca marche.");
				}
			};
			
			t.start();
		} else if(e.getSource() == quitButton) {
			System.exit(0);
		} else if(e.getSource() == hideButton) {
			LauncherFrame.getInstance().setState(JFrame.ICONIFIED);
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);
	}
	
	private void setFieldsEnabled(boolean enabled) {
		usernameField.setEnabled(enabled);
		passwordField.setEnabled(enabled);
		playButton.setEnabled(enabled);
	}
}
