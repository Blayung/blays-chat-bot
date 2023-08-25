package com.blay.chatbot.command;

import net.minecraft.client.MinecraftClient;

public class GithubCommand {
    public static void execute() {
        MinecraftClient.getInstance().getNetworkHandler().sendChatMessage(">> Blay's Chat Bot's github repo: https://github.com/Blayung/blays-chat-bot");
    }
}
