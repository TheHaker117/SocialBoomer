import java.util.Iterator;
import java.util.List;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;

public class Brain{
	
	private String user = "ale_storm@outlook.com";
	private String pass = "343Forerunner419";
	
	private WebClient webClient;
	private HtmlPage mainpage;
	
	
	public Brain() throws Exception {
		logIn();
		sendMessage("");
	}
	
	
	public static void main(String[] args) throws Exception{
		Brain brain = new Brain();
	}
	
	/**
	 * Method for login sesion
	 * 
	 * @return True if login was success, else false.
	 * @throws Exception
	 */
	public int logIn() throws Exception {
		
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
		String text = mainpage.asText();
		
		System.out.println(mainpage.asText());
		
		if(text.contains("Iniciar sesión con un toque")){
			HtmlSubmitInput acpt = (HtmlSubmitInput) mainpage.getForms().get(0).getInputByValue("Aceptar");
			mainpage = acpt.click();
			
			System.out.println(mainpage.asText());
			
			return 0;
		}
		
		else if(text.contains("Queremos asegurarnos de que tu cuenta está protegida"))
			return -1;
		
		else
			return 1;
		
	}
	
	/**
	 * Method to send messages
	 * 
	 * @param Message to send
	 * @throws Exception
	 */
	public void sendMessage(String message) throws Exception{
		HtmlPage messenger = webClient.getPage("http://m.facebook.com/messages/");
		
		List<DomElement> spans = messenger.getElementsByTagName("a");
		
		Iterator<DomElement> ite = spans.iterator();
		DomElement element;
		HtmlPage newmess = null;
		
		
		
		while(ite.hasNext()){
			element = ite.next();
			if(element.asText().contains("Nuevo mensaje")){
				newmess = element.click();
				System.out.println(newmess.asText());
				break;
			}
		}
		
		System.out.println(">>>>");
		
		if(newmess != null){
			// Trying to access to the textarea through the form...
			HtmlForm form = newmess.getFormByName("fb_dtsg");
				
				
			
			
		}
		
		
	}
	
	
	
	
	
}













