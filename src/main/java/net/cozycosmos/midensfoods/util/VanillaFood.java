package net.cozycosmos.midensfoods.util;

public enum VanillaFood {
    APPLE(4,2.4f),
    BAKED_POTATO(5,6.0f),
    BEEF(3,1.8f),
    BEETROOT(1,1.2f),
    BEETROOT_SOUP(6,7.2f),
    BREAD(5,6.0f),
    CARROT(3,3.6f),
    CHICKEN(2,1.2f),
    CHORUS_FRUIT(4,2.4f),
    COD(2,0.4f),
    COOKED_CHICKEN(6,7.2f),
    COOKED_COD(5,6.0f),
    COOKED_MUTTON(6,9.6f),
    COOKED_PORKCHOP(8,12.8f),
    COOKED_RABBIT(5,6.0f),
    COOKED_SALMON(6,9.6f),
    COOKED_BEEF(8,12.8f),
    COOKIE(2,0.4f),
    DRIED_KELP(1,0.6f),
    ENCHANTED_GOLDEN_APPLE(4,9.6f),
    GLOW_BERRIES(2,0.4f),
    GOLDEN_APPLE(4,9.6f),
    GOLDEN_CARROT(4,14.4f),
    HONEY_BOTTLE(6,1.2f),
    MELON_SLICE(2,1.2f),
    MUSHROOM_STEW(4,7.2f),
    MUTTON(2,1.2f),
    POISONOUS_POTATO(2,1.2f),
    PORKCHOP(3,1.8f),
    POTATO(1,0.6f),
    PUFFERFISH(1,0.2f),
    PUMPKIN_PIE(8,4.8f),
    RABBIT_STEW(10,12.0f),
    RABBIT(3,1.8f),
    ROTTEN_FLESH(4,0.8f),
    SALMON(2,0.4f),
    SPIDER_EYE(2,3.2f),
    SWEET_BERRIES(2,0.4f),
    TROPICAL_FISH(1,0.2f);

    private int foodValue;
    private float satValue;
     VanillaFood(int FoodValue, float SatValue) {
        foodValue = FoodValue;
         satValue = SatValue;
    }

    public int getFoodValue() {
        return foodValue;
    }

    public float getSatValue() {
        return satValue;
    }
}
