package lu.kolja.expandedae.storage;

import java.util.function.Supplier;

import net.minecraft.world.item.Item;

public record ExpandedStorageTier(int index, String namePrefix, long bytes, double idleDrain, Supplier<Item> componentSupplier) {

    public ExpandedStorageTier(int index, String namePrefix, long bytes, double idleDrain, Supplier<Item> componentSupplier) {
        this.index = index;
        this.namePrefix = namePrefix;
        this.bytes = bytes;
        this.idleDrain = idleDrain;
        this.componentSupplier = componentSupplier;
    }

    public int index() {
        return this.index;
    }

    public String namePrefix() {
        return this.namePrefix;
    }

    public long bytes() {
        return this.bytes;
    }

    public double idleDrain() {
        return this.idleDrain;
    }

    public Supplier<Item> componentSupplier() {
        return this.componentSupplier;
    }
}