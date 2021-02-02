package dk.doggycraft.antitrapdoor;

import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import net.milkbowl.vault.permission.Permission;

public class PermissionsManager
{
	private Trapdoors 		plugin;
	private Permission 		vaultPermission;
	
	public PermissionsManager(Trapdoors p)
	{
		this.plugin = p;
			
		RegisteredServiceProvider<Permission> permissionProvider = plugin.getServer().getServicesManager().getRegistration(Permission.class);
		assert permissionProvider != null;
		vaultPermission = permissionProvider.getProvider();
	}

	public boolean hasPermission(Player player, String node)
	{
		return vaultPermission.has(player, node);
	}

	public boolean inGroups(Player player, String[] groups) {
		for (String group : groups)
		{
			if (vaultPermission.playerInGroup(player, group))
			{
				return true;
			}
		}
		return false;
	}
}