package lu.kolja.expandedae.storage;

import appeng.items.AEBaseItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ExpandedStorageComponentItem extends AEBaseItem implements IExpandedStorageComponent {
    private final long storageInBytes;

    public ExpandedStorageComponentItem(Item.Properties properties, long storageInBytes) {
        super(properties);
        this.storageInBytes = storageInBytes;
    }

    public long getBytes(ItemStack is) {
        return this.storageInBytes;
    }

    public boolean isStorageComponent(ItemStack is) {
        return true;
    }

}
