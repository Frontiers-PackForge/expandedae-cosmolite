package lu.kolja.expandedae.storage;

import net.minecraft.world.item.ItemStack;

public interface IExpandedStorageComponent {
    long getBytes(ItemStack itemStack);

    boolean isStorageComponent(ItemStack itemStack);
}
