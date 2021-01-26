package dk.doggycraft.antitrapdoor;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Trapdoors extends JavaPlugin
{
	private PermissionsManager		permissionManager	= null;
	private BlockListener			blockListener		= null;

	private static Trapdoors 	plugin;
	
	public PermissionsManager getPermissionsManager()
	{
		return this.permissionManager;
	}

	public void onEnable()
	{
		this.permissionManager = new PermissionsManager(this);
		this.blockListener = new BlockListener(this);

		PluginManager pm = getServer().getPluginManager();

		this.blockListener.load();

		pm.registerEvents(blockListener, this);
	}
}