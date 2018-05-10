package main;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JTextArea;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
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
	
	private Queue<String> queue;
	
	
	public Brain(JTextArea ta_log) {
		this.ta_log = ta_log;
	}
	
	public Brain() {
	
	}
	
	public static void main(String[] args) throws Exception {
		Brain brn = new Brain();
		
		brn.logIn("ale_storm@outlook.com", "343Forerunner419");
		brn.getFriendList(queue = new LinkedList<String>());
		
	}
	
	
	/**
	 * Method for login sesion
	 * 
	 * @return True if login was success, else false.
	 * @throws Exception
	 */
	public int logIn(String user, String pass) throws Exception {
		
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
		
		return 0;
	}
	
	/**
	 * Method to send messages
	 * 
	 * @param Message to send
	 * @throws Exception
	 */
	public void sendMessage(String message) throws Exception{
		HtmlPage messenger = webClient.getPage("https://m.facebook.com/messages/compose/");
		
		HtmlForm form = (HtmlForm) messenger.getElementById("composer_form");
		Iterator<HtmlElement> list = form.getElementsByTagName("a").iterator();
		HtmlElement dest = null;
		
		while(list.hasNext()){
			dest = list.next();
			if(dest.asText().contains("A�adir destinatarios")){
				messenger = setDest(dest.click());
				break;
			}
		}
		
		// Just in case...
		form = (HtmlForm) messenger.getElementById("composer_form");
		
		// Sets the message in the textarea
		HtmlTextArea textarea = form.getTextAreaByName("body");
		textarea.setText("Hola");

		// Sends the message
		HtmlSubmitInput send = form.getInputByName("Send");
		messenger = send.click();
		
		System.out.println(messenger.asText());
		
		
	}
	
	
	
	
	/**
	 * Return a Htmlpage with the destinataries setted
	 * 
	 * @param destinataries
	 * @return
	 */
	private HtmlPage setDest(HtmlPage destinataries) throws Exception{
		
		// I GOT AN IDEA!!!
		// Use a queue to obtain the destinataries. Load the friends list and get the friend's names queue
		
		// Sets destinatary, using friend's name
		HtmlTextInput dest = (HtmlTextInput) destinataries.getElementByName("query");
		dest.setValueAttribute("Ariel Bravo");
		
		HtmlSubmitInput search = (HtmlSubmitInput) destinataries.getElementByName("search");
		destinataries = search.click();
		
		// Search destinatary, in case of exception, ignore?
		try{
			HtmlCheckBoxInput chbx = (HtmlCheckBoxInput) destinataries.getElementByName("friend_ids[]");
			chbx.setChecked(true);
			HtmlSubmitInput done = (HtmlSubmitInput) destinataries.getElementByName("done");
	
			return done.click();
		 	
		}
		catch(ElementNotFoundException e){
			return null;
		}
	}
	
	public void getFriendList(Queue<String> queue) throws Exception {
		HtmlPage friends = webClient.getPage("https://m.facebook.com/profile.php?v=friends");
		
		Iterator<DomElement> ite = friends.getElementsByTagName("a").iterator();
		
		while(ite.hasNext()){
			DomElement temp = ite.next();
			if(temp.asXml().contains("class=\"cd\""))
				queue.add(temp.asText());
		}
		
		if(friends.getElementById("m_more_friends") != null) {}
			
		// I decided... Use Recursion
		
	}
	
}













