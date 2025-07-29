package com.midicap.midicap;

public class PageSection {
    private String pageName;
    private int exp1CH;
    private int exp1CC;
    private int exp2CH;
    private int exp2CC;
    private int encoderCC;
    private String encoderName;
    private boolean midithrough;
    private String displayNumberABC;
    private int groupNumber;
    private int displayPcOffset;
    private int displayBankOffset;

    public String getPageName() { return pageName; }
    public void setPageName(String pageName) { this.pageName = pageName; }

    public int getExp1CH() { return exp1CH; }
    public void setExp1CH(int exp1ch) { this.exp1CH = exp1ch; }

    public int getExp1CC() { return exp1CC; }
    public void setExp1CC(int exp1cc) { this.exp1CC = exp1cc; }

    public int getExp2CH() { return exp2CH; }
    public void setExp2CH(int exp2ch) { this.exp2CH = exp2ch; }

    public int getExp2CC() { return exp2CC; }
    public void setExp2CC(int exp2cc) { this.exp2CC = exp2cc; }

    public int getEncoderCC() { return encoderCC; }
    public void setEncoderCC(int encoderCC) { this.encoderCC = encoderCC; }

    public String getEncoderName() { return encoderName; }
    public void setEncoderName(String encoderName) { this.encoderName = encoderName; }

    public boolean isMidithrough() { return midithrough; }
    public void setMidithrough(boolean midithrough) { this.midithrough = midithrough; }

    public String getDisplayNumberABC() { return displayNumberABC; }
    public void setDisplayNumberABC(String displayNumberABC) { this.displayNumberABC = displayNumberABC; }

    public int getGroupNumber() { return groupNumber; }
    public void setGroupNumber(int groupNumber) { this.groupNumber = groupNumber; }

    public int getDisplayPcOffset() { return displayPcOffset; }
    public void setDisplayPcOffset(int displayPcOffset) { this.displayPcOffset = displayPcOffset; }

    public int getDisplayBankOffset() { return displayBankOffset; }
    public void setDisplayBankOffset(int displayBankOffset) { this.displayBankOffset = displayBankOffset; }

    @Override
    public String toString() {
        return "PageSection{" +
                "pageName='" + pageName + '\'' +
                ", exp1CH=" + exp1CH +
                ", exp1CC=" + exp1CC +
                ", exp2CH=" + exp2CH +
                ", exp2CC=" + exp2CC +
                ", encoderCC=" + encoderCC +
                ", encoderName='" + encoderName + '\'' +
                ", midithrough=" + midithrough +
                ", displayNumberABC='" + displayNumberABC + '\'' +
                ", groupNumber=" + groupNumber +
                ", displayPcOffset=" + displayPcOffset +
                ", displayBankOffset=" + displayBankOffset +
                '}';
    }
} 