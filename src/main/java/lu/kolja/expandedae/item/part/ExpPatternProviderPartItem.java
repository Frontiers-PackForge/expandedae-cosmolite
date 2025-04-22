package lu.kolja.expandedae.item.part;

import lu.kolja.expandedae.part.ExpPatternProviderPart;
import appeng.items.parts.PartItem;

public class ExpPatternProviderPartItem extends PartItem<ExpPatternProviderPart> {
    public ExpPatternProviderPartItem(Properties properties) {
        super(properties, ExpPatternProviderPart.class, ExpPatternProviderPart::new);
    }
}
