package lu.kolja.expandedae.part;

import appeng.api.parts.IPartItem;
import appeng.api.parts.IPartModel;
import appeng.core.AppEngBase;
import appeng.helpers.patternprovider.PatternProviderLogic;
import appeng.items.parts.PartModels;
import appeng.menu.ISubMenu;
import appeng.menu.MenuOpener;
import appeng.menu.locator.MenuLocator;
import appeng.parts.PartModel;
import appeng.parts.crafting.PatternProviderPart;
import lu.kolja.expandedae.Expandedae;
import lu.kolja.expandedae.block.ExpPatternProviderBlock;
import lu.kolja.expandedae.definition.ExpItems;
import lu.kolja.expandedae.definition.ExpMenus;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.List;

public class ExpPatternProviderPart extends PatternProviderPart {
    public static List<ResourceLocation> MODELS = Arrays.asList(
            new ResourceLocation(Expandedae.MODID, "part/exp_pattern_provider_base"),
            new ResourceLocation(AppEngBase.MOD_ID, "part/interface_on"),
            new ResourceLocation(AppEngBase.MOD_ID, "part/interface_off"),
            new ResourceLocation(AppEngBase.MOD_ID, "part/interface_has_channel")
    );
    @PartModels
    public static final PartModel MODELS_OFF = new PartModel(MODELS.get(0), MODELS.get(2));
    @PartModels
    public static final PartModel MODELS_ON = new PartModel(MODELS.get(0), MODELS.get(1));
    @PartModels
    public static final PartModel MODELS_HAS_CHANNEL = new PartModel(MODELS.get(0), MODELS.get(3));

    public ExpPatternProviderPart(IPartItem<?> partItem) {
        super(partItem);
    }

    @Override
    public PatternProviderLogic createLogic() {
        return ExpPatternProviderBlock.createLogic(this.getMainNode(), this);
    }

    @Override
    public void openMenu(Player player, MenuLocator locator) {
        MenuOpener.open(ExpMenus.EXP_PATTERN_PROVIDER, player, locator);
    }

    @Override
    public void returnToMainMenu(Player player, ISubMenu subMenu) {
        MenuOpener.returnTo(ExpMenus.EXP_PATTERN_PROVIDER, player, subMenu.getLocator());
    }

    @Override
    public IPartModel getStaticModels() {
        if (isActive() && isPowered()) {
            return MODELS_HAS_CHANNEL;
        } else if (isPowered()) {
            return MODELS_ON;
        } else {
            return MODELS_OFF;
        }
    }

    @Override
    public ItemStack getMainMenuIcon() {
        return ExpItems.EXP_PATTERN_PROVIDER_PART.stack();
    }
}
