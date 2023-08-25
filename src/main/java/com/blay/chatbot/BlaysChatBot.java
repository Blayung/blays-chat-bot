package com.blay.chatbot;

import net.fabricmc.api.ClientModInitializer;

import java.util.Random;

import com.blay.chatbot.CommandTips;

public class BlaysChatBot implements ClientModInitializer {
    public static final Random random = new Random();

    @Override
    public void onInitializeClient() {
        CommandTips.start();
    }
}
