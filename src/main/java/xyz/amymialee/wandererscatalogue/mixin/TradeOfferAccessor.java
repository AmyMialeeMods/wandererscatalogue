package xyz.amymialee.wandererscatalogue.mixin;

import net.minecraft.village.TradeOffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TradeOffer.class)
public interface TradeOfferAccessor {
    @Accessor("maxUses")
    void setMaxUses(int uses);
}