package me.paypur.largerworldicon.mixin.client;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.worldselection.WorldSelectionList;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.LevelSummary;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.Validate;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@OnlyIn(Dist.CLIENT)
@Mixin(WorldSelectionList.WorldListEntry.class)
public abstract class WorldListEntryMixin extends ObjectSelectionList.Entry<WorldSelectionList.WorldListEntry> implements AutoCloseable {

    @Final
    @Shadow
    private Minecraft minecraft;
    @Final
    @Shadow
    LevelSummary summary;
    @Final
    @Shadow
    private ResourceLocation iconLocation;
    @Shadow
    private File iconFile;

    /**
     * @author paypur
     * @reason
     */

    @Nullable
    @Overwrite
    private DynamicTexture loadServerIcon() {
        boolean flag = this.iconFile != null && this.iconFile.isFile();
        if (flag) {
            try {
                InputStream inputstream = new FileInputStream(this.iconFile);
                DynamicTexture dynamictexture1;
                try {
                    NativeImage nativeimage = NativeImage.read(inputstream);
                    Validate.validState(nativeimage.getWidth() == nativeimage.getHeight(), "Must be a square image");
                    DynamicTexture dynamictexture = new DynamicTexture(nativeimage);
                    this.minecraft.getTextureManager().register(this.iconLocation, dynamictexture);
                    dynamictexture1 = dynamictexture;
                } catch (Throwable throwable1) {
                    try {
                        inputstream.close();
                    } catch (Throwable throwable) {
                        throwable1.addSuppressed(throwable);
                    }
                    throw throwable1;
                }
                inputstream.close();
                return dynamictexture1;
            } catch (Throwable throwable2) {
                //https://github.com/SpongePowered/Mixin/issues/594
                WorldSelectionList.LOGGER.error("Invalid icon for world {}", this.summary .getLevelId(), throwable2);
                this.iconFile = null;
                return null;
            }
        } else {
            this.minecraft.getTextureManager().release(this.iconLocation);
            return null;
        }
    }
}
