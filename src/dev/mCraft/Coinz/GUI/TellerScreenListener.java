package dev.mCraft.Coinz.GUI;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.getspout.spoutapi.event.screen.ButtonClickEvent;
import org.getspout.spoutapi.event.screen.TextFieldChangeEvent;
import org.getspout.spoutapi.gui.Button;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericTextField;
import org.getspout.spoutapi.inventory.SpoutItemStack;
import org.getspout.spoutapi.player.SpoutPlayer;

import dev.mCraft.Coinz.Coinz;
import dev.mCraft.Coinz.GUI.TellerMenu.TellerPopup;

public class TellerScreenListener implements Listener {
	private TellerPopup tellerPopup;
	private Coinz plugin = Coinz.instance;
	
	private Button button;
	private SpoutPlayer player;
	private PlayerInventory inv;
	private SpoutItemStack stack;
	private short dur;
	
	private GenericTextField enter;
	private GenericLabel balance;
	
	private double add;
	private double amount;
	private double oldAmount;
	private double coin;
	private double remove;
	
	private SpoutItemStack copp;
	private SpoutItemStack bron;
	private SpoutItemStack silv;
	private SpoutItemStack gold;
	private SpoutItemStack plat;
	
	public TellerScreenListener() {
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onButtonClick(ButtonClickEvent event) {
		tellerPopup = TellerPopup.hook;
		
		if (tellerPopup == null) {
			return;
		}
		
		button = event.getButton();
		player = event.getPlayer();
		inv = player.getInventory();
		plugin = Coinz.instance;
		
		enter = tellerPopup.enter;
		balance = tellerPopup.amount;
		
		if (button.getText() != null && button.getPlugin() == plugin) {
			
			if (button.getId() == tellerPopup.escape.getId()) {
				player.closeActiveWindow();
			}
			
			if (button.getId() == tellerPopup.deposit.getId()) {
				depositCoins();
			}
			
			if (button.getId() == tellerPopup.withdraw.getId()) {
				withdrawCoins();
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onTextFieldChange(TextFieldChangeEvent event) {
		event.getTextField().setFocus(true);
		tellerPopup = TellerPopup.hook;
		player = event.getPlayer();
		
		if (tellerPopup.containsWidget(tellerPopup.notEnoughA)) {
			tellerPopup.removeWidget(tellerPopup.notEnoughA);
		}
		
		if (tellerPopup.containsWidget(tellerPopup.notEnoughC)) {
			tellerPopup.removeWidget(tellerPopup.notEnoughC);
		}
		
		if (tellerPopup.containsWidget(tellerPopup.wrongChange)) {
			tellerPopup.removeWidget(tellerPopup.wrongChange);
		}
		
		if (tellerPopup.containsWidget(tellerPopup.invalidChar)) {
			tellerPopup.removeWidget(tellerPopup.invalidChar);
		}
		
		if (tellerPopup.containsWidget(tellerPopup.invalidAmount)) {
			tellerPopup.removeWidget(tellerPopup.invalidAmount);
		}
	}
	
	public void depositCoins() {
		
		if (enter.getPlugin() == plugin && !enter.getText().isEmpty()) {
			try {
				add = Double.parseDouble(enter.getText());
			}
			catch(Exception e) {
				tellerPopup.attachWidget(plugin, tellerPopup.invalidChar);
				enter.setText("");
				return;
			}
			amount = add;
			coin = 0;
			
			for (ItemStack item : inv.getContents()) {
				if (item != null) {
					stack = new SpoutItemStack(item);
					dur = stack.getDurability();
					
					if (stack.isCustomItem()) {
						if (dur == Coinz.CopperCoin.getDurability()) {
							coin = coin + (stack.getAmount() * 0.1);
							copp = stack;
						}
						
						if (dur == Coinz.BronzeCoin.getDurability()) {
							coin = coin + (stack.getAmount() * 1);
							bron = stack;
						}
						
						if (dur == Coinz.SilverCoin.getDurability()) {
							coin = coin + (stack.getAmount() * 10);
							silv = stack;
						}
						
						if (dur == Coinz.GoldCoin.getDurability()) {
							coin = coin + (stack.getAmount() * 100);
							gold = stack;
						}
						
						if (dur == Coinz.PlatinumCoin.getDurability()) {
							coin = coin + (stack.getAmount() * 1000);
							plat = stack;
						}
					}
				}
			}

			if (coin >= amount) {
				oldAmount = amount;
				if (plat != null && plat.getDurability() == Coinz.PlatinumCoin.getDurability()) {
					while (plat.getAmount() >=1 && amount >= 1000) {
						inv.removeItem(Coinz.PlatinumCoin);
						amount = amount - 1000;
						plat.setAmount(plat.getAmount() - 1);
					}
				}
				
				if (gold != null && gold.getDurability() == Coinz.GoldCoin.getDurability()) {
					while (gold.getAmount() >= 1 && amount >= 100) {
						inv.removeItem(Coinz.GoldCoin);
						amount = amount - 100;
						gold.setAmount(gold.getAmount() - 1);
					}
				}
				
				if (silv != null && silv.getDurability() == Coinz.SilverCoin.getDurability()) {
					while (silv.getAmount() >= 1 && amount >= 10) {
						inv.removeItem(Coinz.SilverCoin);
						amount = amount - 10;
						silv.setAmount(silv.getAmount() - 1);
					}
				}
				
				if (bron != null && bron.getDurability() == Coinz.BronzeCoin.getDurability()) {
					while (bron.getAmount() >= 1 && amount >= 1) {
						inv.removeItem(Coinz.BronzeCoin);
						amount = amount - 1;
						bron.setAmount(bron.getAmount() - 1);
					}
				}
				
				if (copp != null && copp.getDurability() == Coinz.CopperCoin.getDurability()) {
					while (copp.getAmount() >= 1 && amount >= 0.1) {
						inv.removeItem(Coinz.CopperCoin);
						amount = amount - 0.1;
						copp.setAmount(copp.getAmount() - 1);
					}
				}
				
				if (amount > 0) {
					oldAmount = oldAmount - amount;
					tellerPopup.attachWidget(plugin, tellerPopup.wrongChange);

					while (oldAmount >= 1000) {
						inv.addItem(Coinz.PlatinumCoin);
						oldAmount = oldAmount - 1000;
					}
					
					while (oldAmount >= 100) {
						inv.addItem(Coinz.GoldCoin);
						oldAmount = oldAmount - 100;
					}
					
					while (oldAmount >= 10) {
						inv.addItem(Coinz.SilverCoin);
						oldAmount = oldAmount - 10;
					}
					
					while (oldAmount >= 1) {
						inv.addItem(Coinz.BronzeCoin);
						oldAmount = oldAmount - 1;
					}
					
					while (oldAmount >= 0.1) {
						inv.addItem(Coinz.CopperCoin);
						oldAmount = oldAmount - 0.1;
					}
				}
				else {
					plugin.econ.depositPlayer(player.getName(), add);
					player.sendMessage(add + " " + "has been added to your account");
					enter.setText("");
					balance.setText(plugin.econ.format(plugin.econ.getBalance(player.getName())));
				}
			}
			
			else {
				tellerPopup.attachWidget(plugin, tellerPopup.notEnoughC);
			}
		}
	}
	
	public void withdrawCoins() {
		
		if (!enter.getText().isEmpty()) {
			try {
				remove = Double.parseDouble(enter.getText());
			}
			catch (Exception e) {
				tellerPopup.attachWidget(plugin, tellerPopup.invalidChar);
				enter.setText("");
				return;
			}
			amount = remove;
			
			if (plugin.econ.has(player.getName(), remove)) {
				
				oldAmount = amount;
				
				while (amount >= 1000) {
					inv.addItem(Coinz.PlatinumCoin);
					amount = amount - 1000;
				}
				
				while (amount >= 100) {
					inv.addItem(Coinz.GoldCoin);
					amount = amount - 100;
				}
				
				while (amount >= 10) {
					inv.addItem(Coinz.SilverCoin);
					amount = amount - 10;
				}
				
				while (amount >= 1) {
					inv.addItem(Coinz.BronzeCoin);
					amount = amount - 1;
				}
				
				while (amount >= 0.1) {
					inv.addItem(Coinz.CopperCoin);
					amount = amount - 0.1;
				}
				
				if (amount > 0) {
					oldAmount = oldAmount - amount;
					
					tellerPopup.attachWidget(plugin, tellerPopup.invalidAmount);
					enter.setText("");
					
					for (ItemStack item : inv.getContents()) {
						if (item != null) {
							stack = new SpoutItemStack(item);
							dur = stack.getDurability();
							
							if (stack.isCustomItem()) {
								if (dur == Coinz.CopperCoin.getDurability()) {
									copp = stack;
								}
								
								if (dur == Coinz.BronzeCoin.getDurability()) {
									bron = stack;
								}
								
								if (dur == Coinz.SilverCoin.getDurability()) {
									silv = stack;
								}
								
								if (dur == Coinz.GoldCoin.getDurability()) {
									gold = stack;
								}
								
								if (dur == Coinz.PlatinumCoin.getDurability()) {
									plat = stack;
								}
							}
						}
					}
					
					while (oldAmount >= 1000) {
						if (plat != null && plat.getAmount() >= 1) {
							plat.setAmount(plat.getAmount() - 1);
							oldAmount = oldAmount - 1000;
						}
					}
					
					while (oldAmount >= 100) {
						if (gold != null && gold.getAmount() >= 1) {
							gold.setAmount(gold.getAmount() - 1);
							oldAmount = oldAmount - 100;
						}
					}
					
					while (oldAmount >= 10) {
						if (silv != null && silv.getAmount() >= 1) {
							silv.setAmount(silv.getAmount() - 1);
							oldAmount = oldAmount - 10;
						}
					}
					
					while (oldAmount >= 1) {
						if (bron != null && bron.getAmount() >= 1) {
							bron.setAmount(bron.getAmount() - 1);
							oldAmount = oldAmount - 1;
						}
					}
					
					while (oldAmount >= 0.1) {
						if (copp != null && copp.getAmount() >= 1) {
							copp.setAmount(copp.getAmount() - 1);
							oldAmount = oldAmount - 0.1;
						}
					}
				}
				
				else {
					plugin.econ.withdrawPlayer(player.getName(), remove);
					player.sendMessage(enter.getText() + " " + "has been taken from your account");
					enter.setText("");
					balance.setText(plugin.econ.format(plugin.econ.getBalance(player.getName())));
				}
			}
			
			else {
				tellerPopup.attachWidget(plugin, tellerPopup.notEnoughA);
			}
		}
	}
}
