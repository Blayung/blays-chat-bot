package com.blay.chatbot.command;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.MinecraftClient;

import com.blay.chatbot.BlaysChatBot;

public class HelloCommand {
    public static void execute(String commandExecutor) {
        ClientPlayNetworkHandler networkHandler = MinecraftClient.getInstance().getNetworkHandler();

        if (BlaysChatBot.random.nextInt(2) == 0) {
            networkHandler.sendChatMessage(">> Hello, " + commandExecutor + "!");
        } else {
            networkHandler.sendChatMessage(">> Hi, " + commandExecutor + "!");
        }
    }
}
