package lu.kolja.expandedae.item.part;

import lu.kolja.expandedae.part.FilterTerminalPart;
import appeng.items.parts.PartItem;

public class FilterTerminalPartItem extends PartItem<FilterTerminalPart> {
    public FilterTerminalPartItem(Properties properties) {
        super(properties, FilterTerminalPart.class, FilterTerminalPart::new);
    }
}
