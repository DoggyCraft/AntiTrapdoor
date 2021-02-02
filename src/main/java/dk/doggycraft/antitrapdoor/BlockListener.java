package dk.doggycraft.antitrapdoor;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.domains.GroupDomain;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import scala.Array;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

public class BlockListener implements Listener
{
	private Trapdoors plugin;
	private RegionContainer container;

	BlockListener(Trapdoors p)
	{
		this.plugin = p;
	}

	public void load()
	{
		this.container = WorldGuard.getInstance().getPlatform().getRegionContainer();
	}

	private boolean isTrapdoor(Block clickedBlock)
	{
		switch (clickedBlock.getType()) {
			case ACACIA_TRAPDOOR:
			case BIRCH_TRAPDOOR:
			case CRIMSON_TRAPDOOR:
			case DARK_OAK_TRAPDOOR:
			case IRON_TRAPDOOR:
			case JUNGLE_TRAPDOOR:
			case OAK_TRAPDOOR:
			case SPRUCE_TRAPDOOR:
			case WARPED_TRAPDOOR:
				return true;
			default:
				return false;
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();

		if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
		{
			return;
		}
		
		if (!Objects.equals(event.getHand(), EquipmentSlot.HAND))
		{
			return;
		}
		
		Block clickedBlock = event.getClickedBlock();
		if (clickedBlock == null)
		{
			return;
		}

		if (this.isTrapdoor(clickedBlock))
		{
			Location blockLocation = clickedBlock.getLocation();
			RegionQuery query = this.container.createQuery();
			com.sk89q.worldedit.util.Location loc = BukkitAdapter.adapt(blockLocation);
			ApplicableRegionSet set = query.getApplicableRegions(loc);

			for (ProtectedRegion region : set) {
				if (region.getId().equals("spawnregion"))
				{
					DefaultDomain members = region.getMembers();
					DefaultDomain owners = region.getOwners();
					Set<String> ownerGroups = owners.getGroups();
					Set<String> memberGroups = members.getGroups();
					String[] groups = new String[ownerGroups.size()+memberGroups.size()];
					ownerGroups.toArray(groups);
					memberGroups.toArray(groups);
					if (!((player.isOp() || plugin.getPermissionsManager().hasPermission(player, "trapdoors.use")) && (members.contains(player.getUniqueId()) || owners.contains(player.getUniqueId()) || plugin.getPermissionsManager().inGroups(player, groups))))
					{
						event.setCancelled(true);
					}
				}
			}
		}
	}
}