package lu.kolja.expandedae.xmod.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import lu.kolja.expandedae.xmod.emi.converters.EmiFluidStackConverter;
import lu.kolja.expandedae.xmod.emi.converters.EmiItemStackConverter;
import lu.kolja.expandedae.xmod.emi.converters.EmiStackConverters;

@EmiEntrypoint
public class ExpEMIAddon implements EmiPlugin {
    @Override
    public void register(EmiRegistry emiRegistry) {
        EmiStackConverters.register(new EmiItemStackConverter());
        EmiStackConverters.register(new EmiFluidStackConverter());

        emiRegistry.addGenericStackProvider(new EmiAeBaseScreenStackProvider());
    }
}
