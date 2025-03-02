package lu.kolja.expandedae.util;

import lu.kolja.expandedae.Expandedae;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class EAETags {
    public static final TagKey<Item> EXP_PATTERN_PROVIDER = TagKey.create(Registries.ITEM, Expandedae.id("exp_pattern_provider"));
}
