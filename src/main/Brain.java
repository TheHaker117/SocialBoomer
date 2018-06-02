package main;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlFileInput;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;

public class Brain{
	
	private WebClient webClient;
	private HtmlPage mainpage;
	private JTextArea ta_log;
	private SwingWorker<?, String> worker = null;
	
	private int countLimit = 0;
	private int waitTime = 60000;

	private boolean isStopped = false;
	private boolean isPhotoSelected = false; 
	
	private	String photopath = null;
	private String msg = null;
	
	private Queue<String> queue = new LinkedList<String>();
	private List<String[]> exl_sended = new ArrayList<String[]>();
	private List<String[]> exl_friends = new ArrayList<String[]>();
	private List<String> sended = new ArrayList<String>();
	private String[] exclusions = null;
	
	// License
	private final String date_build = "2018-05-30";
	private  String date_run = null; 
	private final int EXPIRATION_DAYS = 15;
	
	
	
	public Brain(JTextArea ta_log){
		
		this.ta_log = ta_log;
		
	}

	/**
	 * 
	 * Verify is the license still being valid
	 * 
	 * @return
	 * @throws Exception
	 */
	
	public int checkLicense() throws Exception{

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		
		Date build_date = dateFormat.parse(date_build);
		date_run = dateFormat.format(cal.getTime());
		Date nowdays = dateFormat.parse(date_run);

		int remain = (int) ((nowdays.getTime() - build_date.getTime())/86400000);
		
		if(remain > EXPIRATION_DAYS)
			return -1;
		else
			return 1;
	}
	
	
	
	/* Try this to avoid warnings:
	 * 
	 * 	webClient.getOptions().setTimeout(20000);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setRedirectEnabled(true);
	 * 
	 */
	
	
	
	
	
	/**
	 * Returns 1 if the log in was successful; otherwise, if the account is blocked, it returns -1
	 * 
	 * @throws Exception
	 */
	public void logIn(String user, String pass) throws Exception {
		
		worker = new SwingWorker<Void, String>(){

			@Override
			protected Void doInBackground() throws Exception {
				
				publish("\n#~ Iniciando...");
				
				// Only works with Firefox
				webClient = new WebClient(BrowserVersion.FIREFOX_52);
				webClient.setJavaScriptEngine(new JavaScriptEngine(webClient));
				
				// Main page
				HtmlPage page = webClient.getPage("https://m.facebook.com/");
				// Login form
				HtmlForm form = (HtmlForm) page.getElementById("login_form");
				// Email textfield
				HtmlTextInput tmail = (HtmlTextInput) page.getElementByName("email");
				// Password textfield
				HtmlPasswordInput tpass = (HtmlPasswordInput) page.getElementByName("pass");
				// Login button
				HtmlSubmitInput blogin = (HtmlSubmitInput) form.getInputByValue("Log In");
				
				tmail.setValueAttribute(user);
				tpass.setValueAttribute(pass);
				
				
				mainpage = blogin.click();
				
				System.out.println(mainpage.asText());
				
				
				if(mainpage.asText().contains("Confirma tu identidad")
						|| mainpage.asText().contains("Confirm your identity")){
					publish("\n#~ Cuenta bloqueada"
							+ "\n#~ Falló iniciar sesión...");
					
				}
				
				else if(mainpage.asText().contains("Ingresar número de celular")
						|| mainpage.asText().contains("Enter Mobile Number")){
					publish("\n#~ Cuenta bloqueada: Confirmar número celular"
							+ "\n#~ Falló iniciar sesión...");
					
				}
				
				else if(mainpage.asText().contains("Tu cuenta ha sido desactivada")
						|| mainpage.asText().contains("Your account has been disabled")){
					publish("\n#~ Cuenta inhabilitada"
							+ "\n#~ Falló iniciar sesión...");
				}
				
				else if(mainpage.asText().contains("La contraseña que has introducido es incorrecta")
						|| mainpage.asText().contains("The password you entered is incorrect")){
					publish("\n#~ Contraseña incorrecta");
				}
				
				else{
					
					
					publish("\n#~ Inicio de sesión exitoso"
							+ "\n#~ Cargando lista de amigos...");
					
					getFriendList(webClient.getPage("https://m.facebook.com/profile.php?v=friends"));
					
					publish("\n#~ Se encontraron " + queue.size() + " amigos en la cuenta");
					
				}
				return null;
				
			}
			
			@Override
			protected void process(List<String> chunks){
				
				for(String chunk : chunks)
					ta_log.append(chunk);
			}
			
		
		};
		
		
		worker.execute();
		
	}
	
	/**
	 * Sends messages, one by one, to all destinataries in the queue
	 * 
	 * @param Message to send
	 * @throws Exception
	 */
	public void sendMessageOneByOne(String message) throws Exception{
		
		msg = message;
		
		worker = new SwingWorker<Void, String>(){

			@Override
			protected Void doInBackground() throws Exception {
				
				
				publish("\n#~ Enviando mensajes...");
				
				while(!queue.isEmpty() && !isStopped){
					
					if(!isExclusion(queue.peek())){
						// Adding to the sended list
						sended.add(queue.peek());
						
						// Send message and publish the log
						publish(sendMessage(message, queue.poll()));
						publish("\n#~ Mensajes enviados: " + sended.size()
								+ "\n#~ Restantes: " + queue.size());
						
						
						// Wait time
						if(!queue.isEmpty()){
							publish("\n#~ Esperando " + waitTime/60000 + " minutos");
							Thread.currentThread();
							Thread.sleep(waitTime);
						}
						
					}
					// If is an exclusion, polls the element from the queue and publish the log
					else
						publish("\n#~ Exclusión: " + queue.poll());
					
					
				}
				
				if(isStopped){
					publish("\n#~ Proceso detenido...");
					publish("\n#~ Se enviaron " + countLimit + " mensajes");
				
				}
				
				else{
					publish("\n#~ Proceso finalizado");
					publish("\n#~ Se enviaron " + countLimit + " mensajes");
					
				}
				
				return null;
			}
			
			@Override
			protected void process(List<String> chunks){
				
				for(String chunk : chunks)
					ta_log.append(chunk);
			}
			
			
		};
		
		worker.execute();
		
	}
	
	
	/**
	 * 
	 * Sends a message to a single friend
	 * 
	 * 
	 * @param message
	 * @param friend
	 * @return 
	 * @throws Exception
	 */
	private String sendMessage(String message, String friend) throws Exception{
		HtmlPage messenger = webClient.getPage("https://m.facebook.com/messages/compose/");
		
		
		// Seeks the "Anadir destinatarios" anchor
		Iterator<HtmlAnchor> anchors = messenger.getAnchors().iterator();
		HtmlAnchor anchor = null;
		
		while(anchors.hasNext()){
			anchor = anchors.next();
			if(anchor.getAttribute("href").contains("/friends/selector/")){
				break;
			}
		}
		
			
		HtmlPage destinataries = anchor.click();
		
		HtmlTextInput dest = (HtmlTextInput) destinataries.getElementByName("query");
		HtmlSubmitInput search = (HtmlSubmitInput) destinataries.getElementByName("search");
			
		dest.setValueAttribute(friend);
		destinataries = search.click();
			
		try{
			HtmlCheckBoxInput chbx = (HtmlCheckBoxInput) destinataries.getElementByName("friend_ids[]");
			chbx.setChecked(true);
		
			// This must be here!!!
			HtmlSubmitInput done = (HtmlSubmitInput) destinataries.getElementByName("done");
			
			messenger = done.click();
	
			HtmlForm form = (HtmlForm) messenger.getElementById("composer_form");
				
			// Sets the message in the textarea
			HtmlTextArea textarea = form.getTextAreaByName("body");
			textarea.setText(message);
			
			if(isPhotoSelected){
				HtmlFileInput input = (HtmlFileInput) messenger.getElementByName("file1");
				input.setValueAttribute(photopath);
				input.setContentType("multipart/form-data");
				
				Iterator<HtmlForm> forms = messenger.getForms().iterator();
				HtmlSubmitInput sendphoto = null;
				
				while(forms.hasNext()){
					HtmlForm photoform = forms.next();
					if(photoform.getActionAttribute().contains("upload.facebook.com")){
						sendphoto = photoform.getInputByValue("Enviar fotos");
						System.out.println(messenger.asText());
						sendphoto.click();
						break;
					}
				}
				
				
			}
				

			// Sends the message
			HtmlSubmitInput send = form.getInputByName("Send");
			messenger = send.click();
				
				
//			System.out.println(messenger.asText());
			countLimit++;
			return "\n#~ >>> Message sended to: "  + friend;
				
		}
			
		catch(ElementNotFoundException exc){
			// Pending
			return "\n#~ >>> Element not found exception: Checkbox not found";		
		}

	}
	
	
	
	
	
	
	/**
	 * This method saves every check box previously selected, and I don't know why!!! 
	 * But that is what I wanted! LOL
	 * 
	 * @param message
	 * @throws Exception
	 */
	public void sendMessageByGroup(String message) throws Exception{
		// Load friend list, i mean, friend's name queue
	//	getFriendList(webClient.getPage("https://m.facebook.com/profile.php?v=friends"));
		
		
		HtmlPage messenger = webClient.getPage("https://m.facebook.com/messages/compose/");
		
		Iterator<HtmlAnchor> anchors = messenger.getAnchors().iterator();
		HtmlAnchor anchor = null;
		
		while(anchors.hasNext()){
			anchor = anchors.next();
			if(anchor.getAttribute("href").contains("/friends/selector/"))
				break;
		}
		
		HtmlPage destinataries = anchor.click();
		int counting = 0;
		
		
		while(!queue.isEmpty() && counting < countLimit){
			
			HtmlTextInput dest = (HtmlTextInput) destinataries.getElementByName("query");
			HtmlSubmitInput search = (HtmlSubmitInput) destinataries.getElementByName("search");
			
			
			dest.setValueAttribute(queue.poll());
			destinataries = search.click();
			
			
			try{
				HtmlCheckBoxInput chbx = (HtmlCheckBoxInput) destinataries.getElementByName("friend_ids[]");
				chbx.setChecked(true);
				counting++;
				
				HtmlSubmitInput done = (HtmlSubmitInput) destinataries.getElementByName("done");
				messenger = done.click();
				
				System.out.println(messenger.asText());
				System.out.println(queue.isEmpty());
				
					
			}
			catch(ElementNotFoundException exc){
				// Pending
				
				System.out.println(">>> Element not found exception: Checkbox not found");
				
				continue;
			}
			
			
			
		}
		
		
		if(counting > 0 && counting <= countLimit){
			HtmlForm form = (HtmlForm) messenger.getElementById("composer_form");
			
			// Sets the message in the textarea
			HtmlTextArea textarea = form.getTextAreaByName("body");
			textarea.setText(message);

			System.out.println(messenger.asText());
			
			// Sends the message
			HtmlSubmitInput send = form.getInputByName("Send");
//			messenger = send.click();
			
			System.out.println(">>> Message sended");
			
			System.out.println(messenger.asText());
		}
		
		
	}
	
	
	/**
	 * Fills a empty queue with a friend's name list
	 * 
	 * @param friends
	 * @throws Exception
	 */
	public void getFriendList(HtmlPage friends) throws Exception {
		
		Iterator<HtmlAnchor> ite = friends.getAnchors().iterator();
		
		while(ite.hasNext()){
			HtmlAnchor temp = ite.next();
			if(temp.getAttribute("href").contains("fr_tab")){
				queue.add(temp.asText());
				// [Friend name, facebook link]
				exl_friends.add(new String[] {temp.asText(), "https://www.facebook.com" + temp.getHrefAttribute()});
			}
		}
		
		try{
			
			HtmlAnchor anchor = friends.getAnchorByText("Ver más amigos");
			getFriendList((HtmlPage) anchor.click());
			
		}
		catch(ElementNotFoundException exc){
			System.out.println("No se encontró el jodido botón");
		}
	}
	
	/**
	 * Returns the selected destinataries count
	 * 
	 * @return countLimit
	 */
	public int getCountLimit(){
		return countLimit;
	}
	
	
	/**
	 * Sets the thread time to wait between each message sended
	 * @param time
	 */
	public void setWaitTime(int time){
		waitTime = time;
	}
	
	/**
	 * Stops the messages sending
	 * 
	 */
	public void pause(){
		isStopped = true;
		
	}
	
	/**
	 * Resumes the messages sending
	 * 
	 */
	public void resume(){
		isStopped = false;
	}
	
	
	/**
	 * Sets the exclusion list with a given list
	 * 
	 * @param exclist
	 */
	private void setExclusionList(String[] exclist){
		exclusions = exclist.clone();
	}
	
	
	/**
	 * Return true is the name is in the exclusion list
	 * 
	 * @param name
	 * @return
	 */
	
	private boolean isExclusion(String name){
		
		if(exclusions != null)
			for(int i = 0; i < exclusions.length; i++){
				if(exclusions[i].contains(name))
					return true;
			}
		
		return false;
		
	}
	
	/**
	 * It must generates the excel report, at least, generates the information
	 * 
	 */
	
	public void generateReport(String exlpath, JFrame parent){
		
		worker = new SwingWorker<Void, String>(){

			@Override
			protected Void doInBackground() throws Exception {
				
				publish("\n#~ Exportando datos a excel...");
		
				String username = ((HtmlPage) webClient.getPage("https://m.facebook.com/profile.php")).getTitleText();
				
				ExcelReportFormat excl = new ExcelReportFormat(username, date_run, msg, "VISTO");
				
				setExlSendedList();
				
				System.out.println(exl_sended.size());
				
				excl.createExcel(exl_sended);
				excl.writeExcel(exlpath);
				
				publish("\n#~ Archivo producido satisfactoriamente");
				
				return null;
			}
			
			@Override
			protected void process(List<String> chunks){
				
				for(String str : chunks){
					ta_log.append(str);
				}
			}
			
			@Override
			protected void done(){
				JOptionPane.showMessageDialog(parent, "Se ha producido el archivo", 
						"Proceso finalizado", JOptionPane.INFORMATION_MESSAGE);
				
			}
			
		};
		
		worker.execute();
		
		
	}
	
	
	public void setExlSendedList(){
		
		exl_sended = new ArrayList<String[]>();
		Iterator<String> ite = sended.iterator();
		
		while(ite.hasNext()){
			String name = ite.next();
			exl_sended.add(new String[] {name, getLink(name)});
		}
		
	}
	
	private String getLink(String name){
		
		for(int i = 0; i < exl_friends.size(); i++){
			if(exl_friends.get(i)[0].contains(name))
				return exl_friends.get(i)[1];
		}
		
		return null;
		
	}
	
	
	
	/**
	 * Close the webClient and thus log out the session
	 * 
	 */
	public void logOut(){
		webClient.close();
		
		queue = new LinkedList<String>();
		sended = new ArrayList<String>();
		exclusions = null;
		countLimit = 0;
	}
	
	
	public void setPhoto(String path){
		
		isPhotoSelected = true;
		photopath = path.replace("\\", "/");
		
	}
	
	
	/**
	 * Resets the friend list loading the friends page
	 * 
	 * @throws Exception
	 */
	public void reload() throws Exception{
		
		worker = new SwingWorker<Void, String>(){

			@Override
			protected Void doInBackground() throws Exception {
				
				queue = new LinkedList<String>();
				
				publish("\n#~ Cargando lista de amigos...");
				
				getFriendList(webClient.getPage("https://m.facebook.com/profile.php?v=friends"));
				
				publish("\n#~ Se encontraron " + queue.size() + " amigos en la cuenta");
				
				return null;
			}
			
			@Override
			protected void process(List<String> chunks){
				
				for(String chunk : chunks)
					ta_log.append(chunk);
				
			}
			
		};
		
		worker.execute();
			
		
	}
	
	/**
	 * Generates a temp file with the sended list data
	 * 
	 * @param path
	 */
	public void makeTemporaryFiles(String path){
		
		worker = new SwingWorker<Void, String>(){

			@Override
			protected Void doInBackground() throws Exception {

				try{
					
					publish("\n#~ Generando archivo sended_data.tmp");
					
					ArrayList<String> saveprocess = new ArrayList<String>();
					
					// Adding the exclusions list
					if(exclusions != null)
						saveprocess.addAll(Arrays.asList(exclusions));
					
					// Adding the sended list
					if(!sended.isEmpty())
						saveprocess.addAll(Arrays.asList(sended.toArray(new String[] {})));
					
					
					File temp = new File(path.replace("\\", "/") + "/sended_data.tmp");
					
					BufferedWriter writer = new BufferedWriter(new FileWriter(temp));
					
					writer.write(Arrays.toString(toByteArray(saveprocess)));
					writer.close();
					
					publish("\n#~ Archivo generado satisfactoriamente");
					
				}catch(Exception exc){
					publish("\n#~ <<< NO SE PUDO CREAR sended_data.tmp >>>"
							+ "\n#~ " + exc.getMessage());
				}
				
				return null;
			}
			
			@Override
			protected void process(List<String> chunks){
				
				for(String chunk : chunks)
					ta_log.append(chunk);
			}
			
			
		};
		
		worker.execute();
	
		
	}
	
	/**
	 * Sets the exclusion list with the temp file data
	 * 
	 * @param path
	 */
	public void loadSavedData(String path){
		
		worker = new SwingWorker<Void, String>(){

			@Override
			protected Void doInBackground() throws Exception {
			
				String strb = Files.readAllLines(Paths.get(path.replace("\\", "/"))).get(0);
				strb = strb.replace("[", "").replace("]", "").replace(" ", "");
				String[] array = strb.split(",");
				byte[] bytes = new byte[array.length];
				
				for(int i = 0; i < array.length; i++)
					bytes[i] = (Byte.valueOf(array[i]));
				
				
				ArrayList<String> list = (ArrayList<String>) toObject(bytes);
				
				setExclusionList(list.toArray(new String[] {}));
				
				return null;
			}
			
			@Override
			protected void process(List<String> chunks){
				
				for(String chunk : chunks)
					ta_log.append(chunk);
			}
			
		};
		
		worker.execute();
		
	}
	
	
	/**
	 * Makes a conversion from a Java Object to a bytes array
	 * 
	 * @param obj
	 * @return bytes array
	 * @throws IOException
	 */
	private byte[] toByteArray(Object obj) throws IOException {
        byte[] bytes = null;
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        
        try{
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
            
        } finally{
            if(oos != null)
                oos.close();
            
            if(bos != null)
                bos.close();
            
        }
        
        return bytes;
    }
	
	
	/**
	 * Makes a conversion from a bytes array to a Java Object
	 * 
	 * @param bytes
	 * @return	Object
	 * @throws Exception
	 */
	public Object toObject(byte[] bytes) throws Exception{
		
		Object obj = null;
		ByteArrayInputStream bis = null;
		ObjectInputStream ois = null;
		
		try{
			bis = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(bis);
			obj = ois.readObject();
		} finally{
			if(bis != null)
				bis.close();
			
			if(ois != null)
				ois.close();
			
		}
		
		return obj;
	}
	
	
	
	
}













