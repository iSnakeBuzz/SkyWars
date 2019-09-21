package com.isnakebuzz.skywars.Utils.Comparators;

import com.isnakebuzz.skywars.Teams.Team;

import java.util.Comparator;

public class TeamComparator implements Comparator<Team> {
    @Override
    public int compare(Team t1, Team t2) {
        return t1.getID() - t2.getID();
    }
}
