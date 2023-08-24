package com.blay.chatbot;

import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import net.minecraft.text.Text;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.MinecraftClient;

import java.util.regex.Pattern;
import java.util.Random;
import java.util.Arrays;
import java.io.UnsupportedEncodingException;

public class ChatBot implements ClientModInitializer {
    private static final Pattern commandMessagePattern = Pattern.compile("<[a-zA-Z0-9_]{2,16}> :.+?");
    private Random random = new Random();
    private int commandTipCounter = 2400;

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
        return Math.abs(result % 21) * 5;
    }

    public static void processChatMessage(Text message) {
        String messageAsString = message.getString();
        if (commandMessagePattern.matcher(messageAsString).matches()) {
            String[] splittedMessage = messageAsString.split(" ");

            String commandName = splittedMessage[1].substring(1);
            String commandExecutor = splittedMessage[0].substring(1, splittedMessage[0].length() - 1);
            String[] commandArgs = Arrays.copyOfRange(splittedMessage, 2, splittedMessage.length);

            ClientPlayNetworkHandler networkHandler = MinecraftClient.getInstance().getNetworkHandler();

            if (commandName.equals("hello")) {
                networkHandler.sendChatMessage(">> Hello, " + commandExecutor + "!");
            } else if (commandName.equals("github")) {
                networkHandler.sendChatMessage(">> Blay's Chat Bot's github repo: https://github.com/Blayung/blays-chat-bot");
                networkHandler.sendChatMessage(">> Author's github profile (you can also find my other social media accounts in here): https://github.com/Blayung");
            } else if (commandName.equals("gayrate") || commandName.equals("gr")) {
                if (commandArgs.length == 0) {
                    networkHandler.sendChatMessage(">> " + commandExecutor + "'s gay rate: " + getGayRate(commandExecutor));
                } else {
                    networkHandler.sendChatMessage(">> " + commandArgs[0] + "'s gay rate: " + getGayRate(commandArgs[0]));
                }
            } else if (commandName.equals("help") || commandName.equals("info")) {
                networkHandler.sendChatMessage(">> Blay's Chat Bot help:");
                networkHandler.sendChatMessage(">> :help - Displays this menu.");
                networkHandler.sendChatMessage(">> :github - The github repository of that chat bot.");
                networkHandler.sendChatMessage(">> :hello - Say hello to me!");
                networkHandler.sendChatMessage(">> :gayrate <nick> - Check somebody's gay rate.");
                networkHandler.sendChatMessage(">> Also, if this bot is being run on the og author's account (_Blay_), then he probably leaves often (unlike e.g. moooomoooo that's always online), so some data may be (or rather will always be) incomplete.");
            } else {
                networkHandler.sendChatMessage(">> Unknown command \":" + commandName + "\"");
            }
        }
    }
    
    @Override
    public void onInitializeClient() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            ClientPlayNetworkHandler networkHandler = client.getNetworkHandler();
            if (networkHandler != null) {
                if (commandTipCounter == 3000) {
                    commandTipCounter = 0;

                    int choosenCommandTip = random.nextInt(3);
                    if (choosenCommandTip == 0) {
                        networkHandler.sendChatMessage(">> Command tip: type :hello to say hello to me!");
                    } else if (choosenCommandTip == 1) {
                        networkHandler.sendChatMessage(">> Command tip: type :gayrate to check somebody's gay rate.");
                    } else if (choosenCommandTip == 2) {
                        networkHandler.sendChatMessage(">> Command tip: type :help to list all the available commands.");
                    }
                }
                commandTipCounter++;
            }
        });
    }
}
