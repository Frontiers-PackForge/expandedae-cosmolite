package lu.kolja.expandedae.cell.dual;

import appeng.api.stacks.AEKey;
import appeng.api.stacks.AEKeyType;

import java.util.Set;

public class AEKeyTypes<T extends AEKeyType> {
    public final Set<T> keyTypes;

    @SafeVarargs
    public AEKeyTypes(T... keyTypes) {
        this.keyTypes = Set.of(keyTypes);
    }

    public boolean contains(AEKey what) {
        for (var key : keyTypes) {
            if (key.contains(what)) return true;
        }
        return false;
    }

    public int maxTypes() {
        var max = keyTypes.stream().mapToInt(AEKeyType::getAmountPerByte).max();
        return max.isPresent() ? max.getAsInt() : AEKeyType.items().getAmountPerByte();
    }
}
