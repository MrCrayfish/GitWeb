package me.MinecraftDoodler.GitWeb;

import java.awt.Color;

import com.mrcrayfish.device.api.app.Application;
import com.mrcrayfish.device.api.app.Icons;
import com.mrcrayfish.device.api.app.component.Button;
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
	public Button helpBtn;
	public TextField bar;
	public TextArea siteView;
	

	@Override
	public void init() {
	    this.setDefaultHeight(240);
	    this.setDefaultWidth(400);
	    
	    bar = new TextField(3, 5, 318);
	    		bar.setPlaceholder("Enter Address");
	    searchBtn = new Button(342, 5, 16, 16, Icons.RELOAD);
	    helpBtn = new Button(324, 5, 16, 16, Icons.INFO);
	    siteView = new TextArea(3, 25, 355, 135); //100
	    addComponent(bar);
	    addComponent(searchBtn);
	    		searchBtn.setToolTip("Request Site", "Attempts to get the GitWeb site.");
	    	addComponent(helpBtn);
	    		searchBtn.setToolTip("Useful Sites", "Loads \'usefulsites.info\'.");
	    addComponent(siteView);
	    		siteView.setEditable(false);
	    		siteView.setWrapText(true);
	    
	    		searchBtn.setClickListener((mouseX, mouseY, mouseButton) -> {
	    		
	    		String address;
	    		address = bar.getText();
	    		address.replace(" ", "");
	    		
	    		if(address.startsWith("pastebin:")) {
	    			
	    			OnlineRequest.getInstance().make("https://pastebin.com/raw/" + address.replace("pastebin:", "") + "/",
	    	        		(success, response)-> {
	    	        			siteView.setText(response);
	    	        			bar.setText("§aRaw Pastebin Loaded!");
	    	        		});
	    			logI("pastebin.com/raw/" + address.replace("pastebin:", ""));
	    		}
	    		else if(address.contains(".")) {
	    		String[] urlA = address.split("\\.", -1);	 
	    			OnlineRequest.getInstance().make("https://raw.githubusercontent.com/MinecraftDoodler/GitWeb-Sites/master/" + urlA[1] + "/" + urlA[0],
	    	        		(success, response)-> {
	    	        			siteView.setText(response);
	    	        			bar.setText(urlA[0] + "." + urlA[1]);
	    	        		});
		    		}else {
		    			siteView.setText("000: Invalid Suffix");
		    		}
	    		bar.setFocused(false);
	    		});
	    		
	    		helpBtn.setClickListener((mouseX, mouseY, mouseButton) -> {
	    			OnlineRequest.getInstance().make("https://raw.githubusercontent.com/MinecraftDoodler/GitWeb-Sites/master/info/usefulsites",
	    	        		(success, response)-> {
	    	        			siteView.setText(response);
	    	        			bar.setText("usefulsites.info");
	    	        			bar.setFocused(false);
	    	        		});
	    		});
	}
	

	
	void logI(String URL) {
		System.out.println(URL);
	}
	
	@Override
	public void load(NBTTagCompound arg0) {
	
		
	}

	@Override
	public void save(NBTTagCompound arg0) {
	
		
	}

}
