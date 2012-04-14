package com.c45y.CutePVP;

import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CutePVP extends JavaPlugin {
	private final CutePVPListener loglistener = new CutePVPListener(this);
	Logger log = Logger.getLogger("Minecraft");

	@Override
	public void onEnable() {
		this.getConfig().options().copyDefaults(true);
		// ALL spawn
		this.getConfig().addDefault("spawn.all.x", getServer().getWorlds().get(0).getSpawnLocation().getX());
		this.getConfig().addDefault("spawn.all.y", getServer().getWorlds().get(0).getSpawnLocation().getY());
		this.getConfig().addDefault("spawn.all.z", getServer().getWorlds().get(0).getSpawnLocation().getZ());
		// Red spawn
		this.getConfig().addDefault("spawn.red.x", getServer().getWorlds().get(0).getSpawnLocation().getX());
		this.getConfig().addDefault("spawn.red.y", getServer().getWorlds().get(0).getSpawnLocation().getY());
		this.getConfig().addDefault("spawn.red.z", getServer().getWorlds().get(0).getSpawnLocation().getZ());
		// Blue spawn
		this.getConfig().addDefault("spawn.blue.x", getServer().getWorlds().get(0).getSpawnLocation().getX());
		this.getConfig().addDefault("spawn.blue.y", getServer().getWorlds().get(0).getSpawnLocation().getY());
		this.getConfig().addDefault("spawn.blue.z", getServer().getWorlds().get(0).getSpawnLocation().getZ());
		// Yellow spawn
		this.getConfig().addDefault("spawn.yellow.x", getServer().getWorlds().get(0).getSpawnLocation().getX());
		this.getConfig().addDefault("spawn.yellow.y", getServer().getWorlds().get(0).getSpawnLocation().getY());
		this.getConfig().addDefault("spawn.yellow.z", getServer().getWorlds().get(0).getSpawnLocation().getZ());
		// Green spawn
		this.getConfig().addDefault("spawn.green.x", getServer().getWorlds().get(0).getSpawnLocation().getX());
		this.getConfig().addDefault("spawn.green.y", getServer().getWorlds().get(0).getSpawnLocation().getY());
		this.getConfig().addDefault("spawn.green.z", getServer().getWorlds().get(0).getSpawnLocation().getZ());
		// Block of power
		this.getConfig().addDefault("block.buff.x", getServer().getWorlds().get(0).getSpawnLocation().getX());
		this.getConfig().addDefault("block.buff.y", getServer().getWorlds().get(0).getSpawnLocation().getY());
		this.getConfig().addDefault("block.buff.z", getServer().getWorlds().get(0).getSpawnLocation().getZ());

		this.getConfig().addDefault("base.protection.radius", 50);

		saveConfig();
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(loglistener, this);
		System.out.println(this.toString() + " enabled");

		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				getLogger().info("Running buff");
				Location powerblock = new Location(
						getServer().getWorlds().get(0),
						getConfig().getDouble("block.buff.x"),
						getConfig().getDouble("block.buff.y"),
						getConfig().getDouble("block.buff.z"));
				if (getServer().getWorlds().get(0).getBlockAt(powerblock) != null) {
					Block gPowerBlock = getServer().getWorlds().get(0).getBlockAt(powerblock);
					if (gPowerBlock.getType() == Material.WOOL) {
						String winTeam = woolColorToTeamName(gPowerBlock.getData());
						if (winTeam != null) {
							getServer().broadcastMessage("[NOTICE] " + winTeam + " gets buff!");
							for (Player playeri : getServer().getOnlinePlayers()) {
								if (teamName(playeri.getName()) == winTeam) {
									playeri.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 12000, 0));
									playeri.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 1200, 1));
									playeri.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 12000, 0));
								}
							}
						}
					}
				}
				getLogger().info("End running buff");
			}
		}, 1200, 12000);
	}

	public void onDisable() {
		log.info("playzorz disabled.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Sorry, this plugin cannot be used from console");
			return true;
		}
		Player player = (Player) sender;
		Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
		if (command.getName().equalsIgnoreCase("asdf")) {
			if (args[0] != null) {
				getConfig().set(args[0] + ".x", block.getLocation().getX());
				getConfig().set(args[0] + ".y", block.getLocation().getY());
				getConfig().set(args[0] + ".z", block.getLocation().getZ());
			} else {
				getConfig().set("block.buff.x", block.getLocation().getX());
				getConfig().set("block.buff.y", block.getLocation().getY());
				getConfig().set("block.buff.z", block.getLocation().getZ());
			}
			saveConfig();
			player.sendMessage("Set to: " + block.getType().toString());
			return true;
		}
		if (command.getName().equalsIgnoreCase("g")) {
			String str = StringUtils.join(args, " ");
			for (Player playeri : getServer().getOnlinePlayers()) {
				playeri.sendMessage(ChatColor.RED + ">" + ChatColor.BLUE + ">" + ChatColor.GREEN + ">" + ChatColor.YELLOW + ">" + ChatColor.WHITE + " <" + colorName(sender.getName()) + "> " + str);
			}
			return true;
		}
		return false;
	}

	public int getTeam(String inpt) {
		return (Character.getNumericValue(ChatColor.stripColor(inpt).charAt(0)) % 4);
	}

	public Location getRespawnTeamLocation(String playerName) {
		String team = teamName(playerName);
		return getRespawnTeamLocationByTeam(team);
	}
	
	public String woolColorToTeamName(short metadata) {
		switch(metadata) {
			case 14:
				return "red";
			case 3:
				return "blue";
			case 4:
				return "yellow";
			case 5:
				return "green";
		}
		return null;
	}

	public Location getRespawnTeamLocationByTeam(String teamName) {
		return new Location(
				getServer().getWorlds().get(0),
				getConfig().getInt("spawn." + teamName + ".x"),
				getConfig().getInt("spawn." + teamName + ".y"),
				getConfig().getInt("spawn." + teamName + ".z"));
	}

	public boolean getInRangeOfEnemyTeamSpawn(Player player) {
		int rad = getConfig().getInt("base.protection.radius");
		Location player_loc = player.getLocation();
		if (teamName(player.getName()) != "red") {
			Location _spawn = getRespawnTeamLocationByTeam("red");
			double dx = _spawn.getX() - player_loc.getX();
			double dz = _spawn.getZ() - player_loc.getZ();
			if ((dx<rad && dx>-rad) && (dz<rad && dz>-rad)) {
				return true;
			}
		} else if (teamName(player.getName()) != "blue") {
			Location _spawn = getRespawnTeamLocationByTeam("blue");
			double dx = _spawn.getX() - player_loc.getX();
			double dz = _spawn.getZ() - player_loc.getZ();
			if ((dx<rad && dx>-rad) && (dz<rad && dz>-rad)) {
				return true;
			}
		} else if (teamName(player.getName()) != "yellow") {
			Location _spawn = getRespawnTeamLocationByTeam("yellow");
			double dx = _spawn.getX() - player_loc.getX();
			double dz = _spawn.getZ() - player_loc.getZ();
			if ((dx<rad && dx>-rad) && (dz<rad && dz>-rad)) {
				return true;
			}
		} else if (teamName(player.getName()) != "green") {
			Location _spawn = getRespawnTeamLocationByTeam("green");
			double dx = _spawn.getX() - player_loc.getX();
			double dz = _spawn.getZ() - player_loc.getZ();
			if ((dx<rad && dx>-rad) && (dz<rad && dz>-rad)) {
				return true;
			}
		}
		return false;
	}

	public short woolColor(String inpt) {
		int ret = getTeam(inpt);
		switch (ret) {
		case 0: return 14; // Red
		case 1: return 3;  // Blue
		case 2: return 4;  // Yellow
		case 3: return 5;  // Green
		}
		return (short) ret;
	}

	public String teamName(String inpt) {
		int ret = getTeam(inpt);
		return teamNameFromInt(ret);
	}

	public String teamNameFromInt(int ret) {
		switch (ret) {
		case 0: return "red";
		case 1: return "blue";
		case 2: return "yellow";
		case 3: return "green";
		}
		return "";
	}

	public ItemStack returnWool(String inpt) {
		return new ItemStack(Material.WOOL.getId(), 1, woolColor(inpt));
	}

	public String colorName(String inpt) {
		int ret = getTeam(inpt);
		String retName = "";
		switch (ret) {
		case 0: retName += ChatColor.RED;
		break;
		case 1: retName += ChatColor.BLUE;
		break;
		case 2: retName += ChatColor.YELLOW;
		break;
		case 3: retName += ChatColor.GREEN;
		break;
		}
		retName += (inpt + ChatColor.WHITE);
		return retName;
	}
	public int isFlagBlock(int x, int y, int z) {
		for(int i=0; i<4; i++) {
			String teamName = teamNameFromInt(i);
			if( getConfig().getInt("flag." + teamName + ".x") == x &&
				getConfig().getInt("flag." + teamName + ".y") == y &&
				getConfig().getInt("flag." + teamName + ".z") == z) {
				
				return i;
				
			}
		}
		return -1;
	}
	public void chat(String message) {
		for (Player playeri : getServer().getOnlinePlayers()) {
			playeri.sendMessage(message);
		}
	}
	public void teamChat(String team, String message) {
		for (Player playeri : getServer().getOnlinePlayers()) {
			if (teamName(playeri.getName()) == team) {
				playeri.sendMessage(message);
			}
		}
	}
	
	//Flag carrier of <team>'s flag
	public String getFlagCarrier(String team) {
		return getConfig().getString("carrier."+team);
	}
	public void setFlagCarrier(String team, String player) {
		getConfig().set("carrier."+team, player);
	}
}