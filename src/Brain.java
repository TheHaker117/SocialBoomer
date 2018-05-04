import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;

public class Brain{
	
	private String user = "eunice13021985@gmail.com";
	private String pass = "osmosys2018";
	
	private HtmlPage mainpage;
	
	public Brain() throws Exception {
		logIn();
		//sendMessage("");
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
	
	public boolean logIn() throws Exception {
		
		// Only works with Firefox
		WebClient webClient = new WebClient(BrowserVersion.FIREFOX_52);
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
		
		if(text.contains("Iniciar sesión con un toque")){
			HtmlSubmitInput acpt = (HtmlSubmitInput) mainpage.getForms().get(0).getInputByValue("Aceptar");
			mainpage = acpt.click();
		}
		
		else if(text.contains("Queremos asegurarnos de que tu cuenta está protegida"))
			
		
		System.out.println(mainpage.asText());
		
		return mainpage.getTitleText().equals("Facebook");
	
	}
	
	public void sendMessage(String message) throws Exception{
		
		HtmlSubmitInput bmess = (HtmlSubmitInput) mainpage.getElementById("messages_jewel");
		
		HtmlPage page = bmess.click();
		
		System.out.println(page.asText());
		
		
	}
	
	
	
	
	
}













