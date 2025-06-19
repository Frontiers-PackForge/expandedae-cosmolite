package lu.kolja.expandedae.definition;

import appbot.ae2.ManaKeyType;
import appeng.api.ids.AECreativeTabIds;
import appeng.api.parts.IPart;
import appeng.api.parts.IPartItem;
import appeng.api.parts.PartModels;
import appeng.api.stacks.AEKeyType;
import appeng.core.definitions.ItemDefinition;
import appeng.items.parts.PartItem;
import appeng.items.parts.PartModelsHelper;
import com.glodblock.github.appflux.common.me.key.type.FluxKeyType;
import gripe._90.arseng.me.key.SourceKeyType;
import lu.kolja.expandedae.Expandedae;
import lu.kolja.expandedae.item.cards.ItemAutoCompleteCard;
import lu.kolja.expandedae.item.cards.ItemPatternRefillerCard;
import lu.kolja.expandedae.item.misc.ArtUniverseCellItem;
import lu.kolja.expandedae.item.misc.DummyItem;
import lu.kolja.expandedae.item.misc.ExpPatternProviderUpgradeItem;
import lu.kolja.expandedae.item.part.ExpPatternProviderPartItem;
import lu.kolja.expandedae.part.ExpPatternProviderPart;
import lu.kolja.expandedae.xmod.XMod;
import me.ramidzkh.mekae2.ae2.MekanismKeyType;
import net.minecraft.Util;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

@SuppressWarnings("ALL")
public class ExpItems {

    public static void init() {
        // controls static load order
        Expandedae.LOGGER.info("Initialised items.");
    }

    private static final List<ItemDefinition<?>> ITEMS = new ArrayList<>();

    public static final ItemDefinition<ExpPatternProviderPartItem> EXP_PATTERN_PROVIDER_PART = Util.make(() -> {
        PartModels.registerModels(PartModelsHelper.createModels(ExpPatternProviderPart.class));
        return item("Expanded Pattern Provider", "exp_pattern_provider_part", ExpPatternProviderPartItem::new);
    });

    public static final ItemDefinition<ExpPatternProviderUpgradeItem> EXP_PATTERN_PROVIDER_UPGRADE = item(
            "Expanded Pattern Provider Upgrade",
            "exp_pattern_provider_upgrade",
            ExpPatternProviderUpgradeItem::new
    );

    public static final ItemDefinition<ItemAutoCompleteCard> AUTO_COMPLETE_CARD = item(
            "Auto Complete Card",
            "auto_complete_card",
            ItemAutoCompleteCard::new
    );
    public static final ItemDefinition<ItemPatternRefillerCard> PATTERN_REFILLER_CARD = item(
            "Pattern Refiller Card",
            "pattern_refiller_card",
            ItemPatternRefillerCard::new
    );

    public static final ItemDefinition<ArtUniverseCellItem> ART_ITEM = cell(
            "Artificial Universe Item Cell",
            AEKeyType.items()
    );
    public static final ItemDefinition<ArtUniverseCellItem> ART_FLUID = cell(
            "Artificial Universe Fluid Cell",
            AEKeyType.fluids()
    );
    public static final ItemDefinition<Item> ART_CHEMICAL = addonCell(
            "Artificial Universe Chemical Cell",
            MekanismKeyType.TYPE,
            XMod.Mods.APPMEK
    );
    public static final ItemDefinition<Item> ART_FE = addonCell(
            "Artificial Universe FE Cell",
            FluxKeyType.TYPE,
            XMod.Mods.APPFLUX
    );
    public static final ItemDefinition<Item> ART_SOURCE = addonCell(
            "Artificial Universe Source Cell",
            SourceKeyType.TYPE,
            XMod.Mods.ARSENG
    );
    public static final ItemDefinition<Item> ART_MANA = addonCell(
            "Artificial Universe Mana Cell",
            ManaKeyType.TYPE,
            XMod.Mods.APPBOT
    );


    public static List<ItemDefinition<?>> getItems() {
        return Collections.unmodifiableList(ITEMS);
    }

    public static ItemDefinition<ArtUniverseCellItem> cell(
            String englishName, AEKeyType keyType
    ) {
        return item(englishName, englishName.toLowerCase().replace(" ", "_"),
                p -> new ArtUniverseCellItem(keyType)
        );
    }
    public static ItemDefinition<Item> addonCell(
            String englishName, AEKeyType keyType, XMod.Mods mod
    ) {
        return item(englishName, englishName.toLowerCase().replace(" ", "_"),
                p -> mod.isLoaded() ? new ArtUniverseCellItem(keyType) : new DummyItem(p, mod)
        );
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

    public static void orderInit() {}

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
}
