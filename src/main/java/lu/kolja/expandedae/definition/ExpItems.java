package lu.kolja.expandedae.definition;

import appeng.api.parts.IPart;
import appeng.api.parts.IPartItem;
import appeng.api.parts.PartModels;
import appeng.core.definitions.ItemDefinition;
import appeng.items.parts.PartItem;
import appeng.items.parts.PartModelsHelper;
import lu.kolja.expandedae.Expandedae;
import lu.kolja.expandedae.item.ExpPatternProviderUpgradeItem;
import lu.kolja.expandedae.item.part.ExpPatternProviderPart;
import lu.kolja.expandedae.item.part.ExpPatternProviderPartItem;
import net.minecraft.Util;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class ExpItems {

    private static final List<ItemDefinition<?>> ITEMS = new ArrayList<>();

    public static List<ItemDefinition<?>> getItems() {
        return Collections.unmodifiableList(ITEMS);
    }

    public static final ItemDefinition<ExpPatternProviderPartItem> EXP_PATTERN_PROVIDER_PART = Util.make(() -> {
        PartModels.registerModels(PartModelsHelper.createModels(ExpPatternProviderPart.class));
        return item("Expanded Pattern Provider", "exp_pattern_provider_part", ExpPatternProviderPartItem::new);
    });

    public static final ItemDefinition<ExpPatternProviderUpgradeItem> EXP_PATTERN_PROVIDER_UPGRADE = item(
            "Expanded Pattern Provider Upgrade",
            "exp_pattern_provider_upgrade",
            ExpPatternProviderUpgradeItem::new
    );

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
}
