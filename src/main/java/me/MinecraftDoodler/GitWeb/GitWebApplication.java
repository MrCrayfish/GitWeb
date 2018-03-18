package me.MinecraftDoodler.GitWeb;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import com.mrcrayfish.device.api.app.Application;
import com.mrcrayfish.device.api.app.Dialog;
import com.mrcrayfish.device.api.app.Dialog.Confirmation;
import com.mrcrayfish.device.api.app.Icons;
import com.mrcrayfish.device.api.app.Layout;
import com.mrcrayfish.device.api.app.component.Button;
import com.mrcrayfish.device.api.app.component.Spinner;
import com.mrcrayfish.device.api.app.component.Text;
import com.mrcrayfish.device.api.app.component.TextArea;
import com.mrcrayfish.device.api.app.component.TextField;
import com.mrcrayfish.device.api.app.interfaces.IHighlight;
import com.mrcrayfish.device.api.utils.OnlineRequest;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.network.ServerToClientConnectionEstablishedHandler;

public class GitWebApplication extends Application {


	
	public Layout Browser;
	public Layout Pref;
	
	public Button searchBtn;
	public Button homeBtn;
	public Button settingsBtn;
	
	public TextField bar;
	public TextArea siteView;
	

	@Override
	public void init() {
	    
		Browser = new Layout(400, 240);
		Pref = new Layout(200, 120);
				
	    bar = new TextField(3, 5, 304);
	    searchBtn = new Button(306, 5, 16, 16, Icons.RELOAD);
	    homeBtn = new Button(324, 5, 16, 16, Icons.HOME);
	    settingsBtn = new Button(342, 5, 16, 16, Icons.WRENCH);
	    siteView = new TextArea(3, 25, 355, 135); //100

	    
	    Browser.addComponent(bar);
			bar.setPlaceholder("Enter Address");
		Browser.addComponent(searchBtn);
	    		searchBtn.setToolTip("Refresh", "Loads the entered address.");
	    	Browser.addComponent(homeBtn);
	    		homeBtn.setToolTip("Home", "Loads page set in settings.");
	    	Browser.addComponent(settingsBtn);
	    		settingsBtn.setToolTip("Settings", "Change your preferences.");
	    	Browser.addComponent(siteView);
	    		siteView.setEditable(false);
	    		siteView.setWrapText(true);
	    		siteView.setPlaceholder("  If you can see this page it either means you entered an address with no text or your not connected to the internet.\n\n  Gitweb can be accessed via an address like pickaxes.info, the word after the dot denotes the folder within the root directory and the first word identifies the filename of the site within said folder. \n \n  You can also access pastebin files by entering pastebin:PASTE_ID, this feature was added just to add an option for users to experiement with ideas and test the markup. \n \n  Remember in order to function correctly GitWeb and Minecraft itself need access to the internet.");
	    
	    		GitWebLink("welcome.official", false);
	    		this.setCurrentLayout(Browser);
	    		
	    			
	    		searchBtn.setClickListener((mouseX, mouseY, mouseButton) -> {
	    		
	    			String address;
	    			address = bar.getText();
	    			address.replace(" ", "");
	    		
	    					PasteBinLink(address, false);
	    		
	    		
	    		});
	    		
	    		homeBtn.setClickListener((mouseX, mouseY, mouseButton) -> {
	    			
	    					GitWebLink("welcome.official", false);
	    		});
	    		
	    		settingsBtn.setClickListener((mouseX, mouseY, mouseButton) -> {
	    			this.setCurrentLayout(Pref);
    				
    		});
	}
	
	
	//Paste bin then moves on to GitWeb
	void PasteBinLink(String address, Boolean masked) {
		bar.setFocused(false);
		if(!masked) {
			bar.setText(address);
			}
		if(address.startsWith("pastebin:") || address.startsWith("rawpastebin:") || address.startsWith("rawpaste:")) {
			
			Confirmation pasteBinConfirm = new Confirmation("Are you sure... Pastebins are not moderated by the §aGitWeb§r team!");
            pasteBinConfirm.setTitle("Load Pastebin!");
            this.openDialog(pasteBinConfirm);
        		pasteBinConfirm.setPositiveListener((mouseX1, mouseY1, mouseButton1) -> {
        			OnlineRe("https://pastebin.com/raw/" + address.replace("paste", "").replace("raw", "").replace("bin", "").replace(":", "") + "/");
            });
        		pasteBinConfirm.setNegativeListener((mouseX1, mouseY1, mouseButton1) -> {
        			siteView.setText("This file did not get permission to load!");
            });

				}else
			GitWebLink(address, false);
	
	}
	
	//Attempt to load 'GitWeb' link.
	void GitWebLink(String address, Boolean masked) {
		if(address.contains(".")) {
			if(!masked) {
			bar.setText(address);
			}
			bar.setFocused(false);
			String[] urlA = address.split("\\.", -1);	
				if(!address.contains("/")) {
					OnlineRe("https://raw.githubusercontent.com/MrCrayfish/GitWeb-Sites/master/" + urlA[1] + "/" + urlA[0]);
				}else if(address.contains("/")) {
					String[] urlB = urlA[1].split("/", -1);
					OnlineRe("https://raw.githubusercontent.com/MrCrayfish/GitWeb-Sites/master/" + urlB[0] + "/" + urlA[0] + "-sub/" + urlB[1]);
				}
				}else {
				siteView.setText("That address doesn't look right");
		}
	}
	//Makes online Request with redirects and other stuff!
	void OnlineRe(String URL) {
		bar.setFocused(false);
		OnlineRequest.getInstance().make(URL,
        		(success, response)-> {
        			//Redirects to another GitWeb site!
        			  //pastebin's can not be redirected to.
        			if(response.startsWith("redirect>")) {
        				String reD = response;
        				reD = reD.substring(reD.indexOf(">") + 1);
        				reD = reD.substring(0, reD.indexOf("<"));
        				GitWebLink(reD, false);
        				return;
        			}
        			//Masked redirect to another site! (Keeps redirecting sites address in bar)
        			if(response.startsWith("masked_redirect>")) {
        				String reD = response;
        				reD = reD.substring(reD.indexOf(">") + 1);
        				reD = reD.substring(0, reD.indexOf("<"));
        				GitWebLink(reD, true);
        				return;
        			}
        			siteView.setText("");
        			
        			siteView.setText(response);
        			bar.setFocused(false);
        		});
		
		
	}
	
	//check for enter key
	@Override
    public void handleKeyTyped(char character, int code) {
        super.handleKeyTyped(character, code);
		String address;
		address = bar.getText();
		address.replace(" ", "");
        if (code == Keyboard.KEY_RETURN) {
        		PasteBinLink(address, false);
        }
    }
	
	@Override
	public void load(NBTTagCompound arg0) {
	
		
	}

	@Override
	public void save(NBTTagCompound arg0) {
	
		
	}

}
