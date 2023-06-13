package xyz.amymialee.wandererscatalogue;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import xyz.amymialee.wandererscatalogue.screens.CatalogueScreen;

public class WanderersCatalogueClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(WanderersCatalogue.CATALOGUE_SCREEN_HANDLER, CatalogueScreen::new);
    }
}