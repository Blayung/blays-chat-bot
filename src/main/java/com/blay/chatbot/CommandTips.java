package com.blay.chatbot;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import net.minecraft.client.network.ClientPlayNetworkHandler;

import com.blay.chatbot.BlaysChatBot;

public class CommandTips {
    private static int commandTipCounter = 2400;

    public static void start() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            ClientPlayNetworkHandler networkHandler = client.getNetworkHandler();
            if (networkHandler != null) {
                if (commandTipCounter == 3000) {
                    commandTipCounter = 0;

                    int choosenCommandTip = BlaysChatBot.random.nextInt(6);
                    if (choosenCommandTip == 0) {
                        networkHandler.sendChatMessage(">> Command tip: type ;hello to say hello to me!");
                    } else if (choosenCommandTip == 1) {
                        networkHandler.sendChatMessage(">> Command tip: type ;gayrate to check somebody's gay rate.");
                    } else if (choosenCommandTip == 2) {
                        networkHandler.sendChatMessage(">> Command tip: type ;lastseen to see when I've last seen somebody.");
                    } else if (choosenCommandTip == 3) {
                        networkHandler.sendChatMessage(">> Command tip: type ;ping to check somebody's ping.");
                    } else if (choosenCommandTip == 4) {
                        networkHandler.sendChatMessage(">> Command tip: type ;iq to check somebody's iq.");
                    } else {
                        networkHandler.sendChatMessage(">> Command tip: type ;help to list all the available commands.");
                    }
                }
                commandTipCounter++;
            }
        });
    }
}
