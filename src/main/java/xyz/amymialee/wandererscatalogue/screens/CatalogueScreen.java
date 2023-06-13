package xyz.amymialee.wandererscatalogue.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.TradeOffer;
import xyz.amymialee.wandererscatalogue.WanderersCatalogue;

import java.util.List;

public class CatalogueScreen extends HandledScreen<CatalogueScreenHandler> {
    private static final Identifier TEXTURE = WanderersCatalogue.id("textures/gui/catalogue.png");
    private float scrollAmount;
    private boolean mouseClicked;
    private int scrollOffset;

    public CatalogueScreen(CatalogueScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        --this.titleY;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        this.renderBackground(context);
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int i = this.x;
        int j = this.y;
        context.drawTexture(TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
        int k = (int)(40.0F * this.scrollAmount);
        context.drawTexture(TEXTURE, i + 156, j + 15 + k, 176 + (this.shouldScroll() ? 0 : 12), 0, 12, 15);
        int l = this.x + 52;
        int m = this.y + 14;
        int n = this.scrollOffset + 12;
        this.renderRecipeBackground(context, mouseX, mouseY, l, m, n);
        this.renderRecipeIcons(context, l, m, n);
    }

    @Override
    protected void drawMouseoverTooltip(DrawContext context, int x, int y) {
        super.drawMouseoverTooltip(context, x, y);
        int i = this.x + 52;
        int j = this.y + 14;
        int k = this.scrollOffset + 12;
        List<TradeOffer> list = this.handler.getAvailableOffers();
        for(int l = this.scrollOffset; l < k && l < this.handler.getAvailableOfferCount(); ++l) {
            int m = l - this.scrollOffset;
            int n = i + (m % 4 - 1) * 36 - 8;
            int o = j + m / 4 * 18 + 2;
            if (x >= n && x < n + 36 && y >= o - 1 && y < o + 17) {
                context.drawItemTooltip(this.textRenderer, list.get(l).getSellItem(), x, y);
            }
        }
    }

    private void renderRecipeBackground(DrawContext context, int mouseX, int mouseY, int x, int y, int scrollOffset) {
        for(int i = this.scrollOffset; i < scrollOffset && i < this.handler.getAvailableOfferCount(); ++i) {
            int j = i - this.scrollOffset;
            int k = x + (j % 4 - 1) * 36 - 8;
            int l = j / 4;
            int m = y + l * 18 + 2;
            int n = this.backgroundHeight;
            if (i == this.handler.getSelectedRecipe()) {
                n += 18;
            } else if (mouseX >= k && mouseY >= m - 1 && mouseX < k + 36 && mouseY < m + 17) {
                n += 36;
            }
            context.drawTexture(TEXTURE, k, m - 1, 0, n, 36, 18);
        }
    }

    private void renderRecipeIcons(DrawContext context, int x, int y, int scrollOffset) {
        List<TradeOffer> list = this.handler.getAvailableOffers();
        for(int i = this.scrollOffset; i < scrollOffset && i < this.handler.getAvailableOfferCount(); ++i) {
            int j = i - this.scrollOffset;
            int k = x + (j % 4 - 1) * 36 - 8;
            int l = j / 4;
            int m = y + l * 18 + 2;
            if (this.client != null) {
                ItemStack buyItem = list.get(i).getOriginalFirstBuyItem();
                context.drawItem(buyItem, k, m);
                context.drawItemInSlot(this.textRenderer, buyItem, k, m - 1);
                ItemStack sellItem = list.get(i).getSellItem();
                context.drawItem(sellItem, k + 18, m);
                context.drawItemInSlot(this.textRenderer, sellItem, k + 18, m - 1);
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.mouseClicked = false;
        int i = this.x + 52;
        int j = this.y + 14;
        int k = this.scrollOffset + 12;
        for(int l = this.scrollOffset; l < k; ++l) {
            int m = l - this.scrollOffset;
            double d = mouseX - (double)(i + (m % 4 - 1) * 36 - 8);
            double e = mouseY - (double)(j + m / 4 * 18) - 1;
            if (this.client != null && d >= 0.0 && e >= 0.0 && d < 36.0 && e < 18.0 && this.handler.onButtonClick(this.client.player, l)) {
                MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
                if (this.client.interactionManager != null) {
                    this.client.interactionManager.clickButton(this.handler.syncId, l);
                }
                return true;
            }
        }
        i = this.x + 156;
        j = this.y + 9;
        if (mouseX >= (double)i && mouseX < (double)(i + 12) && mouseY >= (double)j && mouseY < (double)(j + 54)) {
            this.mouseClicked = true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.mouseClicked && this.shouldScroll()) {
            int i = this.y + 14;
            int j = i + 54;
            this.scrollAmount = ((float)mouseY - (float)i - 7.5F) / ((float)(j - i) - 15.0F);
            this.scrollAmount = MathHelper.clamp(this.scrollAmount, 0.0F, 1.0F);
            this.scrollOffset = (int)((double)(this.scrollAmount * (float)this.getMaxScroll()) + 0.5) * 4;
            return true;
        } else {
            return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (this.shouldScroll()) {
            int i = this.getMaxScroll();
            float f = (float)amount / (float)i;
            this.scrollAmount = MathHelper.clamp(this.scrollAmount - f, 0.0F, 1.0F);
            this.scrollOffset = (int)((double)(this.scrollAmount * (float)i) + 0.5) * 4;
        }
        return true;
    }

    private boolean shouldScroll() {
        return this.handler.getAvailableOfferCount() > 12;
    }

    protected int getMaxScroll() {
        return (this.handler.getAvailableOfferCount() + 4 - 1) / 4 - 3;
    }
}