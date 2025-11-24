package lu.kolja.expandedae.definition;

import appeng.api.ids.AECreativeTabIds;
import appeng.api.parts.IPart;
import appeng.api.parts.IPartItem;
import appeng.api.parts.PartModels;
import appeng.api.stacks.AEKeyType;
import appeng.core.definitions.AEItems;
import appeng.core.definitions.ItemDefinition;
import appeng.items.materials.MaterialItem;
import appeng.items.materials.UpgradeCardItem;
import appeng.items.parts.PartItem;
import appeng.items.parts.PartModelsHelper;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lu.kolja.expandedae.Expandedae;
import lu.kolja.expandedae.cell.art.ArtUniverseStorageCell;
import lu.kolja.expandedae.cell.dual.AEKeyTypes;
import lu.kolja.expandedae.cell.dual.DualStorageCell;
import lu.kolja.expandedae.enums.Addons;
import lu.kolja.expandedae.item.misc.ExpPatternProviderUpgradeItem;
import lu.kolja.expandedae.item.part.ExpPatternProviderPartItem;
import lu.kolja.expandedae.part.ExpPatternProviderPart;
import lu.kolja.expandedae.xmod.megacells.MegaCells;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@SuppressWarnings("ALL")
public class ExpItems {

    private static final List<ItemDefinition<?>> ITEMS = new ArrayList<>();
    private static final Object2ObjectOpenHashMap<ItemDefinition<DualStorageCell>, String> CELLS = new Object2ObjectOpenHashMap<>();
    private static final List<ItemDefinition<?>> CPUS = new ArrayList<>();

    public static final ItemDefinition<ExpPatternProviderPartItem> EXP_PATTERN_PROVIDER_PART = Util.make(() -> {
        PartModels.registerModels(PartModelsHelper.createModels(ExpPatternProviderPart.class));
        return item("Expanded Pattern Provider", "exp_pattern_provider_part", ExpPatternProviderPartItem::new);
    });

    public static final ItemDefinition<ExpPatternProviderUpgradeItem> EXP_PATTERN_PROVIDER_UPGRADE = item(
            "Expanded Pattern Provider Upgrade",
            "exp_pattern_provider_upgrade",
            ExpPatternProviderUpgradeItem::new
    );

    public static final ItemDefinition<UpgradeCardItem> PATTERN_REFILLER_CARD = item(
            "Pattern Refiller Card",
            "pattern_refiller_card",
            p -> new UpgradeCardItem(p) {
                @Override
                public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag advancedTooltips) {
                    tooltip.add(Component.translatable("item.expandedae.pattern_refiller_card.tooltip.1").withStyle(ChatFormatting.GRAY));
                    super.appendHoverText(stack, level, tooltip, advancedTooltips);
                }
            }
    );
    public static final ItemDefinition<UpgradeCardItem> GREATER_ACCEL_CARD = item(
            "Greater Acceleration Card",
            "greater_accel_card",
            p -> new UpgradeCardItem(p) {
                @Override
                public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag advancedTooltips) {
                    tooltip.add(Component.translatable("item.expandedae.greater_accel_card.tooltip.1").withStyle(ChatFormatting.GRAY));
                    tooltip.add(Component.translatable("item.expandedae.greater_accel_card.tooltip.2").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.RED));
                    super.appendHoverText(stack, level, tooltip, advancedTooltips);
                }
            }
    );

    public static final ItemDefinition<ArtUniverseStorageCell> ART_UNIVERSE_ITEM = item(
            "Artificial Universe Item Storage Cell",
            "artificial_universe_item_cell",
            p -> new ArtUniverseStorageCell(p.stacksTo(1).rarity(Rarity.EPIC).fireResistant(),
                    100d, Long.MAX_VALUE / 8, Long.MAX_VALUE  / 128, 63, AEKeyType.items())
    );
    public static final ItemDefinition<ArtUniverseStorageCell> ART_UNIVERSE_FLUID = item(
            "Artificial Universe Fluid Storage Cell",
            "artificial_universe_fluid_cell",
            p -> new ArtUniverseStorageCell(p.stacksTo(1).rarity(Rarity.EPIC).fireResistant(),
                    100d, Long.MAX_VALUE / 8, Long.MAX_VALUE / 128, 63, AEKeyType.fluids())
    );

    public static final ItemDefinition<MaterialItem> DUAL_CELL_HOUSING = item("ME Dual Cell Housing", "dual_cell_housing", MaterialItem::new);

    public static final ItemDefinition<DualStorageCell> DUAL_1K = dualCell("1k",
            AEItems.CELL_COMPONENT_1K, DUAL_CELL_HOUSING, 1
    );
    public static final ItemDefinition<DualStorageCell> DUAL_4K = dualCell("4k",
            AEItems.CELL_COMPONENT_4K, DUAL_CELL_HOUSING, 2
    );
    public static final ItemDefinition<DualStorageCell> DUAL_16K = dualCell("16k",
            AEItems.CELL_COMPONENT_16K, DUAL_CELL_HOUSING, 3
    );
    public static final ItemDefinition<DualStorageCell> DUAL_64K = dualCell("64k",
            AEItems.CELL_COMPONENT_64K, DUAL_CELL_HOUSING, 4
    );
    public static final ItemDefinition<DualStorageCell> DUAL_256K = dualCell("256k",
            AEItems.CELL_COMPONENT_256K, DUAL_CELL_HOUSING, 5
    );

    public static Map<ItemDefinition<DualStorageCell>, String> getCells() {
        return Collections.unmodifiableMap(CELLS);
    }
    public static ItemDefinition<DualStorageCell> dualCell(
            String prefix, ItemLike coreItem, ItemLike housingItem, int tier) {
        var power = (long) Math.pow(4, tier - 1);
        var dualCell = new DualStorageCell(
                coreItem, housingItem, tier, power, power * 8, 63,
                new AEKeyTypes(AEKeyType.items(), AEKeyType.fluids())
        );
        var def = item(prefix + " ME Dual Storage Cell", "dual_storage_cell_" + prefix, p -> dualCell);
        CELLS.put(def, prefix);
        return def;
    }

    public static List<ItemDefinition<?>> getItems() {
        return Collections.unmodifiableList(ITEMS);
    }

    public static <T extends IPart> ItemDefinition<PartItem<T>> part(
            String englishName, String id, Class<T> partClass, Function<IPartItem<T>, T> factory) {
        PartModels.registerModels(PartModelsHelper.createModels(partClass));
        return item(englishName, id, p -> new PartItem<>(p, partClass, factory));
    }

    public static <T extends Item> ItemDefinition<T> item(
            String englishName, String id, Function<Item.Properties, T> factory) {
        var definition = new ItemDefinition<>(englishName, Expandedae.makeId(id), factory.apply(new Item.Properties()));
        ITEMS.add(definition);
        return definition;
    }

    static <T extends Item> ItemDefinition<T> item(
            String name, ResourceLocation id,
            Function<Item.Properties, T> factory
    ) {
        return item(name, id, factory, AECreativeTabIds.MAIN);
    }

    static <T extends Item> ItemDefinition<T> item(
            String name, ResourceLocation id,
            Function<Item.Properties, T> factory,
            ResourceKey<CreativeModeTab> group
    ) {

        Item.Properties p = new Item.Properties();
        T item = factory.apply(p);
        ItemDefinition<T> definition = new ItemDefinition<>(name, id, item);

        ITEMS.add(definition);
        return definition;
    }

    /*
    public static final ItemDefinition<FilterTerminalPartItem> FILTER_TERMINAL_PART = Util.make(() -> {
        PartModels.registerModels(PartModelsHelper.createModels(FilterTerminalPart.class));
        return item("Filter Terminal", "filter_terminal", FilterTerminalPartItem::new);
    });
    public static final ItemDefinition<ExtPatternProviderUpgradeItem> EXT_PATTERN_PROVIDER_UPGRADE = item(
            "Extended Pattern Provider Upgrader",
            "ext_pattern_provider_upgrader",
            ExtPatternProviderUpgradeItem::new
    );
    public static final ItemDefinition<ItemAdvancedBlockingCard> ADVANCED_BLOCKING_CARD = item(
            "Advanced Blocking Card",
            "advanced_blocking_card",
            ItemAdvancedBlockingCard::new
    );
    public static final ItemDefinition<ItemSmartBlockingCard> SMART_BLOCKING_CARD = item(
            "Smart Blocking Card",
            "smart_blocking_card",
            ItemSmartBlockingCard::new
    );
    public static final ItemDefinition<ItemStickyCard> STICKY_CARD = item(
            "Sticky Card",
            "sticky_card",
            ItemStickyCard::new
    );
    */

    static {
        if (Addons.MEGA.isLoaded) MegaCells.initItems();
    }

    public static void init() {
        // controls static load order
        Expandedae.LOGGER.info("Initialised items.");
    }
}
