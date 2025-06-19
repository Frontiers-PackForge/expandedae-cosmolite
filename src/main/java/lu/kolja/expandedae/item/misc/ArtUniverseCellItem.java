package lu.kolja.expandedae.item.misc;

import appeng.api.stacks.AEKeyType;
import com.google.common.base.Preconditions;
import lombok.Getter;
import lu.kolja.expandedae.artuniversecell.CellHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Getter
public class ArtUniverseCellItem extends Item {
    private final AEKeyType keyType;

    public ArtUniverseCellItem(AEKeyType keyType) {
        super(new Properties().stacksTo(1).fireResistant().rarity(Rarity.EPIC));
        this.keyType = keyType;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        Preconditions.checkArgument(stack.getItem() == this);
        var handler = CellHandler.INSTANCE.getCellInventory(stack, null);
        CompoundTag tag = stack.getTag();
        if (tag != null && handler != null && handler.hasUUID()) {
            tooltip.add(Component.literal("UUID: ").withStyle(ChatFormatting.GRAY).append(Component.literal(handler.getUUID().toString()).withStyle(ChatFormatting.AQUA)));
            tooltip.add(Component.literal("Stored: ").withStyle(ChatFormatting.GRAY).append(Component.literal(handler.getTotalStorage()).withStyle(ChatFormatting.GREEN)));
            tooltip.add(Component.literal("Types: ").withStyle(ChatFormatting.GRAY).append(Component.literal(handler.getMaxTypes()).withStyle(ChatFormatting.LIGHT_PURPLE)));
        }
    }
    /*
    public void addCellInformationToTooltip(ItemStack is, List<Component> lines, CellInventory handler) {
        if (handler != null) {
            lines.add(Tooltips.typesUsed(handler.getStoredItemTypes(), handler.getTotalItemTypes()));
            if (handler.isPreformatted()) {
                MutableComponent list = (handler.getPartitionListMode() == IncludeExclude.WHITELIST ? GuiText.Included : GuiText.Excluded).text();
                if (handler.isFuzzy()) {
                    lines.add(GuiText.Partitioned.withSuffix(" - ").append(list).append(" ").append(GuiText.Fuzzy.text()));
                } else {
                    lines.add(GuiText.Partitioned.withSuffix(" - ").append(list).append(" ").append(GuiText.Precise.text()));
                }
            }

        }
    }

    public Optional<TooltipComponent> getTooltipImage(ItemStack is) {
        BasicCellInventory handler = this.getCellInventory(is, (ISaveProvider)null);
        if (handler == null) {
            return Optional.empty();
        } else {
            ArrayList<ItemStack> upgradeStacks = new ArrayList();
            if (AEConfig.instance().isTooltipShowCellUpgrades()) {
                for(ItemStack upgrade : handler.getUpgradesInventory()) {
                    upgradeStacks.add(upgrade);
                }
            }

            boolean hasMoreContent;
            List<GenericStack> content;
            if (AEConfig.instance().isTooltipShowCellContent()) {
                content = new ArrayList();
                int maxCountShown = AEConfig.instance().getTooltipMaxCellContentShown();
                KeyCounter availableStacks = handler.getAvailableStacks();

                for(Object2LongMap.Entry<AEKey> entry : availableStacks) {
                    content.add(new GenericStack((AEKey)entry.getKey(), entry.getLongValue()));
                }

                if (content.size() < maxCountShown && handler.getPartitionListMode() == IncludeExclude.WHITELIST) {
                    ConfigInventory config = handler.getConfigInventory();

                    for(int i = 0; i < config.size(); ++i) {
                        AEKey what = config.getKey(i);
                        if (what != null && availableStacks.get(what) <= 0L) {
                            content.add(new GenericStack(what, 0L));
                        }

                        if (content.size() > maxCountShown) {
                            break;
                        }
                    }
                }

                content.sort(Comparator.comparingLong(GenericStack::amount).reversed());
                hasMoreContent = content.size() > maxCountShown;
                if (content.size() > maxCountShown) {
                    content.subList(maxCountShown, content.size()).clear();
                }
            } else {
                hasMoreContent = false;
                content = Collections.emptyList();
            }

            return Optional.of(new StorageCellTooltipComponent(upgradeStacks, content, hasMoreContent, true));
        }
    }
    public void addCellInformationToTooltip(ItemStack is, List<Component> lines) {
        BasicCellInventory handler = this.getCellInventory(is, (ISaveProvider)null);
        if (handler != null) {
            lines.add(Tooltips.bytesUsed(handler.getUsedBytes(), handler.getTotalBytes()));
            lines.add(Tooltips.typesUsed(handler.getStoredItemTypes(), handler.getTotalItemTypes()));
            if (handler.isPreformatted()) {
                MutableComponent list = (handler.getPartitionListMode() == IncludeExclude.WHITELIST ? GuiText.Included : GuiText.Excluded).text();
                if (handler.isFuzzy()) {
                    lines.add(GuiText.Partitioned.withSuffix(" - ").append(list).append(" ").append(GuiText.Fuzzy.text()));
                } else {
                    lines.add(GuiText.Partitioned.withSuffix(" - ").append(list).append(" ").append(GuiText.Precise.text()));
                }
            }

        }
    }

    public Optional<TooltipComponent> getTooltipImage(ItemStack is) {
        BasicCellInventory handler = this.getCellInventory(is, (ISaveProvider)null);
        if (handler == null) {
            return Optional.empty();
        } else {
            ArrayList<ItemStack> upgradeStacks = new ArrayList();
            if (AEConfig.instance().isTooltipShowCellUpgrades()) {
                for(ItemStack upgrade : handler.getUpgradesInventory()) {
                    upgradeStacks.add(upgrade);
                }
            }

            boolean hasMoreContent;
            List<GenericStack> content;
            if (AEConfig.instance().isTooltipShowCellContent()) {
                content = new ArrayList();
                int maxCountShown = AEConfig.instance().getTooltipMaxCellContentShown();
                KeyCounter availableStacks = handler.getAvailableStacks();

                for(Object2LongMap.Entry<AEKey> entry : availableStacks) {
                    content.add(new GenericStack((AEKey)entry.getKey(), entry.getLongValue()));
                }

                if (content.size() < maxCountShown && handler.getPartitionListMode() == IncludeExclude.WHITELIST) {
                    ConfigInventory config = handler.getConfigInventory();

                    for(int i = 0; i < config.size(); ++i) {
                        AEKey what = config.getKey(i);
                        if (what != null && availableStacks.get(what) <= 0L) {
                            content.add(new GenericStack(what, 0L));
                        }

                        if (content.size() > maxCountShown) {
                            break;
                        }
                    }
                }

                content.sort(Comparator.comparingLong(GenericStack::amount).reversed());
                hasMoreContent = content.size() > maxCountShown;
                if (content.size() > maxCountShown) {
                    content.subList(maxCountShown, content.size()).clear();
                }
            } else {
                hasMoreContent = false;
                content = Collections.emptyList();
            }

            return Optional.of(new StorageCellTooltipComponent(upgradeStacks, content, hasMoreContent, true));
        }
    }*/
}
