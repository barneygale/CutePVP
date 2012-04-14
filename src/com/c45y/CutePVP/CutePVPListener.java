package com.c45y.CutePVP;


import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CutePVPListener implements Listener{
	public final CutePVP p;

	public CutePVPListener(CutePVP instance) {
		p = instance;
	}

	/* Inventory crap, locking the wool in place */

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if ( player.getGameMode() == GameMode.CREATIVE ){
			return;
		}
		if (player.getInventory().getHelmet() == null) {
			event.setCancelled(true);
			return;
		}
		if (player.getInventory().getHelmet().getType() != Material.WOOL) {
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onInventoryClose(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		if ( player.getGameMode() == GameMode.CREATIVE ){
			return;
		}
		if (player.getInventory().getHelmet() == null) {
			player.getInventory().setHelmet(p.returnWool(player.getName()));
			return;
		}
		if (player.getInventory().getHelmet().getType() != Material.WOOL) {
			player.getInventory().setHelmet(p.returnWool(player.getName()));
			return;
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		Player player = (Player) event.getPlayer();
		if(player.isDead()) {
			return;
		}
		if ( player.getGameMode() == GameMode.CREATIVE ){
			return;
		}
		if (event.getItemDrop().getItemStack().getType() == Material.WOOL) {
			event.getItemDrop().remove();
			return;
		}
	}

	/* END - Inventory crap, locking the wool in place */

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		event.getPlayer().getInventory().setHelmet(p.returnWool(event.getPlayer().getName()));
		event.setRespawnLocation(p.getRespawnTeamLocation(event.getPlayer().getName()));
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		player.setDisplayName(p.colorName(event.getPlayer().getName()));
		player.getInventory().setHelmet(p.returnWool(player.getName()));
		event.setJoinMessage(player.getDisplayName() + " joined the game.");
        if (!event.getPlayer().hasPlayedBefore()) {
            event.getPlayer().teleport(p.getRespawnTeamLocation(event.getPlayer().getName()));
        }
		/* 
		 * Let us forget this ever happened...
		 * Commands/permissions are a pain to implement and maintain.
		 * Breaks a lot more things than it adds.
		 * 
		 * Simply looking to 'lock' the wool block now.
		 *
		 * // Make a new entity to eventually replace our player with
		 * EntityPlayer playerz = (EntityPlayer) ((CraftPlayer) player).getHandle();
		 * WorldServer world = (WorldServer) playerz.world;
		 * EntityTracker tracker = world.tracker;
		 * 
		 * // Mess with entity tracking, this makes me nervous
		 * tracker.untrackEntity( playerz);
		 * playerz.name = colorName(event.getPlayer().getName());
		 * playerz.displayName = colorName(event.getPlayer().getName());
		 * tracker.track(playerz);
		 */
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
		if (block.getType() == Material.WOOL) {
			if (block.getData() == 14 || block.getData() == 3 || block.getData() == 4 || block.getData() == 5) {
				if (block.getData() != p.woolColor(player.getName())) {
					player.damage(1);
				}
			}	
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event){
		if (!(event.getEntity() instanceof Player)) {
			return;
		}
		if ((event.getDamager() instanceof Player)) {
			Player attacker = (Player) event.getDamager();
			Player player = (Player) event.getEntity();
            if (p.getTeam(attacker.getName()) == p.getTeam(player.getName())) {
                    event.setCancelled(true);
            } else if(p.getInRangeOfEnemyTeamSpawn(attacker)){
            		attacker.sendMessage(ChatColor.DARK_RED + "You cannot attack within another teams base");
                    event.setCancelled(true);
            }
		}
		else if (event.getDamager() instanceof Arrow) {
			Arrow arrow = (Arrow) event.getDamager();
			if (arrow.getShooter() instanceof Player) {
				Player player = (Player) event.getEntity();
				Player shooter = (Player) arrow.getShooter();
	            if (p.getTeam(shooter.getName()) == p.getTeam(player.getName())) {
	                    event.setCancelled(true);
	            } else if(p.getInRangeOfEnemyTeamSpawn(shooter)){
	            		shooter.sendMessage(ChatColor.DARK_RED + "You cannot attack within another teams base");
	                    event.setCancelled(true);
	            }
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerChat(PlayerChatEvent event) {
		if ( event.getPlayer().getGameMode() == GameMode.CREATIVE ){
			return;
		}
		Player player = event.getPlayer();
		event.setCancelled(true);
		p.teamChat(p.teamNameFromInt(p.getTeam(player.getName())), "<" + player.getDisplayName() + "> " + event.getMessage());
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event) {
		if ( event.getPlayer().getGameMode() == GameMode.CREATIVE ){
			return;
		}
		int team = p.isFlagBlock(event.getBlock().getX(), event.getBlock().getY(), event.getBlock().getZ());
		if (team != -1) {
			event.setCancelled(true);
		}
		if (p.getInRangeOfEnemyTeamSpawn(event.getPlayer())) {
			event.getPlayer().sendMessage(ChatColor.DARK_RED + "You cannot build in an enemy base");
			event.setCancelled(true);
		}
	}
	
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if ( event.getPlayer().getGameMode() == GameMode.CREATIVE ){
			return;
		}

		
		//Check if it's a flag
		int team = p.isFlagBlock(event.getBlock().getX(), event.getBlock().getY(), event.getBlock().getZ());
		if (team != -1) {
			System.out.println("Destroyed a flag block!");
			
			//Enemy player...
			if (p.getInRangeOfEnemyTeamSpawn(event.getPlayer())) {
				String teamName = p.woolColorToTeamName((short)event.getBlock().getData());
				p.chat(player.getDisplayName() + " has the " + teamName + " team flag!");
				p.setFlagCarrier(teamName, player.getName());
				//event.getBlock().setTypeIdAndData(35, event.getBlock().getData(), true);
				ItemStack stack = new ItemStack(35, 1, (short)event.getBlock().getData());
				Inventory inv = event.getPlayer().getInventory();
				inv.addItem(stack);
				event.getBlock().setTypeId(0);
				event.setCancelled(true);
			} else {
				event.setCancelled(true);
			}
		} else {
			//If they're in an enemy base...
			if (p.getInRangeOfEnemyTeamSpawn(event.getPlayer())) {
				event.getPlayer().sendMessage(ChatColor.DARK_RED + "You cannot build in an enemy base");
				event.setCancelled(true);
			}
		}
	}
}