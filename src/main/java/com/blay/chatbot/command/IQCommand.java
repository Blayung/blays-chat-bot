package com.blay.chatbot.command;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.MinecraftClient;

import java.io.UnsupportedEncodingException;

public class IQCommand {
    private static int getIQ(String nick) {
        byte[] ascii;
        try {
            ascii = nick.getBytes("US-ASCII");
        } catch (UnsupportedEncodingException e) {
            return 100;
        }
        int result = 0;
        boolean operation = true;
        for (byte character : ascii) {
            if (operation) {
                result += (int) character;
            } else {
                result -= (int) character;
            }
            operation ^= true;
        }
        return Math.abs(result) % 81 + 65;
    }

    public static void execute(String commandExecutor, String[] commandArgs) {
        ClientPlayNetworkHandler networkHandler = MinecraftClient.getInstance().getNetworkHandler();

        if (commandArgs.length == 0) {
            networkHandler.sendChatMessage(">> " + commandExecutor + "'s iq: " + getIQ(commandExecutor));
        } else {
            networkHandler.sendChatMessage(">> " + commandArgs[0] + "'s iq: " + getIQ(commandArgs[0]));
        }
    }
}
