package paid.weather.call56weathermoney;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class Call56WeatherMoney extends JavaPlugin implements Listener {
    public static Economy economy = null;
    public static Call56WeatherMoney instance;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new Call56WeatherMoney(), this);
        saveDefaultConfig();
        instance = this;
        setupEconomy();
        getLogger().info("Enabled");
    }

    @Override
    public void onDisable() {
        getLogger().warning("Disabled");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("wea")) {
            Player player = (Player) sender;
            FileConfiguration config = getConfig();
            double price = config.getDouble("price");
            if(getEconomy().getBalance(player) < price) {
                player.sendMessage("cpが足りません");
                return true;
            }
            if(args[0].equals("clear")) {
                getEconomy().withdrawPlayer(player, price);
                player.sendMessage("§a天気を晴れに変更しました");
                return true;
            } else if(args[0].equals("rain")) {
                getEconomy().withdrawPlayer(player, price);
                player.sendMessage("§a天気を雨に変更しました");
                return true;
            } else if(args[0].equals("reload")) {
                if(player.isOp()) {
                    reloadConfig();
                    player.sendMessage("config.ymlをリロードしました");
                } else {
                    player.sendMessage("reloadは権限者のみができます");
                }
            } else {
                player.sendMessage("§c引数が無効です");
                return true;
            }
        }
        return false;
    }

    private void setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = this.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if(economyProvider != null) {
            economy = economyProvider.getProvider();
        }
    }

    public static Economy getEconomy() {
        return economy;
    }
}
