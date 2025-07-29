package com.midicap.midicap;

import java.util.List;

public class KeySection {
    private boolean enabled = true; // Whether this key is enabled
    private int keytimes;
    private String ledmode;
    private List<List<List<String>>> ledcolors; // Each operation has a list of operation types, each with 3 colors
    private List<List<String>> shortDw;   // Each short_dwN is a list of 4 values
    private List<List<String>> shortUp;
    private List<List<String>> longDw;
    private List<List<String>> longUp;
    private List<List<String>> operationTypes; // Each operation can have multiple types: shortDw, shortUp, longDw, longUp

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public int getKeytimes() { return keytimes; }
    public void setKeytimes(int keytimes) {
        if (keytimes < 1 || keytimes > 4) {
            throw new IllegalArgumentException("keytimes must be between 1 and 4 (inclusive)");
        }
        this.keytimes = keytimes;
    }

    public String getLedmode() { return ledmode; }
    public void setLedmode(String ledmode) {
        if (ledmode == null || !(ledmode.equals("select") || ledmode.equals("tap") || ledmode.equals("normal"))) {
            throw new IllegalArgumentException("ledmode must be one of: select, tap, normal");
        }
        this.ledmode = ledmode;
    }

    public List<List<List<String>>> getLedcolors() { return ledcolors; }
    public void setLedcolors(List<List<List<String>>> ledcolors) {
        if (ledcolors != null) {
            if (ledcolors.size() != this.keytimes) {
                //throw new IllegalArgumentException("Number of ledcolors must match keytimes: " + this.keytimes);
            }
            for (int i = 0; i < ledcolors.size(); i++) {
                List<List<String>> operationLedcolors = ledcolors.get(i);
                if (operationLedcolors != null) {
                    for (int j = 0; j < operationLedcolors.size(); j++) {
                        List<String> ledcolor = operationLedcolors.get(j);
                        if (ledcolor == null || ledcolor.size() != 3) {
                            throw new IllegalArgumentException("Each ledcolor must contain exactly 3 values. ledcolor " + i + "_" + j + " has " + (ledcolor != null ? ledcolor.size() : 0) + " values");
                        }
                    }
                }
            }
        }
        this.ledcolors = ledcolors;
    }

    public List<List<String>> getShortDw() { return shortDw; }
    public void setShortDw(List<List<String>> shortDw) {
        if (shortDw != null) {
            if (shortDw.size() != this.keytimes) {
                //throw new IllegalArgumentException("Number of shortDw items must match keytimes: " + this.keytimes);
            }
            for (int i = 0; i < shortDw.size(); i++) {
                List<String> shortDwItem = shortDw.get(i);
                if (shortDwItem == null || shortDwItem.size() != 4) {
                    throw new IllegalArgumentException("Each shortDw item must contain exactly 4 values. shortDw " + i + " has " + (shortDwItem != null ? shortDwItem.size() : 0) + " values");
                }
                
                // Validate midi channel (1-16)
                try {
                    int midiChannel = Integer.parseInt(shortDwItem.get(0));
                    if (midiChannel < 1 || midiChannel > 16) {
                        throw new IllegalArgumentException("Midi channel must be between 1-16. Found: " + midiChannel + " in shortDw " + i);
                    }
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Midi channel must be an integer. Found: " + shortDwItem.get(0) + " in shortDw " + i);
                }
                
                // Validate CC or PC literal
                String commandType = shortDwItem.get(1);
                if (!"PC".equals(commandType) && !"CC".equals(commandType)) {
                    throw new IllegalArgumentException("Second value must be literal 'PC' or 'CC'. Found: " + commandType + " in shortDw " + i);
                }
                
                // Validate CC/PC Number (0-127)
                try {
                    int number = Integer.parseInt(shortDwItem.get(2));
                    if (number < 0 || number > 127) {
                        throw new IllegalArgumentException("CC/PC Number must be between 0-127. Found: " + number + " in shortDw " + i);
                    }
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("CC/PC Number must be an integer. Found: " + shortDwItem.get(2) + " in shortDw " + i);
                }
                
                // Validate fourth value based on command type
                if ("PC".equals(commandType)) {
                    // For PC, fourth value must be literal "-"
                    if (!"-".equals(shortDwItem.get(3))) {
                        throw new IllegalArgumentException("Fourth value must be literal '-' for PC commands. Found: " + shortDwItem.get(3) + " in shortDw " + i);
                    }
                } else if ("CC".equals(commandType)) {
                    // For CC, fourth value must be CC Value (0-127)
                    try {
                        int ccValue = Integer.parseInt(shortDwItem.get(3));
                        if (ccValue < 0 || ccValue > 127) {
                            throw new IllegalArgumentException("CC Value must be between 0-127. Found: " + ccValue + " in shortDw " + i);
                        }
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("CC Value must be an integer. Found: " + shortDwItem.get(3) + " in shortDw " + i);
                    }
                }
            }
        }
        this.shortDw = shortDw;
    }

    public List<List<String>> getShortUp() { return shortUp; }
    public void setShortUp(List<List<String>> shortUp) {
        if (shortUp != null) {
            if (shortUp.size() != this.keytimes) {
                //throw new IllegalArgumentException("Number of shortUp items must match keytimes: " + this.keytimes);
            }
            for (int i = 0; i < shortUp.size(); i++) {
                List<String> shortUpItem = shortUp.get(i);
                if (shortUpItem == null || shortUpItem.size() != 4) {
                    throw new IllegalArgumentException("Each shortUp item must contain exactly 4 values. shortUp " + i + " has " + (shortUpItem != null ? shortUpItem.size() : 0) + " values");
                }
                
                // Validate midi channel (1-16)
                try {
                    int midiChannel = Integer.parseInt(shortUpItem.get(0));
                    if (midiChannel < 1 || midiChannel > 16) {
                        throw new IllegalArgumentException("Midi channel must be between 1-16. Found: " + midiChannel + " in shortUp " + i);
                    }
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Midi channel must be an integer. Found: " + shortUpItem.get(0) + " in shortUp " + i);
                }
                
                // Validate CC or PC literal
                String commandType = shortUpItem.get(1);
                if (!"PC".equals(commandType) && !"CC".equals(commandType)) {
                    throw new IllegalArgumentException("Second value must be literal 'PC' or 'CC'. Found: " + commandType + " in shortUp " + i);
                }
                
                // Validate CC/PC Number (0-127)
                try {
                    int number = Integer.parseInt(shortUpItem.get(2));
                    if (number < 0 || number > 127) {
                        throw new IllegalArgumentException("CC/PC Number must be between 0-127. Found: " + number + " in shortUp " + i);
                    }
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("CC/PC Number must be an integer. Found: " + shortUpItem.get(2) + " in shortUp " + i);
                }
                
                // Validate fourth value based on command type
                if ("PC".equals(commandType)) {
                    // For PC, fourth value must be literal "-"
                    if (!"-".equals(shortUpItem.get(3))) {
                        throw new IllegalArgumentException("Fourth value must be literal '-' for PC commands. Found: " + shortUpItem.get(3) + " in shortUp " + i);
                    }
                } else if ("CC".equals(commandType)) {
                    // For CC, fourth value must be CC Value (0-127)
                    try {
                        int ccValue = Integer.parseInt(shortUpItem.get(3));
                        if (ccValue < 0 || ccValue > 127) {
                            throw new IllegalArgumentException("CC Value must be between 0-127. Found: " + ccValue + " in shortUp " + i);
                        }
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("CC Value must be an integer. Found: " + shortUpItem.get(3) + " in shortUp " + i);
                    }
                }
            }
        }
        this.shortUp = shortUp;
    }

    public List<List<String>> getLongDw() { return longDw; }
    public void setLongDw(List<List<String>> longDw) {
        if (longDw != null) {
            if (longDw.size() != this.keytimes) {
                //throw new IllegalArgumentException("Number of longDw must match keytimes: " + this.keytimes);
            }
            for (int i = 0; i < longDw.size(); i++) {
                List<String> longDwItem = longDw.get(i);
                if (longDwItem == null || longDwItem.size() != 4) {
                    throw new IllegalArgumentException("Each longDw item must contain exactly 4 values. longDw " + i + " has " + (longDwItem != null ? longDwItem.size() : 0) + " values");
                }
                
                // Validate midi channel (1-16)
                try {
                    int midiChannel = Integer.parseInt(longDwItem.get(0));
                    if (midiChannel < 1 || midiChannel > 16) {
                        throw new IllegalArgumentException("Midi channel must be between 1-16. Found: " + midiChannel + " in longDw " + i);
                    }
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Midi channel must be an integer. Found: " + longDwItem.get(0) + " in longDw " + i);
                }
                
                // Validate PC literal
                if (!"PC".equals(longDwItem.get(1))) {
                    throw new IllegalArgumentException("Second value must be literal 'PC'. Found: " + longDwItem.get(1) + " in longDw " + i);
                }
                
                // Validate PC Number (0-127)
                try {
                    int pcNumber = Integer.parseInt(longDwItem.get(2));
                    if (pcNumber < 0 || pcNumber > 127) {
                        throw new IllegalArgumentException("PC Number must be between 0-127. Found: " + pcNumber + " in longDw " + i);
                    }
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("PC Number must be an integer. Found: " + longDwItem.get(2) + " in longDw " + i);
                }
                
                // Validate literal "-"
                if (!"-".equals(longDwItem.get(3))) {
                    throw new IllegalArgumentException("Fourth value must be literal '-'. Found: " + longDwItem.get(3) + " in longDw " + i);
                }
            }
        }
        this.longDw = longDw;
    }

    public List<List<String>> getLongUp() { return longUp; }
    public void setLongUp(List<List<String>> longUp) {
        if (longUp != null) {
            if (longUp.size() != this.keytimes) {
                //throw new IllegalArgumentException("Number of longUp must match keytimes: " + this.keytimes);
            }
            for (int i = 0; i < longUp.size(); i++) {
                List<String> longUpItem = longUp.get(i);
                if (longUpItem == null || longUpItem.size() != 4) {
                    throw new IllegalArgumentException("Each longUp item must contain exactly 4 values. longUp " + i + " has " + (longUpItem != null ? longUpItem.size() : 0) + " values");
                }
                
                // Validate midi channel (1-16)
                try {
                    int midiChannel = Integer.parseInt(longUpItem.get(0));
                    if (midiChannel < 1 || midiChannel > 16) {
                        throw new IllegalArgumentException("Midi channel must be between 1-16. Found: " + midiChannel + " in longUp " + i);
                    }
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Midi channel must be an integer. Found: " + longUpItem.get(0) + " in longUp " + i);
                }
                
                // Validate PC literal
                if (!"PC".equals(longUpItem.get(1))) {
                    throw new IllegalArgumentException("Second value must be literal 'PC'. Found: " + longUpItem.get(1) + " in longUp " + i);
                }
                
                // Validate PC Number (0-127)
                try {
                    int pcNumber = Integer.parseInt(longUpItem.get(2));
                    if (pcNumber < 0 || pcNumber > 127) {
                        throw new IllegalArgumentException("PC Number must be between 0-127. Found: " + pcNumber + " in longUp " + i);
                    }
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("PC Number must be an integer. Found: " + longUpItem.get(2) + " in longUp " + i);
                }
                
                // Validate literal "-"
                if (!"-".equals(longUpItem.get(3))) {
                    throw new IllegalArgumentException("Fourth value must be literal '-'. Found: " + longUpItem.get(3) + " in longUp " + i);
                }
            }
        }
        this.longUp = longUp;
    }



    public List<List<String>> getOperationTypes() { return operationTypes; }
    public void setOperationTypes(List<List<String>> operationTypes) {
        if (operationTypes != null) {
            if (operationTypes.size() != this.keytimes) {
                //throw new IllegalArgumentException("Number of operation types must match keytimes: " + this.keytimes);
            }
            for (int i = 0; i < operationTypes.size(); i++) {
                List<String> operationTypeList = operationTypes.get(i);
                if (operationTypeList != null) {
                    for (int j = 0; j < operationTypeList.size(); j++) {
                        String operationType = operationTypeList.get(j);
                        if (operationType == null || !(operationType.equals("shortDw") || operationType.equals("shortUp") || operationType.equals("longDw") || operationType.equals("longUp"))) {
                            throw new IllegalArgumentException("Operation type must be one of: shortDw, shortUp, longDw, longUp. Found: " + operationType + " at operation " + i + ", type " + j);
                        }
                    }
                }
            }
        }
        this.operationTypes = operationTypes;
    }

    @Override
    public String toString() {
        return "KeySection{" +
                "enabled=" + enabled +
                ", keytimes=" + keytimes +
                ", ledmode='" + ledmode + '\'' +
                ", ledcolors=" + ledcolors +
                ", shortDw=" + shortDw +
                ", shortUp=" + shortUp +
                ", longDw=" + longDw +
                ", longUp=" + longUp +
                ", operationTypes=" + operationTypes +
                '}';
    }
} 