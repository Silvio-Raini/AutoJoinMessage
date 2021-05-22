package net.fbgstudios.joinmessage;

import net.labymod.api.LabyModAddon;
import net.labymod.api.events.MessageReceiveEvent;
import net.labymod.settings.elements.BooleanElement;
import net.labymod.settings.elements.ControlElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.settings.elements.StringElement;
import net.labymod.utils.Consumer;
import net.labymod.utils.Material;
import net.minecraft.client.Minecraft;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main extends LabyModAddon {
    private String message = "";
    private boolean bEnabled = false;
    @Override
    public void onEnable() {
        this.getApi().registerForgeListener(this);
        this.getApi().getEventManager().register(new MessageReceiveEvent() {
            @Override
            public boolean onReceive(String unformatted, String s1) {
                Pattern joinMessagePattern = Pattern.compile("^§r§8\\[§r§6GrieferGames§r§8\\] §r§aDeine Daten wurden vollständig heruntergeladen.§r$");
                Matcher m = joinMessagePattern.matcher(unformatted);

                if (m.matches() && bEnabled) {
                    Minecraft.getMinecraft().thePlayer.sendChatMessage(message);
                }
                return false;
            }
        });
    }

    @Override
    public void loadConfig() {
        this.message = this.getConfig().has("JoinMessage") ? this.getConfig().get("JoinMessage").getAsString() : message;
        this.bEnabled = this.getConfig().has("Enabled") ? this.getConfig().get("Enabled").getAsBoolean() : bEnabled;
    }

    @Override
    protected void fillSettings(List<SettingsElement> list) {
        final StringElement messageOnJoinElement = new StringElement("Nachricht bei Serverjoin", new ControlElement.IconData(Material.PAPER), message, new Consumer<String>() {
            @Override
            public void accept(String userEnter) {
                message = userEnter;
                Main.this.getConfig().addProperty("JoinMessage", message);
                Main.this.saveConfig();
            }
        });
        list.add(messageOnJoinElement);

        final BooleanElement messageOnJoinEnabledElement = new BooleanElement(("Aktiviert"), new ControlElement.IconData(Material.REDSTONE), new Consumer<Boolean>() {
            @Override
            public void accept(Boolean status) {
                bEnabled = status;
                Main.this.getConfig().addProperty("Enabled", bEnabled);
                Main.this.saveConfig();
            }
        }, this.bEnabled);
        list.add(messageOnJoinEnabledElement);
    }
}
