package com.blay.chatbot.command;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;

public class PingCommand {
    public static void execute(String commandExecutor, String[] commandArgs) {
        String subject;
        if (commandArgs.length == 0) {
            subject = commandExecutor;
        } else {
            subject = commandArgs[0];
        }

        ClientPlayNetworkHandler networkHandler = MinecraftClient.getInstance().getNetworkHandler();

        PlayerListEntry player = networkHandler.getPlayerListEntry(subject);
        if (player == null) {
            networkHandler.sendChatMessage(">> " + subject + "'s currently offline!");
        } else {
            networkHandler.sendChatMessage(">> " + subject + "'s ping: " + player.getLatency() + "ms");
        }
    }
}
