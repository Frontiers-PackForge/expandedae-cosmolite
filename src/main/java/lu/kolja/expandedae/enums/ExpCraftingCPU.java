package lu.kolja.expandedae.enums;

import appeng.block.crafting.ICraftingUnitType;
import appeng.core.definitions.BlockDefinition;
import lombok.Getter;
import lu.kolja.expandedae.ExpConfig;
import lu.kolja.expandedae.definition.ExpBlocks;
import net.minecraft.world.item.Item;

import java.io.Serializable;

public enum ExpCraftingCPU implements ICraftingUnitType, Serializable {
    THREADS_2(2, "exp_crafting_accelerator_2"),
    THREADS_4(4, "exp_crafting_accelerator_4"),
    THREADS_8(8, "exp_crafting_accelerator_8"),
    THREADS_16(16, "exp_crafting_accelerator_16"),
    THREADS_32(32, "exp_crafting_accelerator_32"),
    THREADS_64(64, "exp_crafting_accelerator_64"),
    THREADS_128(128, "exp_crafting_accelerator_128"),
    THREADS_256(256, "exp_crafting_accelerator_256"),
    THREADS_512(512, "exp_crafting_accelerator_512"),
    THREADS_1K(1024, "exp_crafting_accelerator_1k"),
    THREADS_2K(2048, "exp_crafting_accelerator_2k"),
    THREADS_4K(4096, "exp_crafting_accelerator_4k"),
    THREADS_8K(8192, "exp_crafting_accelerator_8k"),
    THREADS_16K(16384, "exp_crafting_accelerator_16k"),
    THREADS_32K(32768, "exp_crafting_accelerator_32k"),
    THREADS_64K(65536, "exp_crafting_accelerator_64k"),
    THREADS_128K(131072, "exp_crafting_accelerator_128k"),
    THREADS_256K(262144, "exp_crafting_accelerator_256k"),
    THREADS_512K(524288, "exp_crafting_accelerator_512k"),
    THREADS_1M(1048576, "exp_crafting_accelerator_1m");

    private final int threads;

    @Getter
    private final String affix;
    ExpCraftingCPU(int threads, String affix) {
        this.threads = threads;
        this.affix = affix;
    }

    public boolean isEnabled() {
        return ExpConfig.maxThreads >= threads;
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
            case THREADS_2 -> ExpBlocks.CPU_2;
            case THREADS_4 -> ExpBlocks.CPU_4;
            case THREADS_8 -> ExpBlocks.CPU_8;
            case THREADS_16 -> ExpBlocks.CPU_16;
            case THREADS_32 -> ExpBlocks.CPU_32;
            case THREADS_64 -> ExpBlocks.CPU_64;
            case THREADS_128 -> ExpBlocks.CPU_128;
            case THREADS_256 -> ExpBlocks.CPU_256;
            case THREADS_512 -> ExpBlocks.CPU_512;
            case THREADS_1K -> ExpBlocks.CPU_1K;
            case THREADS_2K -> ExpBlocks.CPU_2K;
            case THREADS_4K -> ExpBlocks.CPU_4K;
            case THREADS_8K -> ExpBlocks.CPU_8K;
            case THREADS_16K -> ExpBlocks.CPU_16K;
            case THREADS_32K -> ExpBlocks.CPU_32K;
            case THREADS_64K -> ExpBlocks.CPU_64K;
            case THREADS_128K -> ExpBlocks.CPU_128K;
            case THREADS_256K -> ExpBlocks.CPU_256K;
            case THREADS_512K -> ExpBlocks.CPU_512K;
            case THREADS_1M -> ExpBlocks.CPU_1M;
        };
    }

    @Override
    public Item getItemFromType() {
        return getDefinition().asItem();
    }
}
