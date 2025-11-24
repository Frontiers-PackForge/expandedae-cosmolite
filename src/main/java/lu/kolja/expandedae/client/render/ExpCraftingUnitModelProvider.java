package lu.kolja.expandedae.client.render;

import appeng.client.render.crafting.AbstractCraftingUnitModelProvider;
import appeng.client.render.crafting.LightBakedModel;
import appeng.client.render.crafting.UnitBakedModel;
import lu.kolja.expandedae.Expandedae;
import lu.kolja.expandedae.enums.ExpTiers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public class ExpCraftingUnitModelProvider extends AbstractCraftingUnitModelProvider<ExpTiers> {
    public static final List<Material> MATERIALS = new ArrayList<>();
    public static final ChunkRenderTypeSet CUTOUT = ChunkRenderTypeSet.of(RenderType.cutout());

    protected static final Material RING_CORNER = texture("ring_corner");
    protected static final Material RING_SIDE_HOR = texture("ring_side_hor");
    protected static final Material RING_SIDE_VER = texture("ring_side_ver");
    protected static final Material LIGHT_BASE = texture("light_base");
    protected static final Material UNIT_BASE = texture("unit_base");
    protected static final Material CPU_2_LIGHT = texture("exp_crafting_accelerator_2_light");
    protected static final Material CPU_4_LIGHT = texture("exp_crafting_accelerator_4_light");
    protected static final Material CPU_8_LIGHT = texture("exp_crafting_accelerator_8_light");
    protected static final Material CPU_16_LIGHT = texture("exp_crafting_accelerator_16_light");
    protected static final Material CPU_32_LIGHT = texture("exp_crafting_accelerator_32_light");
    protected static final Material CPU_64_LIGHT = texture("exp_crafting_accelerator_64_light");
    protected static final Material CPU_128_LIGHT = texture("exp_crafting_accelerator_128_light");
    protected static final Material CPU_256_LIGHT = texture("exp_crafting_accelerator_256_light");
    protected static final Material CPU_512_LIGHT = texture("exp_crafting_accelerator_512_light");
    protected static final Material CPU_1K_LIGHT = texture("exp_crafting_accelerator_1k_light");
    protected static final Material CPU_2K_LIGHT = texture("exp_crafting_accelerator_2k_light");
    protected static final Material CPU_4K_LIGHT = texture("exp_crafting_accelerator_4k_light");
    protected static final Material CPU_8K_LIGHT = texture("exp_crafting_accelerator_8k_light");
    protected static final Material CPU_16K_LIGHT = texture("exp_crafting_accelerator_16k_light");
    protected static final Material CPU_32K_LIGHT = texture("exp_crafting_accelerator_32k_light");
    protected static final Material CPU_64K_LIGHT = texture("exp_crafting_accelerator_64k_light");
    protected static final Material CPU_128K_LIGHT = texture("exp_crafting_accelerator_128k_light");
    protected static final Material CPU_256K_LIGHT = texture("exp_crafting_accelerator_256k_light");
    protected static final Material CPU_512K_LIGHT = texture("exp_crafting_accelerator_512k_light");
    protected static final Material CPU_1M_LIGHT = texture("exp_crafting_accelerator_1m_light");

    public ExpCraftingUnitModelProvider(ExpTiers cpu) {
        super(cpu);
    }

    @Override
    public List<Material> getMaterials() {
        return Collections.unmodifiableList(MATERIALS);
    }

    public TextureAtlasSprite getLightMaterial(Function<Material, TextureAtlasSprite> textureGetter) {
        return switch (type) {
            case TIER_2 -> textureGetter.apply(CPU_2_LIGHT);
            case TIER_4 -> textureGetter.apply(CPU_4_LIGHT);
            case TIER_8 -> textureGetter.apply(CPU_8_LIGHT);
            case TIER_16 -> textureGetter.apply(CPU_16_LIGHT);
            case TIER_32 -> textureGetter.apply(CPU_32_LIGHT);
            case TIER_64 -> textureGetter.apply(CPU_64_LIGHT);
            case TIER_128 -> textureGetter.apply(CPU_128_LIGHT);
            case TIER_256 -> textureGetter.apply(CPU_256_LIGHT);
            case TIER_512 -> textureGetter.apply(CPU_512_LIGHT);
            case TIER_1K -> textureGetter.apply(CPU_1K_LIGHT);
            case TIER_2K -> textureGetter.apply(CPU_2K_LIGHT);
            case TIER_4K -> textureGetter.apply(CPU_4K_LIGHT);
            case TIER_8K -> textureGetter.apply(CPU_8K_LIGHT);
            case TIER_16K -> textureGetter.apply(CPU_16K_LIGHT);
            case TIER_32K -> textureGetter.apply(CPU_32K_LIGHT);
            case TIER_64K -> textureGetter.apply(CPU_64K_LIGHT);
            case TIER_128K -> textureGetter.apply(CPU_128K_LIGHT);
            case TIER_256K -> textureGetter.apply(CPU_256K_LIGHT);
            case TIER_512K -> textureGetter.apply(CPU_512K_LIGHT);
            case TIER_1M -> textureGetter.apply(CPU_1M_LIGHT);
            default -> throw new IllegalArgumentException(
                    "Crafting unit type " + type + " does not use a light texture.");
        };
    }

    @Override
    public BakedModel getBakedModel(Function<Material, TextureAtlasSprite> function) {
        var ringCorner = function.apply(RING_CORNER);
        var ringSideHor = function.apply(RING_SIDE_HOR);
        var ringSideVer = function.apply(RING_SIDE_VER);

        return switch (type) {
            case UNIT -> new UnitBakedModel(ringCorner, ringSideHor, ringSideVer, function.apply(UNIT_BASE)) {
                @Override
                public ChunkRenderTypeSet getRenderTypes(BlockState state, RandomSource rand, ModelData data) {
                    return CUTOUT;
                }
            };
            default ->
                    new LightBakedModel(ringCorner, ringSideHor, ringSideVer, function.apply(LIGHT_BASE), this.getLightMaterial(function)) {
                        @Override
                        @NotNull
                        public ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand, @NotNull ModelData data) {
                            return CUTOUT;
                        }
                    };
        };
    }

    private static Material texture(String name) {
        var material = new Material(InventoryMenu.BLOCK_ATLAS, Expandedae.makeId("block/crafting/" + name));
        MATERIALS.add(material);
        return material;
    }
}
