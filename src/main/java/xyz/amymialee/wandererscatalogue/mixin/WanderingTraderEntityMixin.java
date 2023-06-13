package xyz.amymialee.wandererscatalogue.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.village.TradeOffer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.amymialee.wandererscatalogue.WanderersCatalogue;
import xyz.amymialee.wandererscatalogue.cca.CustomerComponent;
import xyz.amymialee.wandererscatalogue.cca.SellerComponent;

@Mixin(WanderingTraderEntity.class)
public abstract class WanderingTraderEntityMixin extends MerchantEntityMixin {
    protected WanderingTraderEntityMixin(EntityType<? extends PassiveEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void wanderersCatalogue$setCustomer(PlayerEntity customer, CallbackInfo ci) {
        if (this.isAlive()) {
            SellerComponent seller = WanderersCatalogue.SELLER.get(this);
            if (!seller.isHasBeenTradedWith()) {
                CustomerComponent component = WanderersCatalogue.CUSTOMER.get(customer);
                if (component.getCurrentOrder() != -1) {
                    this.getOffers().add(0, WanderersCatalogue.getAvailableOffers(this).get(component.getCurrentOrder()));
                    this.getOffers().add(new TradeOffer(new ItemStack(Items.EMERALD, 8), new ItemStack(WanderersCatalogue.WANDERERS_CATALOGUE), 1, 6, 1));
                } else {
                    this.getOffers().add(0, new TradeOffer(new ItemStack(Items.EMERALD, 8), new ItemStack(WanderersCatalogue.WANDERERS_CATALOGUE), 1, 6, 1));
                }
                seller.setHasBeenTradedWith(true);
            }
        }
        super.wanderersCatalogue$setCustomer(customer, ci);
    }
}