package lu.kolja.expandedae.datagen.model;

import appeng.core.AppEng;
import appeng.core.definitions.ItemDefinition;
import lu.kolja.expandedae.Expandedae;
import lu.kolja.expandedae.client.ExpCellModels;
import lu.kolja.expandedae.definition.ExpItems;
import lu.kolja.expandedae.xmod.megacells.MegaCells;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ExpItemModelProvider extends ItemModelProvider {
    public ExpItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Expandedae.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (var cell : ExpItems.getCells().entrySet()) {
            storageCell(cell.getKey(), "item/dual_storage_cell_" + cell.getValue());
        }
        for (var cell : ExpCellModels.cellModels.object2ObjectEntrySet()) {
            driveCell(cell.getValue());
        }
        basicItem(ExpItems.DUAL_CELL_HOUSING.asItem());
        basicItem(MegaCells.DUAL_CELL_MEGA_HOUSING.asItem());
    }

    private void storageCell(ItemDefinition<?> item, String background) {
        singleTexture(
                item.id().getPath(),
                mcLoc("item/generated"),
                "layer0",
                Expandedae.makeId(background)
        ).texture("layer1", "ae2:item/storage_cell_led");
    }

    private void driveCell(ResourceLocation texture) {
        var loc = Expandedae.makeId("block/drive/cells/" + (texture.toString().endsWith("m") ? "mega_" : "") + "dual_cell");
        singleTexture(
                texture.toString(),
                AppEng.makeId("block/drive/drive_cell"),
                "cell",
                loc
        );
    }
}
