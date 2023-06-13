package xyz.amymialee.wandererscatalogue;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.impl.itemgroup.ItemGroupEventsImpl;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import xyz.amymialee.wandererscatalogue.cca.CustomerComponent;
import xyz.amymialee.wandererscatalogue.cca.SellerComponent;
import xyz.amymialee.wandererscatalogue.mixin.TradeOfferAccessor;
import xyz.amymialee.wandererscatalogue.screens.CatalogueScreenHandler;

import java.util.ArrayList;
import java.util.List;

public class WanderersCatalogue implements ModInitializer, EntityComponentInitializer {
    public static final String MOD_ID = "wandererscatalogue";
    private static List<TradeOffer> availableOffers;

    public static final ScreenHandlerType<CatalogueScreenHandler> CATALOGUE_SCREEN_HANDLER = Registry.register(Registries.SCREEN_HANDLER, "catalogue", new ScreenHandlerType<>(CatalogueScreenHandler::new, FeatureFlags.VANILLA_FEATURES));
    public static final Item WANDERERS_CATALOGUE = Registry.register(Registries.ITEM, id("catalogue"), new CatalogueItem(new FabricItemSettings().maxCount(1).rarity(Rarity.RARE)));

    public static final ComponentKey<CustomerComponent> CUSTOMER = ComponentRegistry.getOrCreate(id("customer"), CustomerComponent.class);
    public static final ComponentKey<SellerComponent> SELLER = ComponentRegistry.getOrCreate(id("seller"), SellerComponent.class);

    @Override
    public void onInitialize() {
        ItemGroupEventsImpl.getOrCreateModifyEntriesEvent(ItemGroups.TOOLS).register((entries) -> entries.add(WANDERERS_CATALOGUE));
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.beginRegistration(PlayerEntity.class, CUSTOMER).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(player -> new CustomerComponent());
        registry.beginRegistration(WanderingTraderEntity.class, SELLER).end(wanderingTrader -> new SellerComponent());
    }

    public static List<TradeOffer> getAvailableOffers(Entity entity) {
        if (availableOffers != null) {
            return availableOffers;
        }
        fillRecipes(entity);
        return availableOffers;
    }

    protected static void fillRecipes(Entity entity) {
        ArrayList<TradeOffer> offers = new ArrayList<>();
        Random random = Random.create();
        TradeOffers.Factory[] factory = TradeOffers.WANDERING_TRADER_TRADES.get(1);
        if (factory != null) {
            for (TradeOffers.Factory fact : factory) {
                TradeOffer tradeOffer = fact.create(entity, random);
                if (tradeOffer != null) {
                    ((TradeOfferAccessor) tradeOffer).setMaxUses(tradeOffer.getMaxUses() * 4);
                    offers.add(tradeOffer);
                }
            }
        }
        TradeOffers.Factory[] factory2 = TradeOffers.WANDERING_TRADER_TRADES.get(2);
        if (factory2 != null) {
            for (TradeOffers.Factory fact : factory2) {
                TradeOffer tradeOffer = fact.create(entity, random);
                if (tradeOffer != null) {
                    ((TradeOfferAccessor) tradeOffer).setMaxUses(tradeOffer.getMaxUses() * 4);
                    offers.add(tradeOffer);
                }
            }
        }
        availableOffers = offers;
    }

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }
}