package com.blay.chatbot.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.client.MinecraftClient;

import java.util.regex.Pattern;
import java.util.Arrays;

import com.blay.chatbot.command.*;

@Mixin(ChatHud.class)
public class OnChatMixin {
    private static final Pattern commandMessagePattern = Pattern.compile("^<[a-zA-Z0-9_]{2,16}> ;.+?");
    private static final Pattern leftServerMessagePattern = Pattern.compile("^[a-zA-Z0-9_]{2,16} left");

	@Inject(at = @At("HEAD"), method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;ILnet/minecraft/client/gui/hud/MessageIndicator;Z)V")
	private void addMessage(Text message, MessageSignatureData signature, int ticks, MessageIndicator indicator, boolean refresh, CallbackInfo ci) {
        String messageAsString = message.getString();

        if (leftServerMessagePattern.matcher(messageAsString).find()) {
            LastSeenCommand.updateLastSeen(messageAsString.split(" ")[0]);
        } else if (commandMessagePattern.matcher(messageAsString).matches()) {
            String[] splittedMessage = messageAsString.split(" ");

            String commandName = splittedMessage[1].substring(1);
            String commandExecutor = splittedMessage[0].substring(1, splittedMessage[0].length() - 1);
            String[] commandArgs = Arrays.copyOfRange(splittedMessage, 2, splittedMessage.length);

            if (commandName.equals("hello") || commandName.equals("hi")) {
                HelloCommand.execute(commandExecutor);
            } else if (commandName.equals("github") || commandName.equals("gh") || commandName.equals("author")) {
                GithubCommand.execute();
            } else if (commandName.equals("gayrate") || commandName.equals("gr")) {
                GayRateCommand.execute(commandExecutor, commandArgs);
            } else if (commandName.equals("lastseen") || commandName.equals("ls")) {
                LastSeenCommand.execute(commandExecutor, commandArgs);
            } else if (commandName.equals("help") || commandName.equals("info")) {
                HelpCommand.execute();
            } else if (commandName.equals("ping")) {
                PingCommand.execute(commandExecutor, commandArgs);
            } else if (commandName.equals("iq")) {
                IQCommand.execute(commandExecutor, commandArgs);
            } else {
                MinecraftClient.getInstance().getNetworkHandler().sendChatMessage(">> Unknown command \":" + commandName + "\"");
            }
        }
	}
}
