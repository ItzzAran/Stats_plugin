package me.itzzaran.stats;

import org.bukkit.OfflinePlayer;

public class Playerdata {

    public int getMob_kills() {
        return mob_kills;
    }

    public void setMob_kills(int mob_kills) {
        this.mob_kills = mob_kills;
    }

    public int getPlayer_kills() {
        return player_kills;
    }

    public void setPlayer_kills(int player_kills) {
        this.player_kills = player_kills;
    }

    public int getTimes_eaten() {
        return times_eaten;
    }

    public void setTimes_eaten(int times_eaten) {
        this.times_eaten = times_eaten;
    }

    public int getDistance_walked() {
        return distance_walked;
    }

    public void setDistance_walked(int distance_walked) {
        this.distance_walked = distance_walked;
    }

    public OfflinePlayer getPlayer() {
        return player;
    }

    public void setPlayer(OfflinePlayer player) {
        this.player = player;
    }

    OfflinePlayer player;
    int mob_kills;
    int player_kills;
    int times_eaten;
    int distance_walked;
}
