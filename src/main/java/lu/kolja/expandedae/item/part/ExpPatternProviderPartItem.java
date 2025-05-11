package lu.kolja.expandedae.item.part;

import appeng.items.parts.PartItem;
import lu.kolja.expandedae.part.ExpPatternProviderPart;

public class ExpPatternProviderPartItem extends PartItem<ExpPatternProviderPart> {
    public ExpPatternProviderPartItem(Properties properties) {
        super(properties, ExpPatternProviderPart.class, ExpPatternProviderPart::new);
    }
}
