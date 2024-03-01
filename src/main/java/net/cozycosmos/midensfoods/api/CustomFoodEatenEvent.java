package net.cozycosmos.midensfoods.api;

import net.cozycosmos.midensfoods.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

public class CustomFoodEatenEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    private final Main plugin = Main.getPlugin(Main.class);
    private int foodID;
    private int hungerFill;
    private double satFill;
    private String foodName;
    private String foodConfigName;
    private List<PotionEffect> effects;
    private List<String> commands;
    Material base;
    private boolean legacy;
    FoodLevelChangeEvent originalEvent;


    public CustomFoodEatenEvent(Player player, int foodid, String FoodName, String FoodConfigName, int HungerFill, double SatFill, Material Base, boolean Legacy, List<PotionEffect> Effects, List<String> Commands, FoodLevelChangeEvent OriginalEvent) {
        super(player);
        foodID = foodid;
        foodName = FoodName;
        foodConfigName = FoodConfigName;
        hungerFill = HungerFill;
        satFill = SatFill;
        base = Base;
        legacy = Legacy;
        effects = Effects;
        commands = Commands;
        originalEvent = OriginalEvent;
    }


    @Override
    public HandlerList getHandlers() {
        return handlers;
    }


    public static HandlerList getHandlerList() {
        return handlers;
    }

    public int getFoodID() {return foodID;}
    public String getFoodName() {return foodName;}
    public String getFoodConfigName() {return foodConfigName;}
    public int getHungerFill() {return hungerFill;}
    public double getSaturationFill() {return satFill;}
    public List<PotionEffect> getEffects() {return effects;}
    public List<String> getCommands() {return commands;}
    public Material getFoodBase() {return base;}
    public boolean isLegacyFood() {return legacy;}
    public FoodLevelChangeEvent getOriginalEvent() {return originalEvent;}
}
