package me.paypur.largerworldicon.mixin;

import net.minecraft.network.protocol.status.ClientboundStatusResponsePacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import static me.paypur.largerworldicon.LargerWorldIcon.NEW_STRING_LIMIT;

@Mixin(ClientboundStatusResponsePacket.class)
abstract public class ClientboundStatusResponsePacketMixin {
    @ModifyConstant(
            method = "Lnet/minecraft/network/protocol/status/ClientboundStatusResponsePacket;<init>(Lnet/minecraft/network/FriendlyByteBuf;)V",
            constant = @Constant(intValue = 32767)
    )
    private int override1(int constant) {
        return NEW_STRING_LIMIT;
    }
}
