package me.MinecraftDoodler.GitWeb;

import java.awt.Color;

import com.mrcrayfish.device.api.app.Application;
import com.mrcrayfish.device.api.app.Icons;
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

	public Button searchBtn;
	public Button homeBtn;
	public Button settingsBtn;
	public TextField bar;
	public TextArea siteView;
	

	@Override
	public void init() {
	    this.setDefaultHeight(240);
	    this.setDefaultWidth(400);
	    
	    bar = new TextField(3, 5, 304);
	    searchBtn = new Button(306, 5, 16, 16, Icons.RELOAD);
	    homeBtn = new Button(324, 5, 16, 16, Icons.HOME);
	    settingsBtn = new Button(342, 5, 16, 16, Icons.WRENCH);
	    siteView = new TextArea(3, 25, 355, 135); //100

	    addComponent(bar);
			bar.setPlaceholder("Enter Address");
	    addComponent(searchBtn);
	    		searchBtn.setToolTip("Refresh", "Loads the entered address.");
	    	addComponent(homeBtn);
	    		homeBtn.setToolTip("Home", "Loads page set in settings.");
	    	 addComponent(settingsBtn);
	    		settingsBtn.setToolTip("Settings", "Change your preferences.");
	    addComponent(siteView);
	    		siteView.setEditable(false);
	    		siteView.setWrapText(true);
	    		siteView.setPlaceholder("  Gitweb can be accessed via an address like pickaxes.info, the word after the dot denotes the folder within the root directory and the first word identifies the filename of the site within said folder. \n \n  You can also access pastebin files by entering pastebin:PASTE_ID, this feature was added just to add an option for users to experiement with ideas and test the markup. \n \n  Remember in order to function correctly GitWeb and Minecraft itself need access to the internet.");
	    
	    		searchBtn.setClickListener((mouseX, mouseY, mouseButton) -> {
	    		
	    		String address;
	    		address = bar.getText();
	    		address.replace(" ", "");
	    		
	    		if(address.startsWith("pastebin:")) {
	    			OnlineRe("https://pastebin.com/raw/" + address.replace("pastebin:", "") + "/");
	    		}
	    		else if(address.contains(".")) {
	    		String[] urlA = address.split("\\.", -1);	
	    				OnlineRe("https://raw.githubusercontent.com/MinecraftDoodler/GitWeb-Sites/master/" + urlA[1] + "/" + urlA[0]);
		    		}else {
		    			siteView.setText("That address doesn't look right");
		    		}
	    		bar.setFocused(false);
	    		});
	    		
	    		homeBtn.setClickListener((mouseX, mouseY, mouseButton) -> {
	    			OnlineRe("https://raw.githubusercontent.com/MinecraftDoodler/GitWeb-Sites/master/official/welcome");
	    		});
	}
	

	
	void OnlineRe(String URL) {
		System.out.println(URL);
		OnlineRequest.getInstance().make(URL,
        		(success, response)-> {
        			siteView.setText(response);
        			bar.setFocused(false);
        		});
	}
	
	@Override
	public void load(NBTTagCompound arg0) {
	
		
	}

	@Override
	public void save(NBTTagCompound arg0) {
	
		
	}

}
