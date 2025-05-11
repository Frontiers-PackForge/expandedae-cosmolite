package lu.kolja.expandedae.client.gui;

import appeng.util.inv.AppEngInternalInventory;
import lu.kolja.expandedae.block.entity.FilterContainerGroup;
import org.jetbrains.annotations.NotNull;

public class FilterContainerRecord implements Comparable<FilterContainerRecord> {
    private final FilterContainerGroup group;
    private final String searchName;
    private final long serverId;
    private final AppEngInternalInventory inventory;
    private final long order;

    public FilterContainerRecord(long serverId, int slots, long order, FilterContainerGroup group) {
        this.inventory = new AppEngInternalInventory(slots);
        this.group = group;
        this.searchName = group.name().getString().toLowerCase();
        this.serverId = serverId;
        this.order = order;
    }

    public FilterContainerGroup getGroup() {
        return this.group;
    }

    public String getSearchName() {
        return this.searchName;
    }

    public int compareTo(@NotNull FilterContainerRecord o) {
        return Long.compare(this.order, o.order);
    }

    public long getServerId() {
        return this.serverId;
    }

    public AppEngInternalInventory getInventory() {
        return this.inventory;
    }
}
