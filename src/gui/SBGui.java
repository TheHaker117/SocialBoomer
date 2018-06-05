/**
 * TheHaker117 License
 * SocialBoomer 1.0.0
 * 
 * Copyright 2018 Ariel Bravo
 * 
 * This code was developed by Ariel Bravo (TheHaker117) on May - June 2018
 * 
 * The purpose of this software is strictly educative, cannot be use to lucrative or malicious purposes.
 * 
 * Last build Saturday June 02 2018
 * 
 */

package gui;

import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
import javax.swing.filechooser.FileNameExtensionFilter;

import main.Brain;

public class SBGui extends JFrame implements ActionListener{

	private JLabel l_email, l_pass, l_sendto, l_waittime;
	private JTextField tf_email;
	private JPasswordField pf_pass;
	private JTextArea ta_message, ta_log;
	private JScrollPane sp_message, sp_log;
	private JButton btn_login, btn_logout, btn_photo, btn_send,
					btn_pause, btn_reload, btn_save, btn_load,
					btn_clear, btn_export, btn_exit;
	private JComboBox cbx_sendto;
	private JSpinner spn_waittime;
	private JPanel pnl_login, pnl_log;
	
	private SwingWorker worker = null;
	
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
		this.setSize(840, 500);
		this.setResizable(false);
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
		
		btn_logout = new JButton("Log out");
		btn_logout.setBounds(100, 95, 80, 30);
		btn_logout.setFont(this.getFont());
		btn_logout.addActionListener(this);
		btn_logout.setEnabled(false);
		
		
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
		ta_message.setToolTipText("Escribir mensaje...");
		
		
		sp_message = new JScrollPane(ta_message);
		sp_message.setBounds(20, 230, 280, 200);
		 
		btn_photo = new JButton("+");
		btn_photo.setBounds(310, 240, 30, 30);
		btn_photo.setMargin(new Insets(0, 0, 0, 0));
		btn_photo.setFont(new Font("Arial", Font.PLAIN, 17));
		btn_photo.setToolTipText("Añadir foto");
		btn_photo.addActionListener(this);
		
		pnl_login = new JPanel();
		pnl_login.setBounds(0, 0, 345, 470);
		pnl_login.setLayout(null);
		
		pnl_login.add(l_email);
		pnl_login.add(tf_email);
		pnl_login.add(l_pass);
		pnl_login.add(pf_pass);
//		this.add(new JSeparator(SwingConstants.HORIZONTAL));
		pnl_login.add(btn_login);
		pnl_login.add(btn_logout);
		pnl_login.add(l_sendto);
		pnl_login.add(cbx_sendto);
		pnl_login.add(l_waittime);
		pnl_login.add(spn_waittime);
		pnl_login.add(sp_message);
		pnl_login.add(btn_photo);
		
		
		ta_log = new JTextArea();
		ta_log.setEditable(false);
		ta_log.setFont(new Font("Arial", Font.PLAIN, 16));
		
		sp_log = new JScrollPane(ta_log);
		sp_log.setBounds(0, 20, 330, 410);
		

		btn_send = new JButton("Enviar");
		btn_send.setBounds(350, 20, 100, 30);
		btn_send.setFont(this.getFont());
		btn_send.addActionListener(this);
		
		btn_pause = new JButton("Pausar");
		btn_pause.setBounds(350, 60, 100, 30);
		btn_pause.setFont(this.getFont());
		btn_pause.addActionListener(this);
		
		btn_reload = new JButton("Recargar");
		btn_reload.setBounds(350, 160, 100, 30);
		btn_reload.setFont(this.getFont());
		btn_reload.setToolTipText("Recargar lista de amigos");
		btn_reload.addActionListener(this);
		
		btn_save = new JButton("Guardar");
		btn_save.setBounds(350, 200, 100, 30);
		btn_save.setFont(this.getFont());
		btn_save.setToolTipText("Guardar proceso");
		btn_save.addActionListener(this);
		
		btn_load = new JButton("Cargar...");
		btn_load.setBounds(350, 240, 100, 30);
		btn_load.setFont(this.getFont());
		btn_load.setToolTipText("Cargar lista de exclusiones");
		btn_load.addActionListener(this);
		
		
		btn_clear = new JButton("Clear");
		btn_clear.setBounds(350, 280, 100, 30);
		btn_clear.setFont(this.getFont());
		btn_clear.setToolTipText("Limpiar Log");
		btn_clear.addActionListener(this);
		
		btn_export = new JButton("Exportar");
		btn_export.setBounds(350, 320, 100, 30);
		btn_export.setFont(this.getFont());
		btn_export.setToolTipText("Exportar a excel");
		btn_export.addActionListener(this);
		
		btn_exit = new JButton("Salir");
		btn_exit.setBounds(350, 400, 100, 30);
		btn_exit.setFont(this.getFont());
		btn_exit.addActionListener(this);
		
		
		
		pnl_log = new JPanel();
		pnl_log.setBounds(350, 0, 480, 470);
		pnl_log.setLayout(null);
		
		pnl_log.add(sp_log);
		pnl_log.add(btn_send);
		pnl_log.add(btn_pause);
		pnl_log.add(btn_reload);
		pnl_log.add(btn_save);
		pnl_log.add(btn_load);
		pnl_log.add(btn_clear);
		pnl_log.add(btn_export);
		pnl_log.add(btn_exit);
		
		
		setEnabledButtons(false);
		
		boomer = new Brain(ta_log);
		
		
		
		this.add(pnl_login);
		this.add(pnl_log);
		
		
		try{
			if(boomer.checkLicense() < 0){
				JOptionPane.showMessageDialog(this, "Verificar periodo de licencia. "
						+ "Obtenga una nueva versión renovada. Consulte al desarrollador. "
						+ "\n ~ CS - TH117", "Licencia expirada", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
		}catch(Exception exc){
			JOptionPane.showMessageDialog(this, "No se pudo verificar el periodo de licencia. "
					+ "\nConsulte al desarrollador. "
					+ "\n ~ CS - TH117", "Error al verificar licencia", JOptionPane.ERROR_MESSAGE);
		
			System.exit(0);
		}
		
		
		
		
		
	}
	
	
	
	private void setEnabledButtons(boolean value){
		
		btn_login.setEnabled(!value);
		btn_logout.setEnabled(value);
		btn_send.setEnabled(value);
		btn_pause.setEnabled(value);
		btn_reload.setEnabled(value); 
		btn_save.setEnabled(value); 
		btn_load.setEnabled(value);
		btn_clear.setEnabled(value);
		btn_export.setEnabled(value);
		
		
		
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
					
					setEnabledButtons(true);
					boomer.logIn(tf_email.getText(), pass);
					
				} 
				catch (Exception e){
					setEnabledButtons(false);
					ta_log.append("#~ <<< AN ERROR HAVE BEEN OCCURRED >>>");
					ta_log.append("\n#~ " + e.getMessage());
				}
			
			
		}
		
		else if(evnt.getSource().equals(btn_logout)){
			boomer.logOut();
			ta_log.append("\n#~ <<< Sesión cerrada >>>");
			
			tf_email.setText("");
			pf_pass.setText("");
			
			setEnabledButtons(false);
		}
			
			
		// Send to checkbox event, not defined yet...
		else if(evnt.getSource().equals(cbx_sendto)) {}
		
		
		// Send button event
		else if(evnt.getSource().equals(btn_send)) {
			try{
				
				btn_send.setEnabled(false);
				
				int val = (Integer)spn_waittime.getValue();
				boomer.setWaitTime(val * 60000);
				boomer.resume();
				
				boomer.sendMessageOneByOne(ta_message.getText());
					
				
			} catch (Exception e) {
				ta_log.append("#~ <<< AN ERROR HAVE BEEN OCCURRED >>>");
				ta_log.append("\n#~ " + e.getMessage());
			}
		}
		
		else if(evnt.getSource().equals(btn_pause)){
			btn_send.setEnabled(true);
			boomer.pause();
		}
		
		else if(evnt.getSource().equals(btn_reload)){
			btn_send.setEnabled(true);
			
			
			try {
				boomer.reload();
			} catch (Exception e) {
				ta_log.append("#~ <<< AN ERROR HAVE BEEN OCCURRED >>>");
				ta_log.append("\n#~ " + e.getMessage());
			}
		}
		
		else if(evnt.getSource().equals(btn_save)){
			
			JFileChooser chooser = new JFileChooser();
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int val = chooser.showSaveDialog(this);
			
			
			if(val == JFileChooser.APPROVE_OPTION)
				boomer.makeTemporaryFiles(chooser.getSelectedFile().getPath());
			
			else if(val == JFileChooser.CANCEL_OPTION)
				JOptionPane.showMessageDialog(this, "No se ha seleccionado ningún directorio",	"Advertencia", JOptionPane.WARNING_MESSAGE);
			
			else 
				JOptionPane.showMessageDialog(this, "No se ha podido guardar el archivo",	"Error", JOptionPane.ERROR_MESSAGE);
			
		}
		
		else if(evnt.getSource().equals(btn_load)){
			
			JFileChooser chooser = new JFileChooser();
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			chooser.setFileFilter(new FileNameExtensionFilter("Temporary Files", "tmp"));
			int val = chooser.showOpenDialog(this);
			
			if(val == JFileChooser.APPROVE_OPTION)
				boomer.loadSavedData(chooser.getSelectedFile().getPath());
			
			else if(val == JFileChooser.CANCEL_OPTION)
				JOptionPane.showMessageDialog(this, "No se ha seleccionado ningún archivo",	"Advertencia", JOptionPane.WARNING_MESSAGE);

			else 
				JOptionPane.showMessageDialog(this, "No se ha podido cargar el archivo",	"Error", JOptionPane.ERROR_MESSAGE);
			
		}
		
		
		
		
		else if(evnt.getSource().equals(btn_clear))
			ta_log.setText("");
		
		else if(evnt.getSource().equals(btn_export)) {
			JFileChooser chooser = new JFileChooser();
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int val = chooser.showSaveDialog(this);
			
			
			if(val == JFileChooser.APPROVE_OPTION)
				boomer.generateReport(chooser.getSelectedFile().getPath(), ta_message.getText(),  this);
			
			else if(val == JFileChooser.CANCEL_OPTION)
				JOptionPane.showMessageDialog(this, "No se ha seleccionado ningún directorio",	"Advertencia", JOptionPane.WARNING_MESSAGE);
			
			else 
				JOptionPane.showMessageDialog(this, "No se ha podido guardar el archivo",	"Error", JOptionPane.ERROR_MESSAGE);
			
		}
			
		
		else if(evnt.getSource().equals(btn_exit))
			System.exit(1);
		
		
		
		
		
		else if(evnt.getSource().equals(btn_photo)){
			JFileChooser chooser = new JFileChooser();
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			chooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "jpeg", "gif", "png"));
			int val = chooser.showOpenDialog(this);
			
			if(val == JFileChooser.APPROVE_OPTION){
				File photo = chooser.getSelectedFile();
				
				ta_log.append("\n#~ Foto cargada: " +  photo.getName());
				
				boomer.setPhoto(photo.getPath());
				
			}
	
			else if(val == JFileChooser.CANCEL_OPTION)
				JOptionPane.showMessageDialog(this, "No se ha seleccionado ningún archivo",	"Advertencia", JOptionPane.WARNING_MESSAGE);

			else 
				JOptionPane.showMessageDialog(this, "No se ha podido cargar el archivo",	"Error", JOptionPane.ERROR_MESSAGE);
			
			
			
			
			
			
		}
		
	}
	
}













