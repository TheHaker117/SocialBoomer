package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import main.Brain;

public class SBGui extends JFrame implements ActionListener{

	private JLabel l_email, l_pass, l_sendto;
	private JTextField tf_email;
	private JPasswordField pf_pass;
	private JTextArea ta_message, ta_log;
	private JScrollPane sp_message, sp_log;
	private JButton btn_login, btn_send, btn_cancel;
	private JComboBox cbx_sendto;
	private Brain boomer;
	
	
	
	
	public SBGui(){
		super();
		init();
		config();
	}
	
	public static void main(String[] args){
		
		
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				SBGui sb = new SBGui();
				sb.setVisible(true);
				
			}
			
		});
		
	}
	
	private void init(){
		this.setTitle("SocialBoomer by TheHaker117");
		this.setSize(720, 500);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(null);
		this.setFont(new Font("Arial", Font.PLAIN, 15));
		
		
		try{UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}
		catch(Exception e){
			// Pending for manage this exception
		}
		
	}
	
	private void config(){
		
		l_email = new JLabel("Email:");
		l_email.setBounds(20, 20, 50, 25);
		l_email.setFont(this.getFont());
		
		tf_email = new JTextField();
		tf_email.setBounds(100, 20, 200, 25);
		tf_email.setFont(this.getFont());
		
		l_pass = new JLabel("Password:");
		l_pass.setBounds(20, 60, 70, 25);
		l_pass.setFont(this.getFont());
		
		pf_pass = new JPasswordField();
		pf_pass.setBounds(100, 60, 200, 25);
		
		btn_login = new JButton("Log In");
		btn_login.setBounds(220, 95, 80, 30);
		btn_login.setFont(this.getFont());
		btn_login.addActionListener(this);
		
		l_sendto = new JLabel("Enviar a:");
		l_sendto.setBounds(20, 150, 60, 25);
		l_sendto.setFont(this.getFont());
		
		cbx_sendto = new JComboBox<String>(new String[] {"Todos", "Solo algunos"});
		cbx_sendto.setBounds(100, 150, 150, 25);
		cbx_sendto.setFont(this.getFont());
		cbx_sendto.setEnabled(false);
		cbx_sendto.addActionListener(this);
		
		ta_message = new JTextArea();
		ta_message.setFont(this.getFont());
		
		sp_message = new JScrollPane(ta_message);
		sp_message.setBounds(20, 190, 280, 200);
		 
		btn_send = new JButton("Enviar");
		btn_send.setBounds(30, 410, 100, 30);
		btn_send.setFont(this.getFont());
		btn_send.addActionListener(this);
		
		btn_cancel = new JButton("Cancelar");
		btn_cancel.setBounds(180, 410, 100, 30);
		btn_cancel.setFont(this.getFont());
		btn_cancel.addActionListener(this);
		
		ta_log = new JTextArea();
		ta_log.setFont(new Font("Arial", Font.PLAIN, 16));
		ta_log.setBackground(Color.DARK_GRAY);
		ta_log.setForeground(Color.WHITE);
		
		sp_log = new JScrollPane(ta_log);
		sp_log.setBounds(350, 20, 330, 400);
		
		boomer = new Brain(ta_log);
		
		
		
		
		this.add(l_email);
		this.add(tf_email);
		this.add(l_pass);
		this.add(pf_pass);
//		this.add(new JSeparator(SwingConstants.HORIZONTAL));
		this.add(btn_login);
		this.add(l_sendto);
		this.add(cbx_sendto);
		this.add(sp_message);
		this.add(btn_send);
		this.add(btn_cancel);
		this.add(sp_log);
		
	}
	
	
	
	
	
	@Override
	public void actionPerformed(ActionEvent evnt){
	
		if(evnt.getSource().equals(btn_login))
			try{
				boomer.logIn(tf_email.getText(), pf_pass.getPassword().toString());
			} 
			catch (Exception e){
				ta_log.append("#~ <<< AN ERROR HAVE BEEN OCCURRED >>>");
				ta_log.append("\n#~ " + e.getMessage());
			}
			
		else if(evnt.getSource().equals(cbx_sendto)) {}
		
		else if(evnt.getSource().equals(btn_send)) {
			try {
				
				boomer.sendMessage(ta_message.getText());
			} catch (Exception e) {
				ta_log.append("#~ <<< AN ERROR HAVE BEEN OCCURRED >>>");
				ta_log.append("\n#~ " + e.getMessage());
			}
		}
			
		
		
		
	}
	
}













