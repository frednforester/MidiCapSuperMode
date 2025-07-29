package com.midicap.midicap.controller;

import com.midicap.midicap.GlobalSetup;
import com.midicap.midicap.KeySection;
import com.midicap.midicap.PageConfig;
import com.midicap.midicap.PageSection;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class PageConfigController {

    private Logger logger = LoggerFactory.getLogger(PageConfigController.class);
    
    @GetMapping("/")
    public String index(Model model) {
        // Initialize with default PageConfig
        PageConfig pageConfig = createDefaultPageConfig();
        model.addAttribute("pageConfig", pageConfig);
        return "index";
    }

    @PostMapping("/generate")
    public void generateConfig(@ModelAttribute PageConfig pageConfig, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        response.setHeader("Content-Disposition", "attachment; filename=\"page_config.txt\"");
        
        try (PrintWriter writer = response.getWriter()) {
            writePageConfig(pageConfig, writer);
        }
    }

    @PostMapping("/save")
    public String saveConfig(@ModelAttribute PageConfig pageConfig, Model model) {
        // Here you could save to database or file system
        model.addAttribute("pageConfig", pageConfig);
        model.addAttribute("message", "Configuration saved successfully!");
        return "index";
    }

    @PostMapping("/preview")
    @ResponseBody
    public String previewConfig(@ModelAttribute PageConfig pageConfig) {
        // Return a preview of the configuration
        StringBuilder preview = new StringBuilder();
        preview.append("=== MIDI Captain Configuration Preview ===\n\n");
        
        Queue<String> previewRecords = this.writeConfigToQueue(pageConfig);
        for(String rec : previewRecords)
        {
            preview.append(rec).append("\n");
        }
        
        if (1 == 1)
        {
            return preview.toString();
        }
        GlobalSetup global = pageConfig.getGlobalSetup();
        if (global != null) {
            preview.append("Global Setup:\n");
            preview.append("- LED Brightness: ").append(global.getLedbright()).append("\n");
            preview.append("- Screen Brightness: ").append(global.getScreenbright()).append("\n");
            preview.append("- Dark Fonts: ").append(global.isDarkFonts() ? "On" : "Off").append("\n");
            preview.append("- Wallpaper: ").append(global.getWallpaper()).append("\n");
            preview.append("- Long Press Timing: ").append(global.getLongPressTiming()).append("s\n");
            preview.append("- Wireless 2.4G: ").append(global.isWireless24G() ? "On" : "Off").append("\n");
            preview.append("- Wireless ID: ").append(global.getWirelessId()).append("\n");
            preview.append("- Wireless dB: ").append(global.getWirelessDb()).append("\n");
        }
        
        PageSection page = pageConfig.getPageSection();
        if (page != null) {
            preview.append("\nPage Section:\n");
            preview.append("- Page Name: ").append(page.getPageName()).append("\n");
            preview.append("- Expression Pedal 1: CH").append(page.getExp1CH()).append(" CC").append(page.getExp1CC()).append("\n");
            preview.append("- Expression Pedal 2: CH").append(page.getExp2CH()).append(" CC").append(page.getExp2CC()).append("\n");
            preview.append("- Encoder: CC").append(page.getEncoderCC()).append(" (").append(page.getEncoderName()).append(")\n");
            preview.append("- MIDI Through: ").append(page.isMidithrough() ? "On" : "Off").append("\n");
            preview.append("- Display Format: ").append(page.getDisplayNumberABC()).append("\n");
            preview.append("- Group Number: ").append(page.getGroupNumber()).append("\n");
            preview.append("- PC Offset: ").append(page.getDisplayPcOffset() == 1 ? "Start from 1" : "Start from 0").append("\n");
            preview.append("- Bank Offset: ").append(page.getDisplayBankOffset() == 1 ? "Start from 1A" : "Start from 0A").append("\n");
        }
        
        
        
        // Add Key Sections preview
        List<KeySection> keySections = pageConfig.getKeySections();
        logger.debug("Starting keys:" + keySections);
        if (keySections != null && !keySections.isEmpty()) {
            preview.append("\nKey Sections:\n");
            for (int i = 0; i < keySections.size(); i++) {
                KeySection keySection = keySections.get(i);
                logger.debug("Starting keys:" + keySection);
                if (keySection != null && keySection.isEnabled()) {
                    preview.append("- Key ").append(i).append(": ").append(keySection.getKeytimes())
                           .append(" operations, LED mode: ").append(keySection.getLedmode()).append("\n");
                    
                    // Show operation types
                    List<List<String>> operationTypes = keySection.getOperationTypes();
                    if (operationTypes != null && !operationTypes.isEmpty()) {
                        for (int j = 0; j < operationTypes.size(); j++) {
                            List<String> operationTypeList = operationTypes.get(j);
                            if (operationTypeList != null && !operationTypeList.isEmpty()) {
                                preview.append("  Operation ").append(j + 1).append(": ").append(String.join(", ", operationTypeList)).append("\n");
                            }
                        }
                    }
                }
            }
        }
        
        return preview.toString();
    }

    private PageConfig createDefaultPageConfig() {
        PageConfig config = new PageConfig();
        
        // Default GlobalSetup
        GlobalSetup globalSetup = new GlobalSetup();
        globalSetup.setLedbright(30);
        globalSetup.setScreenbright(80);
        globalSetup.setDarkFonts(false);
        globalSetup.setWallpaper("wp1");
        globalSetup.setLongPressTiming(1.0);
        globalSetup.setWireless24G(true);
        globalSetup.setWirelessId(8);
        globalSetup.setWirelessDb(6);
        config.setGlobalSetup(globalSetup);
        
        // Default PageSection
        PageSection pageSection = new PageSection();
        pageSection.setPageName("PAGE");
        pageSection.setExp1CH(1);
        pageSection.setExp1CC(1);
        pageSection.setExp2CH(1);
        pageSection.setExp2CC(2);
        pageSection.setEncoderCC(90);
        pageSection.setEncoderName("MYCC");
        pageSection.setMidithrough(true);
        pageSection.setDisplayNumberABC("abc3");
        pageSection.setGroupNumber(3);
        pageSection.setDisplayPcOffset(1);
        pageSection.setDisplayBankOffset(1);
        config.setPageSection(pageSection);
        
        // Default KeySections (10 keys)
        List<KeySection> keySections = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            KeySection keySection = new KeySection();
            keySection.setEnabled(true);
            keySection.setKeytimes(1);
            keySection.setLedmode("normal");
            
            // Default ledcolors (1 operation with 1 operation type, 3 colors each)
            List<List<List<String>>> ledcolors = new ArrayList<>();
            List<List<String>> operationLedcolors = new ArrayList<>();
            List<String> ledcolor = new ArrayList<>();
            ledcolor.add("0x666666");
            ledcolor.add("0x666666");
            ledcolor.add("0xff0000");
            operationLedcolors.add(ledcolor);
            ledcolors.add(operationLedcolors);
            keySection.setLedcolors(ledcolors);
            
            // Default shortDw (1 item)
            List<List<String>> shortDw = new ArrayList<>();
            List<String> shortDwItem = new ArrayList<>();
            shortDwItem.add("1");
            shortDwItem.add("CC");
            shortDwItem.add("69");
            shortDwItem.add("0");
            shortDw.add(shortDwItem);
            keySection.setShortDw(shortDw);
            
            // Default shortUp (1 item)
            List<List<String>> shortUp = new ArrayList<>();
            List<String> shortUpItem = new ArrayList<>();
            shortUpItem.add("1");
            shortUpItem.add("CC");
            shortUpItem.add("69");
            shortUpItem.add("0");
            shortUp.add(shortUpItem);
            keySection.setShortUp(shortUp);
            
            // Default longDw (1 item)
            List<List<String>> longDw = new ArrayList<>();
            List<String> longDwItem = new ArrayList<>();
            longDwItem.add("1");
            longDwItem.add("PC");
            longDwItem.add("0");
            longDwItem.add("-");
            longDw.add(longDwItem);
            keySection.setLongDw(longDw);
            
            // Default longUp (1 item)
            List<List<String>> longUp = new ArrayList<>();
            List<String> longUpItem = new ArrayList<>();
            longUpItem.add("1");
            longUpItem.add("PC");
            longUpItem.add("0");
            longUpItem.add("-");
            longUp.add(longUpItem);
            keySection.setLongUp(longUp);
            
            // Default operation types (1 operation with shortDw)
            List<List<String>> operationTypes = new ArrayList<>();
            List<String> operationTypeList = new ArrayList<>();
            operationTypeList.add("shortDw");
            operationTypes.add(operationTypeList);
            keySection.setOperationTypes(operationTypes);
            
            keySections.add(keySection);
        }
        config.setKeySections(keySections);
        
        return config;
    }

    private void writePageConfig(PageConfig config, PrintWriter writer) {
        // Write GlobalSetup section
        writer.println("[globalsetup]");
        writer.println("# globalsetup items in page0 is applied to all pages");
        writer.println("# Lines starting with # are treated as comments");
        writer.println();
        writer.println("# 0-100");
        writer.println("ledbright = [" + config.getGlobalSetup().getLedbright() + "]");
        writer.println();
        writer.println("# 0-100");
        writer.println("screenbright = [" + config.getGlobalSetup().getScreenbright() + "]");
        writer.println();
        writer.println("# on / off --Selecting black or white fonts according to the wallpaper background");
        writer.println("dark_fonts = [" + (config.getGlobalSetup().isDarkFonts() ? "on" : "off") + "]");
        writer.println();
        writer.println("# wp1 --Only one wallpaper coming along with the FW package now, you can add later");
        writer.println("wallpaper = [" + config.getGlobalSetup().getWallpaper() + "]");
        writer.println();
        writer.println("# 1 / 1.5 / 2 / 2.5 --long press time in second");
        writer.println("long_press_timing = [" + config.getGlobalSetup().getLongPressTiming() + "]");
        writer.println();
        writer.println("# wireless setup only valid on MIDI Captain Blue/Gold version");
        writer.println("WIRELESS_2.4G = [" + (config.getGlobalSetup().isWireless24G() ? "on" : "off") + "]");
        writer.println();
        writer.println("# ID range 1-99 --Set to be the same with the MIDI MATE");
        writer.println("WIRELESS_ID   = [" + config.getGlobalSetup().getWirelessId() + "]");
        writer.println();
        writer.println("# 0:12dBm 1:10dBm 2:9dBm 3:8dBm 4:6dBm 5:3dBm 6:0dBm 7:-2dBm 8:-5dBm 9:-10dBm 10:-15dBm 11:-20dBm 12:-25dBm 13:-30dBm 14:-25dBm");
        writer.println("WIRELESS_dB   = [" + config.getGlobalSetup().getWirelessDb() + "]");
        writer.println();
        writer.println();

        // Write PAGE section
        writer.println("[PAGE]");
        writer.println("# Change the \"NAME\" below in uppercase letters <= 4 characters");
        writer.println("page_name = [" + config.getPageSection().getPageName() + "]");
        writer.println();
        writer.println("# This section sets the global items within a page");
        writer.println();
        writer.println("exp1_CH = [" + config.getPageSection().getExp1CH() + "]");
        writer.println("exp1_CC = [" + config.getPageSection().getExp1CC() + "]");
        writer.println();
        writer.println("exp2_CH = [" + config.getPageSection().getExp2CH() + "]");
        writer.println("exp2_CC = [" + config.getPageSection().getExp2CC() + "]");
        writer.println();
        writer.println("encoder_CC = [" + config.getPageSection().getEncoderCC() + "]");
        writer.println("encoder_NAME = [" + config.getPageSection().getEncoderName() + "]");
        writer.println();
        writer.println("# on / off");
        writer.println("midithrough = [" + (config.getPageSection().isMidithrough() ? "on" : "off") + "]");
        writer.println();
        writer.println("# 123 / abc3 / abc4 / abc5 / abc8 --Only this 5 options");
        writer.println("# Setting 123 is to display the PC as a numerical value");
        writer.println("# abc3 means PC display as 1A,1B,1C,2A... abc5 means 1A,1B,1C,1D,1E,2A...");
        writer.println("display_number_ABC = [" + config.getPageSection().getDisplayNumberABC() + "]");
        writer.println();
        writer.println("# 3 / 4 / 5 / 8 define how many patches in one group or bank");
        writer.println("# This is used when display_number_ABC is selected as '123'");
        writer.println("group_number = [" + config.getPageSection().getGroupNumber() + "]");
        writer.println();
        writer.println("# 0 / 1 ");
        writer.println("# set to 1 so that the displayed PC number starting from 1 (Actual PC still 0 )");
        writer.println("# set to 0 so that the displayed PC number starting from 0");
        writer.println("display_pc_offset = [" + config.getPageSection().getDisplayPcOffset() + "]");
        writer.println();
        writer.println("# 0 / 1 ");
        writer.println("# set to 0 so that when abcX display selected above, bank is start from 0A,0B,0C...");
        writer.println("# set to 1 so that when abcX display selected above, bank is start from 1A,1B,1C...");
        writer.println("display_bank_offset = [" + config.getPageSection().getDisplayBankOffset() + "]");
        writer.println();

        // Write KeySections
        for (int i = 0; i < config.getKeySections().size(); i++) {
            KeySection keySection = config.getKeySections().get(i);
            logger.debug("Starting keys 2:" + keySection);
            if (keySection != null && keySection.isEnabled()) {
                writer.println("[key" + i + "]");
                writer.println("keytimes = [" + keySection.getKeytimes() + "]");
                writer.println("ledmode = [" + keySection.getLedmode() + "]");
                
                // Write ledcolors for each operation type
                if (keySection.getLedcolors() != null && !keySection.getLedcolors().isEmpty()) {
                    for (int j = 0; j < keySection.getLedcolors().size(); j++) {
                        List<List<String>> operationLedcolors = keySection.getLedcolors().get(j);
                        if (operationLedcolors != null && !operationLedcolors.isEmpty()) {
                            for (int k = 0; k < operationLedcolors.size(); k++) {
                                List<String> ledcolor = operationLedcolors.get(k);
                                List<String> dfltcolor = new ArrayList();
                                logger.debug("ledcolor 2:" + ledcolor.size());
                                for(String col : ledcolor)
                                {
                                    if (col.isEmpty())
                                    {
                                        dfltcolor.add("0xff0000");
                                    }
                                    else
                                    {
                                        dfltcolor.add(col);
                                    }
                                }
                                if (ledcolor != null && !ledcolor.isEmpty()) {
                                    writer.println("ledcolor" + (j + 1) +  " = [" + String.join("][", dfltcolor) + "]");
                                }
                            }
                        }
                    }
                }
                
                // Write operations based on operation types
                List<List<String>> operationTypes = keySection.getOperationTypes();
                if (operationTypes != null && !operationTypes.isEmpty()) {
                    for (int j = 0; j < operationTypes.size(); j++) {
                        List<String> operationTypeList = operationTypes.get(j);
                        if (operationTypeList != null && !operationTypeList.isEmpty()) {
                            for (String operationType : operationTypeList) {
                                List<String> operationData = null;
                                String operationName = "";
                                
                                switch (operationType) {
                                    case "shortDw":
                                        if (keySection.getShortDw() != null && j < keySection.getShortDw().size()) {
                                            operationData = keySection.getShortDw().get(j);
                                            operationName = "short_dw" + (j + 1);
                                        }
                                        break;
                                    case "shortUp":
                                        if (keySection.getShortUp() != null && j < keySection.getShortUp().size()) {
                                            operationData = keySection.getShortUp().get(j);
                                            operationName = "short_up" + (j + 1);
                                        }
                                        break;
                                    case "longDw":
                                        if (keySection.getLongDw() != null && j < keySection.getLongDw().size()) {
                                            operationData = keySection.getLongDw().get(j);
                                            operationName = "long" + (j + 1);
                                        }
                                        break;
                                    case "longUp":
                                        if (keySection.getLongUp() != null && j < keySection.getLongUp().size()) {
                                            operationData = keySection.getLongUp().get(j);
                                            operationName = "long_up" + (j + 1);
                                        }
                                        break;
                                }
                                
                                if (operationData != null && !operationData.isEmpty()) {
                                    writer.println(operationName + " = [" + String.join("][", operationData) + "]");
                                }
                            }
                        }
                    }
                }
                
                writer.println();
            }
        } //here
    }
    
    
    private Queue<String> writeConfigToQueue(PageConfig config)
    {
        Queue<String> writer = new LinkedList<>();
        
        writer.add("[globalsetup]");
        writer.add("# globalsetup items in page0 is applied to all pages");
        writer.add("# Lines starting with # are treated as comments");
        writer.add("");
        writer.add("# 0-100");
        writer.add("ledbright = [" + config.getGlobalSetup().getLedbright() + "]");
        writer.add("");
        writer.add("# 0-100");
        writer.add("screenbright = [" + config.getGlobalSetup().getScreenbright() + "]");
        writer.add("");
        writer.add("# on / off --Selecting black or white fonts according to the wallpaper background");
        writer.add("dark_fonts = [" + (config.getGlobalSetup().isDarkFonts() ? "on" : "off") + "]");
        writer.add("");
        writer.add("# wp1 --Only one wallpaper coming along with the FW package now, you can add later");
        writer.add("wallpaper = [" + config.getGlobalSetup().getWallpaper() + "]");
        writer.add("");
        writer.add("# 1 / 1.5 / 2 / 2.5 --long press time in second");
        writer.add("long_press_timing = [" + config.getGlobalSetup().getLongPressTiming() + "]");
        writer.add("");
        writer.add("# wireless setup only valid on MIDI Captain Blue/Gold version");
        writer.add("WIRELESS_2.4G = [" + (config.getGlobalSetup().isWireless24G() ? "on" : "off") + "]");
        writer.add("");
        writer.add("# ID range 1-99 --Set to be the same with the MIDI MATE");
        writer.add("WIRELESS_ID   = [" + config.getGlobalSetup().getWirelessId() + "]");
        writer.add("");
        writer.add("# 0:12dBm 1:10dBm 2:9dBm 3:8dBm 4:6dBm 5:3dBm 6:0dBm 7:-2dBm 8:-5dBm 9:-10dBm 10:-15dBm 11:-20dBm 12:-25dBm 13:-30dBm 14:-25dBm");
        writer.add("WIRELESS_dB   = [" + config.getGlobalSetup().getWirelessDb() + "]");
        writer.add("");
        writer.add("");
        
        
        // Write PAGE section
        writer.add("[PAGE]");
        writer.add("# Change the \"NAME\" below in uppercase letters <= 4 characters");
        writer.add("page_name = [" + config.getPageSection().getPageName() + "]");
        writer.add("");
        writer.add("# This section sets the global items within a page");
        writer.add("");
        writer.add("exp1_CH = [" + config.getPageSection().getExp1CH() + "]");
        writer.add("exp1_CC = [" + config.getPageSection().getExp1CC() + "]");
        writer.add("");
        writer.add("exp2_CH = [" + config.getPageSection().getExp2CH() + "]");
        writer.add("exp2_CC = [" + config.getPageSection().getExp2CC() + "]");
        writer.add("");
        writer.add("encoder_CC = [" + config.getPageSection().getEncoderCC() + "]");
        writer.add("encoder_NAME = [" + config.getPageSection().getEncoderName() + "]");
        writer.add("");
        writer.add("# on / off");
        writer.add("midithrough = [" + (config.getPageSection().isMidithrough() ? "on" : "off") + "]");
        writer.add("");
        writer.add("# 123 / abc3 / abc4 / abc5 / abc8 --Only this 5 options");
        writer.add("# Setting 123 is to display the PC as a numerical value");
        writer.add("# abc3 means PC display as 1A,1B,1C,2A... abc5 means 1A,1B,1C,1D,1E,2A...");
        writer.add("display_number_ABC = [" + config.getPageSection().getDisplayNumberABC() + "]");
        writer.add("");
        writer.add("# 3 / 4 / 5 / 8 define how many patches in one group or bank");
        writer.add("# This is used when display_number_ABC is selected as '123'");
        writer.add("group_number = [" + config.getPageSection().getGroupNumber() + "]");
        writer.add("");
        writer.add("# 0 / 1 ");
        writer.add("# set to 1 so that the displayed PC number starting from 1 (Actual PC still 0 )");
        writer.add("# set to 0 so that the displayed PC number starting from 0");
        writer.add("display_pc_offset = [" + config.getPageSection().getDisplayPcOffset() + "]");
        writer.add("");
        writer.add("# 0 / 1 ");
        writer.add("# set to 0 so that when abcX display selected above, bank is start from 0A,0B,0C...");
        writer.add("# set to 1 so that when abcX display selected above, bank is start from 1A,1B,1C...");
        writer.add("display_bank_offset = [" + config.getPageSection().getDisplayBankOffset() + "]");
        writer.add("");
        
        // Write KeySections
        for (int i = 0; i < config.getKeySections().size(); i++) {
            KeySection keySection = config.getKeySections().get(i);
            logger.debug("Starting keys 2:" + keySection);
            if (keySection != null && keySection.isEnabled()) {
                writer.add("[key" + i + "]");
                writer.add("keytimes = [" + keySection.getKeytimes() + "]");
                writer.add("ledmode = [" + keySection.getLedmode() + "]");
                logger.debug("Starting keys 2:" + keySection.getLedcolors());
                // Write ledcolors for each operation type
                if (keySection.getLedcolors() != null && !keySection.getLedcolors().isEmpty()) {
                    for (int j = 0; j < keySection.getLedcolors().size(); j++) {
                        List<List<String>> operationLedcolors = keySection.getLedcolors().get(j);
                        if (operationLedcolors != null && !operationLedcolors.isEmpty()) {
                            for (int k = 0; k < operationLedcolors.size(); k++) {
                                List<String> ledcolor = operationLedcolors.get(k);
                                List<String> dfltcolor = new ArrayList();
                                logger.debug("ledcolor 2:" + ledcolor.size());
                                for(String col : ledcolor)
                                {
                                    if (col.isEmpty())
                                    {
                                        dfltcolor.add("0xff0000");
                                    }
                                    else
                                    {
                                        dfltcolor.add(col);
                                    }
                                }
                                if (ledcolor != null && !ledcolor.isEmpty()) {
                                    writer.add("ledcolor" + (j + 1) +  " = [" + String.join("][", dfltcolor) + "]");
                                }
                            }
                        }
                    }
                }
                
                // Write operations based on operation types
                List<List<String>> operationTypes = keySection.getOperationTypes();
                if (operationTypes != null && !operationTypes.isEmpty()) {
                    for (int j = 0; j < operationTypes.size(); j++) {
                        List<String> operationTypeList = operationTypes.get(j);
                        if (operationTypeList != null && !operationTypeList.isEmpty()) {
                            for (String operationType : operationTypeList) {
                                List<String> operationData = null;
                                String operationName = "";
                                
                                switch (operationType) {
                                    case "shortDw":
                                        if (keySection.getShortDw() != null && j < keySection.getShortDw().size()) {
                                            operationData = keySection.getShortDw().get(j);
                                            operationName = "short_dw" + (j + 1);
                                        }
                                        break;
                                    case "shortUp":
                                        if (keySection.getShortUp() != null && j < keySection.getShortUp().size()) {
                                            operationData = keySection.getShortUp().get(j);
                                            operationName = "short_up" + (j + 1);
                                        }
                                        break;
                                    case "longDw":
                                        if (keySection.getLongDw() != null && j < keySection.getLongDw().size()) {
                                            operationData = keySection.getLongDw().get(j);
                                            operationName = "long" + (j + 1);
                                        }
                                        break;
                                    case "longUp":
                                        if (keySection.getLongUp() != null && j < keySection.getLongUp().size()) {
                                            operationData = keySection.getLongUp().get(j);
                                            operationName = "long_up" + (j + 1);
                                        }
                                        break;
                                }
                                
                                if (operationData != null && !operationData.isEmpty()) {
                                    writer.add(operationName + " = [" + String.join("][", operationData) + "]");
                                }
                            }
                        }
                    }
                }
                
                writer.add("");
            }
        } //here

        return writer;
         
    }
} 