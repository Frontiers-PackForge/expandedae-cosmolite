package lu.kolja.expandedae.definition;

import appeng.block.AEBaseBlock;
import appeng.block.AEBaseBlockItem;
import appeng.core.definitions.ItemDefinition;
import appeng.items.AEBaseItem;
import lu.kolja.expandedae.Expandedae;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;

import java.util.ArrayList;

public class ExpCreativeTab {
    public static final ResourceLocation ID = Expandedae.makeId("tab");

    public static final CreativeModeTab TAB = CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.eae"))
            .icon(ExpBlocks.EXP_PATTERN_PROVIDER::stack)
            .displayItems(ExpCreativeTab::populateTab)
            .build();

    private static void populateTab(CreativeModeTab.ItemDisplayParameters ignored, CreativeModeTab.Output output) {
        var itemDefs = new ArrayList<ItemDefinition<?>>();
        itemDefs.addAll(ExpItems.getItems());
        itemDefs.addAll(ExpBlocks.getBlocks());

        for (var itemDef : itemDefs) {
            var item = itemDef.asItem();

            // For block items, the block controls the creative tab
            if (item instanceof AEBaseBlockItem baseItem && baseItem.getBlock() instanceof AEBaseBlock baseBlock) {
                baseBlock.addToMainCreativeTab(output);
            } else if (item instanceof AEBaseItem baseItem) {
                baseItem.addToMainCreativeTab(output);
            } else {
                output.accept(itemDef);
            }
        }
        var cpus = ExpBlocks.getCPUs();
        for (var cpu : cpus.keySet()) {
            var item = cpus.get(cpu).block();
            if (cpu.isEnabled()) {
                item.addToMainCreativeTab(output);
            }
        }
    }
}
