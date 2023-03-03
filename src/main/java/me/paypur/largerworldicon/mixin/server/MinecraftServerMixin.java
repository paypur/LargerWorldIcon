package me.paypur.largerworldicon.mixin.server;

import net.minecraft.network.protocol.status.ServerStatus;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Optional;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {

    @Final
    @Shadow
    private static Logger LOGGER;
    @Final
    @Shadow
    protected LevelStorageSource.LevelStorageAccess storageSource;

    /**
     * @author paypur
     * @reason
     */

    @Overwrite
    private void updateStatusIcon(ServerStatus p_129879_) {
        Optional<File> optional = Optional.of(((MinecraftServer) (Object) this).getFile("server-icon.png")).filter(File::isFile);
        if (!optional.isPresent()) {
            optional = this.storageSource.getIconFile().map(Path::toFile).filter(File::isFile);
        }

        optional.ifPresent((p_202470_) -> {
            try {
                BufferedImage bufferedimage = ImageIO.read(p_202470_);
                Validate.validState(bufferedimage.getWidth() == bufferedimage.getHeight(), "Must be a square image");
                ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
                ImageIO.write(bufferedimage, "PNG", bytearrayoutputstream);
                byte[] abyte = Base64.getEncoder().encode(bytearrayoutputstream.toByteArray());
                p_129879_.setFavicon("data:image/png;base64," + new String(abyte, StandardCharsets.UTF_8));
            } catch (Exception exception) {
                LOGGER.error("Couldn't load server icon", exception);
            }

        });
    }
}
