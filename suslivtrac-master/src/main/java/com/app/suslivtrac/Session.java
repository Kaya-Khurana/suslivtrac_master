package com.app.suslivtrac;

public class Session {
    private static int currentUserId = -1;

    public static void setCurrentUserId(int userId) {
        currentUserId = userId;
    }

    public static int getCurrentUserId() {
        return currentUserId;
    }
}
