package edu.wpi.cs3733.D21.teamB.util;

import edu.wpi.cs3733.D21.teamB.views.misc.ChatBoxController;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class PageCache {
    @Getter
    private static final List<ChatBoxController.Message> messages = new ArrayList<>();
}
