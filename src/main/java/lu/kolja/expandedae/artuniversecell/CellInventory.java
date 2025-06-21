package lu.kolja.expandedae.artuniversecell;

import appeng.api.config.Actionable;
import appeng.api.config.IncludeExclude;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.AEKeyType;
import appeng.api.stacks.KeyCounter;
import appeng.api.storage.cells.CellState;
import appeng.api.storage.cells.ISaveProvider;
import appeng.api.storage.cells.StorageCell;
import appeng.util.prioritylist.FuzzyPriorityList;
import appeng.util.prioritylist.IPartitionList;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import lombok.Getter;
import lu.kolja.expandedae.ExpConfig;
import lu.kolja.expandedae.helper.misc.NumberUtil;
import lu.kolja.expandedae.item.misc.ArtUniverseCellItem;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class CellInventory implements StorageCell {
    private final ArtUniverseCellItem cellType;
    private final ItemStack stack;
    private final ISaveProvider container;
    private final AEKeyType keyType;
    private Object2LongMap<AEKey> storedMap;
    private boolean isPersisted = true;
    private final int MAX_TYPES;
    private IPartitionList partitionList;
    @Getter
    private IncludeExclude partitionListMode;

    private CellInventory(AEKeyType keyType, ItemStack stack, ISaveProvider saveProvider, ArtUniverseCellItem cellType,int maxTypes) {
        this.stack = stack;
        container = saveProvider;
        this.keyType = keyType;
        storedMap = null;
        this.cellType = cellType;
        this.MAX_TYPES = maxTypes;
        initData();
    }

    private DataStore getCellStorage() {
        if (getUUID() == null) {
            return DataStore.INSTANCE;
        } else {
            return getStorageInstance().getOrCreateCell(getUUID());
        }
    }

    private void initData() {
        if (!hasUUID()) {
            getCellStoredMap();
        }
    }

    @Override
    public CellState getStatus() {
        if (getCellStoredMap().isEmpty()) {
            return CellState.EMPTY;
        }
        return CellState.NOT_EMPTY;
    }

    @Override
    public double getIdleDrain() {
        return ExpConfig.idleDrain;
    }

    @Override
    public Component getDescription() {
        return null;
    }

    public boolean hasUUID() {
        return stack.hasTag() && stack.getOrCreateTag().contains("uuid");
    }

    public UUID getUUID() {
        if (hasUUID()) return stack.getOrCreateTag().getUUID("uuid");
        else return null;
    }

    static CellInventory createInventory(ItemStack stack, ISaveProvider saveProvider, int maxTypes) {
        if (stack.getItem() instanceof ArtUniverseCellItem cellType) {
            return new CellInventory(cellType.getKeyType(), stack, saveProvider, cellType, maxTypes);
        }
        return null;
    }

    private static CellSavedData getStorageInstance() {
        return CellSavedData.INSTANCE;
    }

    private Object2LongMap<AEKey> getCellStoredMap() {
        if (storedMap == null) {
            storedMap = new Object2LongOpenHashMap<>();
            loadCellStoredMap();
        }
        return storedMap;
    }

    private void loadCellStoredMap() {
        boolean corruptedTag = false;
        if (!stack.hasTag()) return;
        long[] amounts = getCellStorage().amounts;
        for (int i = 0; i < amounts.length; i++) {
            long amount = amounts[i];
            AEKey key = AEKey.fromTagGeneric(getCellStorage().keys.getCompound(i));
            if (amount <= 0 || key == null) {
                corruptedTag = true;
            } else {
                getCellStoredMap().put(key, amount);
            }
        }
        if (corruptedTag) {
            saveChanges();
        }
    }

    private void saveChanges() {
        isPersisted = false;
        if (container != null) {
            container.saveChanges();
        } else {
            persist();
        }
    }

    @Override
    public void getAvailableStacks(KeyCounter out) {
        for (Object2LongMap.Entry<AEKey> entry : getCellStoredMap().object2LongEntrySet()) {
            out.add(entry.getKey(), entry.getLongValue());
        }
    }

    @Override
    public void persist() {
        if (isPersisted) return;
        if (getCellStoredMap().isEmpty()) {
            if (hasUUID()) {
                getStorageInstance().removeCell(getUUID());
                if (stack.getTag() != null) {
                    stack.getTag().remove("uuid");
                }
                initData();
            }
            return;
        }
        LongArrayList amounts = new LongArrayList(getCellStoredMap().size());
        ListTag keys = new ListTag();
        for (Object2LongMap.Entry<AEKey> entry : getCellStoredMap().object2LongEntrySet()) {
            long amount = entry.getLongValue();
            if (amount > 0) {
                keys.add(entry.getKey().toTagGeneric());
                amounts.add(amount);
            }
        }
        if (keys.isEmpty()) {
            getStorageInstance().updateCell(getUUID(), new DataStore());
        } else {
            getStorageInstance().modifyCell(getUUID(), keys, amounts.toArray(new long[0]));
        }
        isPersisted = true;
    }

    @Override
    public long insert(AEKey what, long amount, Actionable mode, IActionSource source) {
        if (amount == 0 || !keyType.contains(what)) return 0;
        if (what instanceof AEItemKey itemKey && itemKey.getItem() instanceof ArtUniverseCellItem) return 0;
        if (!hasUUID()) {
            stack.getOrCreateTag().putUUID("uuid", UUID.randomUUID());
            getStorageInstance().getOrCreateCell(getUUID());
            loadCellStoredMap();
        }
        long currentAmount = getCellStoredMap().getLong(what);
        if (mode == Actionable.MODULATE) {
            getCellStoredMap().put(what, currentAmount + amount);
            saveChanges();
        }
        return amount;
    }

    @Override
    public long extract(AEKey what, long amount, Actionable mode, IActionSource source) {
        long currentAmount = getCellStoredMap().getLong(what);
        if (currentAmount > 0) {
            if (amount >= currentAmount) {
                if (mode == Actionable.MODULATE) {
                    getCellStoredMap().remove(what, currentAmount);
                    saveChanges();
                }
                return currentAmount;
            } else {
                if (mode == Actionable.MODULATE) {
                    getCellStoredMap().put(what, currentAmount - amount);
                    saveChanges();
                }
                return amount;
            }
        }
        return 0;
    }

    public boolean isPreformatted() {
        return !partitionList.isEmpty();
    }

    public boolean isFuzzy() {
        return partitionList instanceof FuzzyPriorityList;
    }

    public String getTotalStorage() {
        double itemCount = 0;
        for (long storedAmount : getCellStoredMap().values()) {
            itemCount += storedAmount;
        }
        return NumberUtil.formatNum(itemCount);
    }

    public String getMaxTypes() {
        return String.valueOf(MAX_TYPES);
    }
}
