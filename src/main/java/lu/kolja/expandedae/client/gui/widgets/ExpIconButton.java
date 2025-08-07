package lu.kolja.expandedae.client.gui.widgets;

import appeng.client.gui.Icon;
import appeng.client.gui.style.Blitter;
import appeng.client.gui.widgets.ITooltip;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ExpIconButton extends Button implements ITooltip {
    private boolean halfSize = false;
    private boolean disableClickSound = false;
    private boolean disableBackground = false;

    public ExpIconButton(Button.OnPress onPress) {
        super(0, 0, 16, 16, Component.empty(), onPress, Button.DEFAULT_NARRATION);
    }

    public void setVisibility(boolean vis) {
        this.visible = vis;
        this.active = vis;
    }

    public void playDownSound(@NotNull SoundManager soundHandler) {
        if (!this.disableClickSound) {
            super.playDownSound(soundHandler);
        }

    }

    public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partial) {
        if (this.visible) {
            ExpIcon icon = this.getIcon();
            Blitter blitter = icon.getBlitter();
            if (!this.active) {
                blitter.opacity(0.5F);
            }

            if (this.halfSize) {
                this.width = 8;
                this.height = 8;
            }

            RenderSystem.disableDepthTest();
            RenderSystem.enableBlend();
            if (this.isFocused()) {
                guiGraphics.fill(this.getX() - 1, this.getY() - 1, this.getX() + this.width + 1, this.getY(), -1);
                guiGraphics.fill(this.getX() - 1, this.getY(), this.getX(), this.getY() + this.height, -1);
                guiGraphics.fill(this.getX() + this.width, this.getY(), this.getX() + this.width + 1, this.getY() + this.height, -1);
                guiGraphics.fill(this.getX() - 1, this.getY() + this.height, this.getX() + this.width + 1, this.getY() + this.height + 1, -1);
            }

            if (this.halfSize) {
                PoseStack pose = guiGraphics.pose();
                pose.pushPose();
                pose.translate((float)this.getX(), (float)this.getY(), 0.0F);
                pose.scale(0.5F, 0.5F, 1.0F);
                if (!this.disableBackground) {
                    Icon.TOOLBAR_BUTTON_BACKGROUND.getBlitter().dest(0, 0).blit(guiGraphics);
                }

                blitter.dest(0, 0).blit(guiGraphics);
                pose.popPose();
            } else {
                if (!this.disableBackground) {
                    Icon.TOOLBAR_BUTTON_BACKGROUND.getBlitter().dest(this.getX(), this.getY()).blit(guiGraphics);
                }

                icon.getBlitter().dest(this.getX(), this.getY()).blit(guiGraphics);
            }

            RenderSystem.enableDepthTest();
            Item item = this.getItemOverlay();
            if (item != null) {
                guiGraphics.renderItem(new ItemStack(item), this.getX(), this.getY());
            }
        }

    }

    protected abstract ExpIcon getIcon();

    protected @Nullable Item getItemOverlay() {
        return null;
    }

    public List<Component> getTooltipMessage() {
        return Collections.singletonList(this.getMessage());
    }

    public Rect2i getTooltipArea() {
        return new Rect2i(this.getX(), this.getY(), this.halfSize ? 8 : 16, this.halfSize ? 8 : 16);
    }

    public boolean isTooltipAreaVisible() {
        return this.visible;
    }

    public boolean isHalfSize() {
        return this.halfSize;
    }

    public void setHalfSize(boolean halfSize) {
        this.halfSize = halfSize;
    }

    public boolean isDisableClickSound() {
        return this.disableClickSound;
    }

    public void setDisableClickSound(boolean disableClickSound) {
        this.disableClickSound = disableClickSound;
    }

    public boolean isDisableBackground() {
        return this.disableBackground;
    }

    public void setDisableBackground(boolean disableBackground) {
        this.disableBackground = disableBackground;
    }
}
