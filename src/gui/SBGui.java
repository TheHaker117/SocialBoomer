package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class SBGui extends JFrame implements ActionListener{

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
		this.setSize(720, 480);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(null);
		
		try{UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}
		catch(Exception e){
			// Pending for manage this exception
		}
		
	}
	
	private void config(){
		
	}
	
	
	
	
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
	
		
	}
	
}
