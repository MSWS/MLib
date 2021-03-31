package xyz.msws.mlib.menu;

import org.bukkit.entity.HumanEntity;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Basic wrapper that represents a paged {@link AbstractMenu}. Method to refresh inventory should be
 * {@link AbstractMenu#displayTo(HumanEntity)} after calling setPage
 */
public class PagedMenu extends AbstractMenu {

    protected final List<Map<Integer, CItem>> pages = new ArrayList<>();
    protected int page = 0;

    public PagedMenu(Plugin plugin) {
        super(plugin);
    }

    public void addPage(Map<Integer, CItem> page) {
        this.pages.add(page);
    }

    public void removePage(int page) {
        pages.remove(page);
    }

    public void removePage(Map<Integer, CItem> page) {
        pages.remove(page);
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    /**
     * Override and auto-refresh the items according to the current page, then run native displayTo method
     *
     * @param player Player to show the item to
     */
    @Override
    public void displayTo(HumanEntity player) {
        this.items.clear();
        this.items.putAll(pages.get(page));
        super.displayTo(player);
    }
}
