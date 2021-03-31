package xyz.msws.mlib.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import xyz.msws.mlib.utils.Callback;
import xyz.msws.mlib.utils.MSG;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a generic menu, supports {@link CItem} actions and cancels all events within this inventory
 */
public class AbstractMenu implements Listener {

    protected final Plugin plugin;

    private final HashSet<UUID> open = new HashSet<>();

    protected Inventory inv;
    protected final Map<Integer, CItem> items = new HashMap<>();

    public AbstractMenu(Plugin plugin) {
        this.plugin = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public AbstractMenu(Plugin plugin, int size) {
        this(plugin);
        inv = Bukkit.createInventory(null, size);
    }

    public AbstractMenu(Plugin plugin, int size, String title) {
        this(plugin);
        inv = Bukkit.createInventory(null, size, MSG.color(title));
    }

    /**
     * Fills all slots that are empty with this item
     *
     * @param item Item to fill the inventory with
     */
    public void setBackground(CItem item) {
        for (int i = 0; i < inv.getSize(); i++) {
            if (items.containsKey(i))
                continue;
            items.put(i, item);
        }
    }

    /**
     * Shows the inventory to the specified player
     * This also automatically places all the items from the internal items map into the according slots
     *
     * @param player Player to show the item to
     */
    public void displayTo(HumanEntity player) {
        items.forEach((k, v) -> inv.setItem(k, v.build()));

        player.openInventory(inv);
        open.add(player.getUniqueId());
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!open.contains(event.getWhoClicked().getUniqueId()))
            return;
        event.setCancelled(true);
        if (!items.containsKey(event.getRawSlot()))
            return;
        Callback<InventoryClickEvent> call = items.get(event.getRawSlot()).getAction();
        if (call == null)
            return;
        call.execute(event);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        open.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        open.remove(event.getPlayer().getUniqueId());
    }
}
