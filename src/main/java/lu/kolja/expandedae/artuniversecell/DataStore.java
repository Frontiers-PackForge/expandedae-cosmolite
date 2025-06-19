package lu.kolja.expandedae.artuniversecell;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

public class DataStore {
    public static final DataStore INSTANCE = new DataStore();

    public ListTag keys;
    public long[] amounts;

    public DataStore() {
        this(new ListTag(), new long[0]);
    }

    private DataStore(ListTag keys, long[] amounts) {
        this.keys = keys;
        this.amounts = amounts;
    }

    public CompoundTag toNbt() {
        CompoundTag nbt = new CompoundTag();
        nbt.put("keys", keys);
        nbt.putLongArray("amounts", amounts);
        return nbt;
    }

    public static DataStore fromNbt(CompoundTag nbt) {
        ListTag stackKeys = nbt.getList("keys", Tag.TAG_COMPOUND);
        long[] stackAmounts = nbt.getLongArray("amounts");
        return new DataStore(stackKeys, stackAmounts);
    }
}
