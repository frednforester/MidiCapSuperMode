package com.midicap.midicap;

public class GlobalSetup {
    private int ledbright;
    private int screenbright;
    private boolean darkFonts;
    private String wallpaper;
    private double longPressTiming;
    private boolean wireless24G;
    private int wirelessId;
    private int wirelessDb;

    public int getLedbright() { return ledbright; }
    public void setLedbright(int ledbright) { this.ledbright = ledbright; }

    public int getScreenbright() { return screenbright; }
    public void setScreenbright(int screenbright) { this.screenbright = screenbright; }

    public boolean isDarkFonts() { return darkFonts; }
    public void setDarkFonts(boolean darkFonts) { this.darkFonts = darkFonts; }

    public String getWallpaper() { return wallpaper; }
    public void setWallpaper(String wallpaper) { this.wallpaper = wallpaper; }

    public double getLongPressTiming() { return longPressTiming; }
    public void setLongPressTiming(double longPressTiming) { this.longPressTiming = longPressTiming; }

    public boolean isWireless24G() { return wireless24G; }
    public void setWireless24G(boolean wireless24g) { this.wireless24G = wireless24g; }

    public int getWirelessId() { return wirelessId; }
    public void setWirelessId(int wirelessId) { this.wirelessId = wirelessId; }

    public int getWirelessDb() { return wirelessDb; }
    public void setWirelessDb(int wirelessDb) { this.wirelessDb = wirelessDb; }

    @Override
    public String toString() {
        return "GlobalSetup{" +
                "ledbright=" + ledbright +
                ", screenbright=" + screenbright +
                ", darkFonts=" + darkFonts +
                ", wallpaper='" + wallpaper + '\'' +
                ", longPressTiming=" + longPressTiming +
                ", wireless24G=" + wireless24G +
                ", wirelessId=" + wirelessId +
                ", wirelessDb=" + wirelessDb +
                '}';
    }
}
