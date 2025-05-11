package lu.kolja.expandedae.item.part;

import appeng.items.parts.PartItem;
import lu.kolja.expandedae.part.FilterTerminalPart;

public class FilterTerminalPartItem extends PartItem<FilterTerminalPart> {
    public FilterTerminalPartItem(Properties properties) {
        super(properties, FilterTerminalPart.class, FilterTerminalPart::new);
    }
}
