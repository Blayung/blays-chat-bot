package com.blay.chatbot.command;

import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.MinecraftClient;

import com.mojang.logging.LogUtils;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

public class LastSeenCommand {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static void updateLastSeen(String nick) {
        try {
            Long timestamp = Instant.now().getEpochSecond();

            File lastSeenFile = FabricLoader.getInstance().getConfigDir().resolve("blays-chat-bot/last-seen.txt").toFile();

            if (!(lastSeenFile.exists() && lastSeenFile.isFile())) {
                lastSeenFile.delete();
                lastSeenFile.getParentFile().mkdirs();
                lastSeenFile.createNewFile();
            }

            FileReader fileReader = new FileReader(lastSeenFile);

            int i;
            String fileContent = "";
            while (true) {
                i = fileReader.read();
                if (i == -1) {
                    break;
                }
                fileContent += (char) i;
            }

            fileReader.close();

            Map<String,Long> lastSeenData = new HashMap<String,Long>();
            for (String line : fileContent.split("\n")) {
                String[] splittedLine = line.split(":");
                try {
                    lastSeenData.put(splittedLine[0], Long.parseLong(splittedLine[1]));
                } catch (ArrayIndexOutOfBoundsException e) {
                    break;
                }
            }

            lastSeenData.put(nick, timestamp);

            String toWrite = "";
            for (Map.Entry<String,Long> entry : lastSeenData.entrySet()) {
                toWrite += entry.getKey() + ":" + entry.getValue() + "\n";
            }
            toWrite.substring(0, toWrite.length() - 1);

            FileWriter fileWriter = new FileWriter(lastSeenFile);
            fileWriter.write(toWrite);
            fileWriter.close();
        } catch (IOException e) {
            LogUtils.getLogger().error("Failed to update the last seen file", e);
        }
    }
    
    private static Instant getLastSeen(String nick) {
        try {
            File lastSeenFile = FabricLoader.getInstance().getConfigDir().resolve("blays-chat-bot/last-seen.txt").toFile();

            if (!(lastSeenFile.exists() && lastSeenFile.isFile())) {
                lastSeenFile.delete();
                lastSeenFile.getParentFile().mkdirs();
                lastSeenFile.createNewFile();
            }

            FileReader fileReader = new FileReader(lastSeenFile);

            int i;
            String fileContent = "";
            while (true) {
                i = fileReader.read();
                if (i == -1) {
                    break;
                }
                fileContent += (char) i;
            }

            fileReader.close();

            for (String line : fileContent.split("\n")) {
                String[] splittedLine = line.split(":");
                if (splittedLine[0].equals(nick)) {
                    return Instant.ofEpochSecond(Long.parseLong(splittedLine[1]));
                }
            }
        } catch (IOException e) {
            LogUtils.getLogger().error("Failed to read from the last seen file", e);
        }

        return null;
    }

    public static void execute(String commandExecutor, String[] commandArgs) {
        String subject; 
        if (commandArgs.length == 0) {
            subject = commandExecutor;
        } else {
            subject = commandArgs[0];
        }

        ClientPlayNetworkHandler networkHandler = MinecraftClient.getInstance().getNetworkHandler();
        if (networkHandler.getPlayerListEntry(subject) == null) {
            Instant lastSeen = getLastSeen(subject);
            if (lastSeen == null) {
                networkHandler.sendChatMessage(">> I currently do not have any data records of " + subject + " ever being online.");
            } else {
                networkHandler.sendChatMessage(">> Last time I've seen " + subject + " online was on " + dateTimeFormatter.format(LocalDateTime.ofInstant(lastSeen, ZoneId.of("UTC"))) + " UTC.");
            }
        } else {
            networkHandler.sendChatMessage(">> " + subject + "'s currently online!");
        }
    }
}
