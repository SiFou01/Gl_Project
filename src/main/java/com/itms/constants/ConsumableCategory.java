package com.itms.constants;

public class ConsumableCategory {
    public static final String RAM = "RAM";
    public static final String TONER = "Toner";
    public static final String HARD_DRIVE = "Hard Drive";
    public static final String PROCESSOR = "Processor";
    public static final String POWER_SUPPLY = "Power Supply";
    public static final String MOTHERBOARD = "Motherboard";
    public static final String PAPER = "Paper";
    public static final String GRAPHICS_CARD = "Graphics Card";
    public static final String SOUND_CARD = "Sound Card";
    public static final String INK_CARTRIDGE = "Ink Cartridge";

    public static final String[] ALL_CATEGORIES = {
            RAM,
            TONER,
            HARD_DRIVE,
            PROCESSOR,
            POWER_SUPPLY,
            MOTHERBOARD,
            PAPER,
            GRAPHICS_CARD,
            SOUND_CARD,
            INK_CARTRIDGE
    };

    private ConsumableCategory() {
        // Utility class, prevent instantiation
    }
}
