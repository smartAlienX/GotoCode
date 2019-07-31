package com.liucr.gotocode;

import java.util.ArrayList;
import java.util.List;

public class ClickEvent {

    public final static String CLICK_EVENT = "ClickEvent";

    public List<StackTraceElement> stackTraceElements = new ArrayList<StackTraceElement>();

    public static ClickEvent parse(String s) {
        ClickEvent clickEvent = new ClickEvent();
        if (s != null
                && s.contains(CLICK_EVENT)) {
            int start = s.indexOf("[");
            int end = s.lastIndexOf("]");
            String infoString = s.substring(start, end);
            String[] infoList = infoString.split(",");
            for (String info : infoList) {
                StackTraceElement stackTraceElement = parseToStackTraceElement(info);
                if (stackTraceElement != null) {
                    clickEvent.stackTraceElements.add(stackTraceElement);
                }
            }
        }
        return clickEvent;
    }

    public static StackTraceElement parseToStackTraceElement(String s) {
        if (s != null) {
            s = s.trim();
            String[] strings = s.split("\\|");
            if (strings.length == 4) {
                return new StackTraceElement(
                        strings[0], strings[2], strings[1], Integer.parseInt(strings[3])
                );
            }
        }
        return null;
    }

}
