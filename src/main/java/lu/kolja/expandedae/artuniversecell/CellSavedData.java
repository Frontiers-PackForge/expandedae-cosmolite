package lu.kolja.expandedae.artuniversecell;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CellSavedData extends SavedData {
    public static CellSavedData INSTANCE = new CellSavedData();

    private final Map<UUID, DataStore> cells = new HashMap<>();

    public CellSavedData() {
        setDirty();
    }

    public CellSavedData(CompoundTag nbt) {
        ListTag cellList = nbt.getList("list", CompoundTag.TAG_COMPOUND);
        for (int i = 0; i < cellList.size(); i++) {
            CompoundTag cell = cellList.getCompound(i);
            cells.put(cell.getUUID("uuid"), DataStore.fromNbt(cell.getCompound("data")));
        }
        setDirty();
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag nbt) {
        ListTag cellList = new ListTag();
        for (Map.Entry<UUID, DataStore> entry : cells.entrySet()) {
            CompoundTag cell = new CompoundTag();
            cell.putUUID("uuid", entry.getKey());
            cell.put("data", entry.getValue().toNbt());
            cellList.add(cell);
        }
        nbt.put("list", cellList);
        return nbt;
    }

    public void updateCell(UUID uuid, DataStore infinityCellDataStorage) {
        cells.put(uuid, infinityCellDataStorage);
        setDirty();
    }

    public DataStore getOrCreateCell(UUID uuid) {
        if (!cells.containsKey(uuid)) {
            updateCell(uuid, new DataStore());
        }
        return cells.get(uuid);
    }

    public void modifyCell(UUID cellID, ListTag keys, long[] amounts) {
        DataStore cellToModify = getOrCreateCell(cellID);
        if (keys != null && amounts != null) {
            cellToModify.keys = keys;
            cellToModify.amounts = amounts;
        }
        updateCell(cellID, cellToModify);
    }

    public void removeCell(UUID uuid) {
        cells.remove(uuid);
        setDirty();
    }
}
