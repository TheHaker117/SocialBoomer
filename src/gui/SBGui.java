package gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;

import main.Brain;

public class SBGui extends JFrame implements ActionListener{

	private JLabel l_email, l_pass, l_sendto, l_waittime;
	private JTextField tf_email;
	private JPasswordField pf_pass;
	private JTextArea ta_message, ta_log;
	private JScrollPane sp_message, sp_log;
	private JButton btn_login, btn_send, btn_cancel;
	private JComboBox cbx_sendto;
	private JSpinner spn_waittime;
	private Brain boomer;
	
	private SwingWorker worker = null;
	
	
	
	
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
		this.setSize(720, 540);
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
		
		l_waittime = new JLabel("Tiempo de espera en minutos:");
		l_waittime.setBounds(20, 190, 200, 25);
		l_waittime.setFont(this.getFont());
		
		spn_waittime = new JSpinner(new SpinnerNumberModel(1, 1, 60, 1));
		spn_waittime.setBounds(250, 190, 50, 25);
		spn_waittime.setFont(this.getFont());
		((DefaultEditor) spn_waittime.getEditor()).getTextField().setEditable(false);
		
		ta_message = new JTextArea();
		ta_message.setFont(this.getFont());
		
		sp_message = new JScrollPane(ta_message);
		sp_message.setBounds(20, 230, 280, 200);
		 
		btn_send = new JButton("Enviar");
		btn_send.setBounds(30, 450, 100, 30);
		btn_send.setFont(this.getFont());
		btn_send.addActionListener(this);
		
		btn_cancel = new JButton("Cancelar");
		btn_cancel.setBounds(180, 450, 100, 30);
		btn_cancel.setFont(this.getFont());
		btn_cancel.addActionListener(this);
		
		ta_log = new JTextArea();
		ta_log.setEditable(false);
		ta_log.setFont(new Font("Arial", Font.PLAIN, 16));
		
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
		this.add(l_waittime);
		this.add(spn_waittime);
		this.add(sp_message);
		this.add(btn_send);
		this.add(btn_cancel);
		this.add(sp_log);
		
	}
	
	
	
	
	
	@Override
	public void actionPerformed(ActionEvent evnt){
	
		
		// Login button event
		if(evnt.getSource().equals(btn_login)){
			String pass = new String(pf_pass.getPassword());
			
			if(tf_email.getText().isEmpty() || pass.isEmpty())
				JOptionPane.showMessageDialog(this, "Campos vacíos", "Advertencia", JOptionPane.WARNING_MESSAGE);
			
			else
				try{
					
					int conf =  boomer.logIn(tf_email.getText(), pass);
					
					if(conf < 0)
						JOptionPane.showMessageDialog(this, "Cuenta bloqueada", "Aviso", JOptionPane.ERROR_MESSAGE);
			
					
				} 
				catch (Exception e){
					ta_log.append("#~ <<< AN ERROR HAVE BEEN OCCURRED >>>");
					ta_log.append("\n#~ " + e.getMessage());
				}
			
			
		}
			
		// Send to checkbox event, not defined yet...
		else if(evnt.getSource().equals(cbx_sendto)) {}
		
		
		// Send button event
		else if(evnt.getSource().equals(btn_send)) {
			try {
				int val = (Integer)spn_waittime.getValue();
				boomer.setWaitTime(val * 60000);
				boomer.sendMessageOneByOne(ta_message.getText());
				
			} catch (Exception e) {
				ta_log.append("#~ <<< AN ERROR HAVE BEEN OCCURRED >>>");
				ta_log.append("\n#~ " + e.getMessage());
			}
		}
		
		else if(evnt.getSource().equals(btn_cancel))
			System.exit(1);
		
		
		
	}
	
}













