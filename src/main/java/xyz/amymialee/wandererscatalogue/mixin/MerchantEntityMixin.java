package xyz.amymialee.wandererscatalogue.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.village.TradeOfferList;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MerchantEntity.class)
public abstract class MerchantEntityMixin extends PassiveEntity {
    @Shadow public abstract TradeOfferList getOffers();

    protected MerchantEntityMixin(EntityType<? extends PassiveEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "setCustomer", at = @At("HEAD"))
    public void wanderersCatalogue$setCustomer(PlayerEntity customer, CallbackInfo ci) {}
}