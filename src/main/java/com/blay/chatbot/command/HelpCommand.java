package com.blay.chatbot.command;

import net.minecraft.client.MinecraftClient;

public class HelpCommand {
    public static void execute() {
        MinecraftClient.getInstance().getNetworkHandler().sendChatMessage(">> Blay's Chat Bot command list -> :help :github :hello :gayrate :lastseen :ping");
    }
}
