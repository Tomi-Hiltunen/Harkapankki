package com.example.myapplication;

public class State {
    private static int loggedInUserIndex;
    private static int curAccIndex;
    private static String errorMsg;

    public static int getLoggedInUserIndex() {
        return loggedInUserIndex;
    }

    public static void setLoggedInUserIndex(int loggedInUserIndex) {
        State.loggedInUserIndex = loggedInUserIndex;
    }

    public static int getCurAccIndex() {
        return curAccIndex;
    }

    public static void setCurAccIndex(int curAccIndex) {
        State.curAccIndex = curAccIndex;
    }

    public static String getErrorMsg() {
        return errorMsg;
    }

    public static void setErrorMsg(String errorMsg) {
        State.errorMsg = errorMsg;
    }
}
