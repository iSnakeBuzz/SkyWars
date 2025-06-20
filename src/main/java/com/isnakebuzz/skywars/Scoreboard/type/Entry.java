package com.isnakebuzz.skywars.Scoreboard.type;

import com.isnakebuzz.skywars.Scoreboard.common.Strings;

public class Entry {

    private String name;
    private int position;

    public Entry(String name, int position) {
        this.name = Strings.format(name);
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

}
