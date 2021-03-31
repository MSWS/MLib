package xyz.msws.mlib.menu;

import com.sun.istack.internal.NotNull;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.msws.mlib.utils.Callback;
import xyz.msws.mlib.utils.MSG;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Basic Item Builder and wrapper of a specific {@link InventoryClickEvent} {@link Callback}
 */
@SerializableAs("CItem")
public class CItem implements ConfigurationSerializable {
    private final ItemStack item;
    private final ItemMeta meta;

    private Callback<InventoryClickEvent> action;

    public CItem(ItemStack item) {
        this.item = item;
        this.meta = item.getItemMeta();
    }

    public CItem(Material mat) {
        this(new ItemStack(mat));
    }

    public CItem(Material mat, String name, String... lore) {
        this(mat);
        meta.setDisplayName(MSG.color(name));
        lore(Arrays.asList(lore));
    }

    public CItem amo(int amo) {
        item.setAmount(amo);
        return this;
    }

    public CItem name(String name) {
        meta.setDisplayName(MSG.color(name));
        return this;
    }

    public CItem flag(ItemFlag flag) {
        meta.addItemFlags(flag);
        return this;
    }

    public String getName() {
        return meta.getDisplayName();
    }

    public CItem lore(List<String> lore) {
        meta.setLore(lore.stream().map(MSG::color).collect(Collectors.toList()));
        return this;
    }

    public List<String> getLore() {
        return meta.getLore();
    }

    public ItemStack build() {
        item.setItemMeta(meta);
        return item;
    }

    public CItem setAction(Callback<InventoryClickEvent> action) {
        this.action = action;
        return this;
    }

    public Callback<InventoryClickEvent> getAction() {
        return action;
    }

    public static CItem deserialize(Map<String, Object> data) {
        CItem item = new CItem(Material.matchMaterial((String) data.get("Type")));
        item.amo((Integer) data.getOrDefault("Amount", 1));
        item.lore((List<String>) data.getOrDefault("Lore", new ArrayList<>()));
        if (data.containsKey("Name"))
            item.name((String) data.get("Name"));
        if (data.containsKey("Flags"))
            for (String s : (List<String>) data.get("Flags"))
                item.flag(ItemFlag.valueOf(s));
        return item;
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("Type", item.getType().toString());
        data.put("Amount", item.getAmount());
        data.put("Lore", meta.getLore());
        data.put("Name", meta.getDisplayName());
        data.put("Flags", meta.getItemFlags());

        return data;
    }
}
