package com.blay.chatbot.command;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.MinecraftClient;

import java.io.UnsupportedEncodingException;

public class GayRateCommand {
    private static int getGayRate(String nick) {
        byte[] ascii;
        try {
            ascii = nick.getBytes("US-ASCII");
        } catch (UnsupportedEncodingException e) {
            return 100;
        }
        int result = 0;
        boolean operation = false;
        for (byte character : ascii) {
            if (operation) {
                result += (int) character;
            } else {
                result -= (int) character;
            }
            operation ^= true;
        }
        return Math.abs(result) % 21 * 5;
    }

    public static void execute(String commandExecutor, String[] commandArgs) {
        ClientPlayNetworkHandler networkHandler = MinecraftClient.getInstance().getNetworkHandler();

        if (commandArgs.length == 0) {
            networkHandler.sendChatMessage(">> " + commandExecutor + "'s gay rate: " + getGayRate(commandExecutor) + "%");
        } else {
            networkHandler.sendChatMessage(">> " + commandArgs[0] + "'s gay rate: " + getGayRate(commandArgs[0]) + "%");
        }
    }
}
