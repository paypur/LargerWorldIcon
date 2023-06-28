package me.paypur.largerworldicon.mixin;

import net.minecraft.network.FriendlyByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import static me.paypur.largerworldicon.LargerWorldIcon.NEW_STRING_LIMIT;

@Mixin(FriendlyByteBuf.class)
public abstract class FriendlyByteBufMixin {
    @ModifyConstant(
            method = "Lnet/minecraft/network/FriendlyByteBuf;readUtf()Ljava/lang/String;",
            constant = @Constant(intValue = 32767)
    )
    private int override1(int constant) {
        return NEW_STRING_LIMIT;
    }
    @ModifyConstant(
        method = "Lnet/minecraft/network/FriendlyByteBuf;writeUtf(Ljava/lang/String;)Lnet/minecraft/network/FriendlyByteBuf;",
        constant = @Constant(intValue = 32767)
    )
    private int override2(int constant) {
        return NEW_STRING_LIMIT;
    }
    @ModifyConstant(
            method = "Lnet/minecraft/network/FriendlyByteBuf;readResourceLocation()Lnet/minecraft/resources/ResourceLocation;",
            constant = @Constant(intValue = 32767)
    )
    private int override3(int constant) {
        return NEW_STRING_LIMIT;
    }
}
