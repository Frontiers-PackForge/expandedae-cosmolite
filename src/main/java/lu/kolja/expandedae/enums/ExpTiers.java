package lu.kolja.expandedae.enums;

import appeng.block.crafting.ICraftingUnitType;
import appeng.core.definitions.BlockDefinition;
import java.io.Serializable;
import lombok.Getter;
import lu.kolja.expandedae.definition.ExpBlocks;
import net.minecraft.world.item.Item;

public enum ExpTiers implements ICraftingUnitType, Serializable {
    UNIT(0, "exp_crafting_unit", false),
    TIER_2(2, "2", true),
    TIER_4(4, "4", true),
    TIER_8(8, "8", true),
    TIER_16(16, "16", true),
    TIER_32(32, "32", true),
    TIER_64(64, "64", true),
    TIER_128(128, "128", true),
    TIER_256(256, "256", true),
    TIER_512(512, "512", true),
    TIER_1K(1024, "1k", true),
    TIER_2K(2048, "2k", true),
    TIER_4K(4096, "4k", true),
    TIER_8K(8192, "8k", true),
    TIER_16K(16384, "16k", true),
    TIER_32K(32768, "32k", true),
    TIER_64K(65536, "64k", true),
    TIER_128K(131072, "128k", true),
    TIER_256K(262144, "256k", true),
    TIER_512K(524288, "512k", true),
    TIER_1M(1048576, "1m", true);


    @Getter
    private final int threads;
    @Getter
    private final String affix;
    @Getter
    private final String cpuAffix;
    @Getter
    private final boolean isCPU;

    ExpTiers(int threads, String affix, boolean isCPU) {
        this.threads = threads;
        this.affix = affix;
        this.cpuAffix = "exp_crafting_accelerator_" + affix;
        this.isCPU = isCPU;
    }

    @Override
    public long getStorageBytes() {
        return 0;
    }

    @Override
    public int getAcceleratorThreads() {
        return threads;
    }

    public BlockDefinition<?> getDefinition() {
        return switch (this) {
            case UNIT -> ExpBlocks.EXP_CRAFTING_UNIT;
            case TIER_2 -> ExpBlocks.CPU_2;
            case TIER_4 -> ExpBlocks.CPU_4;
            case TIER_8 -> ExpBlocks.CPU_8;
            case TIER_16 -> ExpBlocks.CPU_16;
            case TIER_32 -> ExpBlocks.CPU_32;
            case TIER_64 -> ExpBlocks.CPU_64;
            case TIER_128 -> ExpBlocks.CPU_128;
            case TIER_256 -> ExpBlocks.CPU_256;
            case TIER_512 -> ExpBlocks.CPU_512;
            case TIER_1K -> ExpBlocks.CPU_1K;
            case TIER_2K -> ExpBlocks.CPU_2K;
            case TIER_4K -> ExpBlocks.CPU_4K;
            case TIER_8K -> ExpBlocks.CPU_8K;
            case TIER_16K -> ExpBlocks.CPU_16K;
            case TIER_32K -> ExpBlocks.CPU_32K;
            case TIER_64K -> ExpBlocks.CPU_64K;
            case TIER_128K -> ExpBlocks.CPU_128K;
            case TIER_256K -> ExpBlocks.CPU_256K;
            case TIER_512K -> ExpBlocks.CPU_512K;
            case TIER_1M -> ExpBlocks.CPU_1M;
        };
    }

    @Override
    public Item getItemFromType() {
        return getDefinition().asItem();
    }
}
