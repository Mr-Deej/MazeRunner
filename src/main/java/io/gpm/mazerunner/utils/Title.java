package io.gpm.mazerunner.utils;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

/***
 * @author George
 * @since 18-Jun-19
 */
public class Title {

    private String title;
    private String subtitle;
    private int fadeIn, stay, fadeOut;

    public Title(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        this.title = title;
        this.subtitle = subtitle;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    public void send(Player player) {
        IChatBaseComponent titleCb = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}");
        IChatBaseComponent subtitleCb = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}");

        //packet handling
        PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, titleCb);
        PacketPlayOutTitle subtitlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, subtitleCb);
        PacketPlayOutTitle length = new PacketPlayOutTitle(fadeIn, stay, fadeOut);

        //send to the player
        (((CraftPlayer)player).getHandle()).playerConnection.sendPacket(titlePacket);
        (((CraftPlayer)player).getHandle()).playerConnection.sendPacket(subtitlePacket);
        (((CraftPlayer)player).getHandle()).playerConnection.sendPacket(length);
    }

    public void sendToAll() {
        Bukkit.getServer().getOnlinePlayers().forEach(this::send);
    }
}
