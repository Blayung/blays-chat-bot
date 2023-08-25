package com.blay.chatbot.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;

import com.blay.chatbot.command.LastSeenCommand;

@Mixin(MinecraftClient.class)
public class OnDisconnectMixin {
	@Inject(at = @At("HEAD"), method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;)V")
	private void disconnect(Screen screen, CallbackInfo ci) {
        ClientPlayNetworkHandler networkHandler = MinecraftClient.getInstance().getNetworkHandler();
        if (networkHandler != null) { // I dunno why is this function called when joining a server
            for (PlayerListEntry player : networkHandler.getPlayerList()) {
                LastSeenCommand.updateLastSeen(player.getProfile().getName());
            }
        }
	}
}
