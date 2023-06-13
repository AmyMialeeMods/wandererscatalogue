package xyz.amymialee.wandererscatalogue.screens;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.village.TradeOffer;
import xyz.amymialee.wandererscatalogue.WanderersCatalogue;
import xyz.amymialee.wandererscatalogue.cca.CustomerComponent;

import java.util.List;

public class CatalogueScreenHandler extends ScreenHandler {
    private final PlayerEntity player;
    private final Property selectedRecipe;

    public CatalogueScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(WanderersCatalogue.CATALOGUE_SCREEN_HANDLER, syncId);
        this.player = playerInventory.player;
        this.selectedRecipe = Property.create();
        this.selectedRecipe.set(WanderersCatalogue.CUSTOMER.get(playerInventory.player).getCurrentOrder());
        for (int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
        this.addProperty(this.selectedRecipe);
    }

    public int getSelectedRecipe() {
        return this.selectedRecipe.get();
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        CustomerComponent component = WanderersCatalogue.CUSTOMER.get(player);
        if (id >= 0 && id < WanderersCatalogue.getAvailableOffers(this.player).size()) {
            this.selectedRecipe.set(id);
            component.setCurrentOrder(id);
        }
        return true;
    }

    public List<TradeOffer> getAvailableOffers() {
        return WanderersCatalogue.getAvailableOffers(this.player);
    }

    public int getAvailableOfferCount() {
        return WanderersCatalogue.getAvailableOffers(this.player).size();
    }

    @Override
    public ScreenHandlerType<?> getType() {
        return WanderersCatalogue.CATALOGUE_SCREEN_HANDLER;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ItemStack.EMPTY;
    }
}