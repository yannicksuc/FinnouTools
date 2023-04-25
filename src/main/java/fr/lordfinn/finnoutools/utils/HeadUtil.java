package fr.lordfinn.finnoutools.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.UUID;

public class HeadUtil {

    public static ItemStack createCustomPlayerHead(String url, String name)
    {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
        if (url.isEmpty()) return head;

        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), "");

        profile.getProperties().put("textures", new Property("textures", url));
        try
        {
            Field profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        }
        catch (IllegalArgumentException|NoSuchFieldException|SecurityException | IllegalAccessException error)
        {
            error.printStackTrace();
        }
        head.setItemMeta(headMeta);
        return head;
    }

    public static boolean arePlayerHeadsSimilar(ItemStack head1, ItemStack head2) {

        SkullMeta skullMeta1 = (SkullMeta) head1.getItemMeta();
        SkullMeta skullMeta2 = (SkullMeta) head2.getItemMeta();

        GameProfile profile1 = getGameProfileFromSkull(skullMeta1);
        GameProfile profile2 = getGameProfileFromSkull(skullMeta2);

        if (profile1 == null || profile2 == null) {
            return false;
        }

        Property texture1 = profile1.getProperties().get("textures").iterator().next();
        Property texture2 = profile2.getProperties().get("textures").iterator().next();

        return texture1.getValue().equals(texture2.getValue());
    }

    private static GameProfile getGameProfileFromSkull(SkullMeta skullMeta) {
        try {
            Field profileField = skullMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            return (GameProfile) profileField.get(skullMeta);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

}
