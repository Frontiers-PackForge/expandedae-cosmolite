package lu.kolja.expandedae.client;

import appeng.core.definitions.ItemDefinition;
import appeng.items.AEBaseItem;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lu.kolja.expandedae.Expandedae;
import lu.kolja.expandedae.cell.dual.DualStorageCell;
import lu.kolja.expandedae.definition.ExpItems;
import lu.kolja.expandedae.enums.Addons;
import lu.kolja.expandedae.xmod.megacells.MegaCells;
import net.minecraft.resources.ResourceLocation;

public class ExpCellModels {
    public static final Object2ObjectOpenHashMap<ItemDefinition<? extends AEBaseItem>, ResourceLocation> cellModels = new Object2ObjectOpenHashMap<>(10);

    public static final ResourceLocation DUAL_1K = cellModel(ExpItems.DUAL_1K, false);
    public static final ResourceLocation DUAL_4K = cellModel(ExpItems.DUAL_4K, false);
    public static final ResourceLocation DUAL_16K = cellModel(ExpItems.DUAL_16K, false);
    public static final ResourceLocation DUAL_64K = cellModel(ExpItems.DUAL_64K, false);
    public static final ResourceLocation DUAL_256K = cellModel(ExpItems.DUAL_256K, false);
    public static ResourceLocation DUAL_1M;
    public static ResourceLocation DUAL_4M;
    public static ResourceLocation DUAL_16M;
    public static ResourceLocation DUAL_64M;
    public static ResourceLocation DUAL_256M;
    static {
        if (Addons.MEGA.isLoaded) {
            DUAL_1M = cellModel(MegaCells.DUAL_1M, true);
            DUAL_4M = cellModel(MegaCells.DUAL_4M, true);
            DUAL_16M = cellModel(MegaCells.DUAL_16M, true);
            DUAL_64M = cellModel(MegaCells.DUAL_64M, true);
            DUAL_256M = cellModel(MegaCells.DUAL_256M, true);
        }
    }


    public static ResourceLocation cellModel(ItemDefinition<DualStorageCell> item, boolean isMega) {
        var model = Expandedae.makeId("block/drive/cells/" + item.id().getPath());
        cellModels.put(item, model);
        return model;
    }
}
