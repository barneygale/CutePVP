package com.c45y.CutePVP;

import java.util.Dictionary;
import java.util.HashMap;
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
        HashMap<String, String> fposSet = new HashMap<String, String>();
        
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

                this.getConfig().addDefault("ctf.red.x", null);
                this.getConfig().addDefault("ctf.red.y", null);
                this.getConfig().addDefault("ctf.red.z", null);
                
                this.getConfig().addDefault("ctf.blue.x", null);
                this.getConfig().addDefault("ctf.blue.y", null);
                this.getConfig().addDefault("ctf.blue.z", null);
                
                this.getConfig().addDefault("ctf.yellow.x", null);
                this.getConfig().addDefault("ctf.yellow.y", null);
                this.getConfig().addDefault("ctf.yellow.z", null);
                
                this.getConfig().addDefault("ctf.green.x", null);
                this.getConfig().addDefault("ctf.green.y", null);
                this.getConfig().addDefault("ctf.green.z", null);
                
                this.getConfig().addDefault("ctf.red.curr.x", null);
                this.getConfig().addDefault("ctf.red.curr.y", null);
                this.getConfig().addDefault("ctf.red.curr.z", null);
                
                this.getConfig().addDefault("ctf.blue.curr.x", null);
                this.getConfig().addDefault("ctf.blue.curr.y", null);
                this.getConfig().addDefault("ctf.blue.curr.z", null);
                
                this.getConfig().addDefault("ctf.yellow.curr.x", null);
                this.getConfig().addDefault("ctf.yellow.curr.y", null);
                this.getConfig().addDefault("ctf.yellow.curr.z", null);
                
                this.getConfig().addDefault("ctf.green.curr.x", null);
                this.getConfig().addDefault("ctf.green.curr.y", null);
                this.getConfig().addDefault("ctf.green.curr.z", null);
                
                this.getConfig().addDefault("ctf.red.carier", null);
                this.getConfig().addDefault("ctf.blue.carier", null);
                this.getConfig().addDefault("ctf.yellow.carier", null);
                this.getConfig().addDefault("ctf.green.carier", null);
                
		this.getConfig().addDefault("base.protection.radius", 50);
		this.getConfig().addDefault("count.red", 0);
		this.getConfig().addDefault("count.blue", 0);
		this.getConfig().addDefault("count.yellow", 0);
		this.getConfig().addDefault("count.green", 0);
		saveConfig();
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(loglistener, this);
		System.out.println(this.toString() + " enabled");

		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				getServer().broadcastMessage(ChatColor.DARK_PURPLE + "[NOTICE] Flags are respawning!");
				respawnFlags();
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
							getServer().broadcastMessage(ChatColor.DARK_PURPLE + "[NOTICE] " + winTeam + " gets buff!");
							for (Player playeri : getServer().getOnlinePlayers()) {
								if (teamName(playeri.getName()) == winTeam) {
									playeri.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 12000, 0));
									playeri.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 1200, 1));
								}
							}
						}
					}
				}
				getLogger().info("End running buff");
			}
		}, 1200, 12000);
		
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				//Need to loop over all teams, check if their carrier is offline
				//If so we increment a config value.
				//If it gets above 5, return
				for(int i = 0; i<4; i++) {
					String team = teamNameFromInt(i);
					String carrier = getFlagCarrier(team);
					if(carrier != null) {
						int old = getConfig().getInt("flag."+team+".expiry");
						getConfig().set("flag."+team+".expiry", old+1);
						if(old+1 > 5) {
							getServer().broadcastMessage("Flag carrier gone for 5 mins, returning...");
						}
					}
				}
			}
		}, 1200, 1200);
	}

	public void onDisable() {
		System.out.println(this.toString() + " disabled");
	}
        
        public Location getTeamFlagLoc(String team) {
            return new Location(getServer().getWorlds().get(0),
                    getConfig().getInt("ctf. " + team + " .curr.x"),
                    getConfig().getInt("ctf. " + team + " .curr.y"),
                    getConfig().getInt("ctf. " + team + " .curr.z")
            );
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
                else if (command.getName().equalsIgnoreCase("g")) {
			String str = StringUtils.join(args, " ");
			for (Player playeri : getServer().getOnlinePlayers()) {
				playeri.sendMessage(ChatColor.RED + ">" + ChatColor.BLUE + ">" + ChatColor.GREEN + ">" + ChatColor.YELLOW + ">" + ChatColor.WHITE + " <" + colorName(sender.getName()) + "> " + str);
			}
			return true;
		}
                else if (command.getName().equalsIgnoreCase("fpos")) {
                    if (args.length == 0) {
                        if (fposSet.containsKey(sender.getName())) {
                            fposSet.remove(sender.getName());
                            return true;
                        }
                        return false;
                    }
                    if ( woolColorByName(args[0]) == 0) {
                        return false;
                    }
                    fposSet.put(sender.getName(), args[0]);
                    sender.sendMessage("You are now setting the " + args[0] + " flag.");
                }
		return false;
	}

	public int getTeam(String inpt) {
		int value = Character.getNumericValue(ChatColor.stripColor(inpt).charAt(-1));
		for(int i: new int[] { 10, 11, 23, 30, 33 } ) {
			if(value == i) return 0;
		}
		for(int i: new int[] { 14, 17, 28, 21, 29 } ) {
			if(value == i) return 1;
		}
		for(int i: new int[] { 12, 13, 15, 18, 25, 31, 32 } ) {
			if(value == i) return 2;
		}
		for(int i: new int[] { 0, 16, 19, 20, 22, 24, 27, 34, 35 } ) {
			if(value == i) return 3;
		}
		return 0;
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
	public int teamNameToWoolColor(String team) {
		if (team == "red")    return 14;
		if (team == "blue")   return 3;
		if (team == "yellow") return 4;
		if (team == "green")  return 5;
		return 0;
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
		return (short)teamNameToWoolColor(teamName(inpt));
	}
	
	public byte woolColorByName(String inpt) {
		if (inpt == "red") {
			return 14;
		} else if (inpt == "blue") {
			return 3;
		} else if (inpt == "yellow") {
			return 4;
		} else if (inpt == "green") {
			return 5;
		}
		return 0;
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
	
	public void respawnFlags() {
		for(int i=0; i<4; i++) {
			String teamName = teamNameFromInt(i);
			Block flag_block = getServer().getWorlds().get(0).getBlockAt(
					getConfig().getInt("flag." + teamName + ".x"),
					getConfig().getInt("flag." + teamName + ".y"),
					getConfig().getInt("flag." + teamName + ".z"));
			flag_block.setTypeIdAndData(35, woolColorByName(teamName), false);
		}
	}

    String carrierFor(Player player) {
        if (player.getName().equalsIgnoreCase(getConfig().getString("ctf.red.carier"))) {
            return "red";
        }
        if (player.getName().equalsIgnoreCase(getConfig().getString("ctf.blue.carier"))) {
            return "blue";
        }
        if (player.getName().equalsIgnoreCase(getConfig().getString("ctf.yellow.carier"))) {
            return "yellow";
        }
        if (player.getName().equalsIgnoreCase(getConfig().getString("ctf.green.carier"))) {
            return "green";
        }
        return null;
    }
    
    void capForTeam(String team) {
        getConfig().set("count." + team, getConfig().getInt("count." + team) + 1);
    }
    
    void messageCap(String capTeam, String cappedTeam) {
        getServer().broadcastMessage(String.format("%sTeam %s just capped the %s flag", ChatColor.GREEN, capTeam, cappedTeam));
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