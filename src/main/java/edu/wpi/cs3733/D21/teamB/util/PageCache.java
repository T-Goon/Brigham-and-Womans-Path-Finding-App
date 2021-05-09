package edu.wpi.cs3733.D21.teamB.util;

import edu.wpi.cs3733.D21.teamB.views.misc.ChatBoxController;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PageCache {

    @Getter
    @Setter
    private static boolean pageMinimized = false;

    @Getter
    private static final AtomicInteger newMessagesWaitingForBot = new AtomicInteger();

    @Getter
    private static final AtomicInteger newMessagesWaitingForUser = new AtomicInteger();

    @Getter
    private static final List<ChatBoxController.Message> userMessages = new ArrayList<>();

    @Getter
    private static final List<ChatBoxController.Message> botMessages = new ArrayList<>();


    public synchronized static ChatBoxController.Message getUserLastMessage() {
        newMessagesWaitingForBot.getAndDecrement();
        return userMessages.get(userMessages.size() - 1);
    }

    public synchronized static ChatBoxController.Message getBotLastMessage() {
        return botMessages.get(botMessages.size() - 1);
    }

    public synchronized static void addUserMessage(ChatBoxController.Message message) {
        newMessagesWaitingForBot.getAndIncrement();
        userMessages.add(message);
    }

    public synchronized static void addBotMessage(ChatBoxController.Message message) {
        newMessagesWaitingForUser.getAndIncrement();
        botMessages.add(message);
    }

    /**
     * @return a merged list of the user and bot messages
     */
    public static List<ChatBoxController.Message> getAllMessages() {
        int index = 0;
        int botIndex = 0;
        int userIndex = 0;
        List<ChatBoxController.Message> finalList = new ArrayList<>();
        while (index < userMessages.size() + botMessages.size()) {
            if (botIndex >= botMessages.size() || (userMessages.size() > userIndex && userMessages.get(userIndex).getIndex() < botMessages.get(botIndex).getIndex())) {
                finalList.add(userMessages.get(userIndex++));
            } else if (userIndex >= userMessages.size() || (botMessages.size() > botIndex && userMessages.get(userIndex).getIndex() > botMessages.get(botIndex).getIndex())) {
                finalList.add(botMessages.get(botIndex++));
            }
            index++;
        }
        return finalList;
    }
}
