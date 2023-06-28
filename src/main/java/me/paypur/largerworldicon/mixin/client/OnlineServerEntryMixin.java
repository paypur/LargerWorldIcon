package me.paypur.largerworldicon.mixin.client;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.Validate;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
@Mixin(ServerSelectionList.OnlineServerEntry.class)
public abstract class OnlineServerEntryMixin {

    @Final
    @Shadow
    private Minecraft minecraft;
    @Final
    @Shadow
    private ResourceLocation iconLocation;
    @Shadow
    private DynamicTexture icon;
    @Shadow
    private ServerData serverData;

    /**
     * @author paypur
     * @reason
     */

    @Overwrite
    private boolean uploadServerIcon(@Nullable String p_99897_) {
        if (p_99897_ == null) {
            this.minecraft.getTextureManager().release(this.iconLocation);
            if (this.icon != null && this.icon.getPixels() != null) {
                this.icon.getPixels().close();
            }
            this.icon = null;
        } else {
            try {
                NativeImage nativeimage = NativeImage.fromBase64(p_99897_);
                Validate.validState(nativeimage.getWidth() == nativeimage.getHeight(), "Must be a square image");
                if (this.icon == null) {
                    this.icon = new DynamicTexture(nativeimage);
                } else {
                    this.icon.setPixels(nativeimage);
                    this.icon.upload();
                }
                this.minecraft.getTextureManager().register(this.iconLocation, this.icon);
            } catch (Throwable throwable) {
                ServerSelectionList.LOGGER.error("Invalid icon for server {} ({})", this.serverData.name, this.serverData.ip, throwable);
                return false;
            }
        }
        return true;
    }
}