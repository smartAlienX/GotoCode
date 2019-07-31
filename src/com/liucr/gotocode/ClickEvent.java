package com.liucr.gotocode;

import java.util.ArrayList;
import java.util.List;

public class ClickEvent {

    public final static String CLICK_EVENT = "ClickEvent";

    public List<StackTraceElement> stackTraceElements = new ArrayList<StackTraceElement>();

    public static ClickEvent parse(String s) {
        if (s == null
                && s.contains(CLICK_EVENT)) {
            int start = s.indexOf("[");
            int end = s.lastIndexOf("]");
            String infoString = s.substring(start, end);
            String[] infoList = infoString.split(",");
        }
    }


}
