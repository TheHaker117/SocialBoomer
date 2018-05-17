package main;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.swing.JTextArea;
import javax.swing.SwingWorker;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
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
	
	private Queue<String> queue = new LinkedList<String>();
	private int countLimit = 0;
	private int waitTime = 60000;

	private SwingWorker worker = null;
	
	
	
	public Brain(JTextArea ta_log){
		this.ta_log = ta_log;
		
		queue.add("Ariel Bravo");
		queue.add("Ana Clara González");
		queue.add("Jenn Gon-Bauer");
	
		
	}
	
	public Brain(int countLimit){
		this.countLimit = countLimit;
		
		queue.add("Ariel Bravo");
		queue.add("Ana Clara González");
		queue.add("Jenn Gon-Bauer");
	
		
	}
	
	public static void main(String[] args) throws Exception {
		Brain brn = new Brain(4);
		
		brn.logIn("ale_storm@outlook.com", "343Forerunner419");
		brn.sendMessageOneByOne("No contestar");
		//brn.getFriendList(brn.temp2());
		
	}
	
	
	/**
	 * Returns 1 if the login was successful; otherwise, if the account is blocked, it returns -1
	 * 
	 * @return True if login was success, else false.
	 * @throws Exception
	 */
	public int logIn(String user, String pass) throws Exception {
		
		worker = new SwingWorker<Integer, String>(){

			@Override
			protected Integer doInBackground() throws Exception {
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
				
				if(mainpage.getTitleText().contains("Confirma tu identidad")){
					publish("\n#~ Cuenta bloqueada");
					publish("\n#~ Falló iniciar sesión...");
					return -1;
				}
					
				else{
					publish("\n#~ Inicio de sesión exitoso");
					return 1;
				}
				
			}
			
			@Override
			protected void process(List<String> chunks){
				
				for(String chunk : chunks)
					ta_log.append(chunk);
			}
			
		
		};
		
		
		worker.execute();
		
		return (Integer) worker.get();
		
	}
	
	/**
	 * Sends messages, one by one, to all destinataries in the queue
	 * 
	 * @param Message to send
	 * @throws Exception
	 */
	public void sendMessageOneByOne(String message) throws Exception{
		
		worker = new SwingWorker<Void, String>(){

			@Override
			protected Void doInBackground() throws Exception {
				
				publish("\n#~ Cargando lista de amigos...");
//				getFriendList(webClient.getPage("https://m.facebook.com/profile.php?v=friends"));
				publish("\n#~ Se encontraron " + queue.size() + " amigos en la cuenta");
				publish("\n#~ Enviando mensajes...");
				
				while(!queue.isEmpty()){
					publish(sendMessage(message, queue.poll()));
					
					publish("\n#~ Esperando " + waitTime/60000 + " minutos");
					Thread.currentThread();
					Thread.sleep(waitTime);
				}
				
				publish("\n#~ Proceso finalizado");
				publish("\n#~ Se enviaron " + countLimit + " mensajes");
				
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

			// Sends the message
			HtmlSubmitInput send = form.getInputByName("Send");
//			messenger = send.click();
				
				
//			System.out.println(messenger.asText());
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
			HtmlSubmitInput done = (HtmlSubmitInput) destinataries.getElementByName("done");
			
			dest.setValueAttribute(queue.poll());
			destinataries = search.click();
			
			
			try{
				HtmlCheckBoxInput chbx = (HtmlCheckBoxInput) destinataries.getElementByName("friend_ids[]");
				chbx.setChecked(true);
				counting++;
				
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
				System.out.println(temp.asText());
			}
				
		}
		
		try{
			HtmlAnchor anchor = friends.getAnchorByText("Ver más amigos");
			getFriendList((HtmlPage) anchor.click());
			
		}
		catch(ElementNotFoundException exc){
			System.out.println(queue.size());
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
	
	
}













