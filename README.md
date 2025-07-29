# MidiCap Configuration Generator

A web-based configuration generator for MIDI controller settings using Spring Boot and Thymeleaf.

## Features

- **Global Setup Configuration**: Configure LED brightness, screen brightness, fonts, wallpaper, and wireless settings
- **Modern Web Interface**: Beautiful, responsive UI with real-time feedback
- **Configuration File Generation**: Generate downloadable configuration files
- **Default Values**: Pre-configured with sensible defaults

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Running the Application

1. Clone or download the project
2. Navigate to the project directory
3. Run the application:

```bash
mvn spring-boot:run
```

4. Open your web browser and go to: `http://localhost:8080`

### Using the Application

1. **Configure Global Setup**: 
   - Adjust LED and screen brightness using the sliders
   - Enable/disable dark fonts for better contrast
   - Select wallpaper theme
   - Set long press timing
   - Configure wireless settings (for Blue/Gold versions)

2. **Generate Configuration**: 
   - Click "Generate Configuration File" to download the configuration
   - The file will be saved as `page_config.txt`

3. **Reset to Defaults**: 
   - Click "Reset to Defaults" to restore default values

## Configuration Options

### Global Setup Parameters

- **LED Brightness**: 0-100 (0 = off, 100 = maximum)
- **Screen Brightness**: 0-100 (0 = off, 100 = maximum)
- **Dark Fonts**: Enable/disable for better contrast
- **Wallpaper**: Select from available themes (wp1, wp2, wp3, custom)
- **Long Press Timing**: 1.0, 1.5, 2.0, or 2.5 seconds
- **Wireless 2.4G**: Enable/disable wireless connection
- **Wireless ID**: 1-99 (must match MIDI MATE)
- **Wireless dB**: Transmission power level (0-14, various dBm values)

### Page Configuration Parameters

- **Page Name**: 4-character uppercase name for the page
- **Expression Pedal 1**: Channel (1-16) and CC number (0-127)
- **Expression Pedal 2**: Channel (1-16) and CC number (0-127)
- **Encoder**: CC number (0-127) and name (max 10 characters)
- **MIDI Through**: Enable/disable MIDI through functionality
- **Display Number Format**: 123 (numerical) or abcX (letter display)
- **Group Number**: 3, 4, 5, or 8 patches per group
- **Display PC Offset**: 0 (start from 0) or 1 (start from 1)
- **Display Bank Offset**: 0 (0A,0B,0C...) or 1 (1A,1B,1C...)

### Key Configuration Parameters

- **Key Times**: Number of times each key can be pressed (1-4)
- **LED Mode**: Behavior mode (normal, select, tap)
- **LED Colors**: Three color values for each key time (hex format)
- **Short Down/Up**: MIDI commands for key press/release
- **Long Down/Up**: MIDI commands for long press/release
- **Collapsible Interface**: Click on any key to expand its configuration

## Project Structure

```
src/
├── main/
│   ├── java/com/midicap/midicap/
│   │   ├── controller/
│   │   │   └── PageConfigController.java    # Web controller
│   │   ├── GlobalSetup.java                 # Global configuration model
│   │   ├── KeySection.java                  # Key configuration model
│   │   ├── PageConfig.java                  # Main configuration model
│   │   ├── PageSection.java                 # Page configuration model
│   │   └── MidiCapApplication.java          # Spring Boot application
│   └── resources/
│       ├── templates/
│       │   └── index.html                   # Thymeleaf template
│       └── application.properties           # Application configuration
```

## Technology Stack

- **Spring Boot 3.5.3**: Main framework
- **Thymeleaf**: Template engine
- **Maven**: Build tool
- **Java 17**: Programming language

## Development

### Adding New Features

1. **Page Configuration**: Extend the form to include PageSection settings
2. **Key Configuration**: Add KeySection configuration for individual keys
3. **Validation**: Add client-side and server-side validation
4. **Multiple Pages**: Support for configuring multiple pages

### Building

```bash
mvn clean compile
mvn package
```

## License

This project is part of the MidiCap MIDI controller configuration system.

## Support

For issues and questions, please refer to the MidiCap documentation or create an issue in the project repository. 