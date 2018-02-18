package me.MinecraftDoodler.GitWeb;

import com.mrcrayfish.device.api.ApplicationManager;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MC_VERSION, acceptedMinecraftVersions = Reference.MC_VERSION, dependencies = Reference.DEPENDS)
public class GitWeb {

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		ApplicationManager.registerApplication(new ResourceLocation("gitwebapp:gitweb"), GitWebApplication.class);
	}
}
