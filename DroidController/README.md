# Droid Controller

Droid Controller is an Android application designed to control the C1-1OP (Chopper) droid via Bluetooth, it can be used for others droids as long as they use a similar version of the receiverMCU. The app provides an intuitive interface for sending commands, macros, and audio triggers to the droid's ESP32 receiver using JSON-formatted messages.

## Features

- **Bluetooth Connectivity:**  
  Connects to the droid's ESP32 receiver for wireless control.

- **Manual Controls:**  
  Control movement, lights, and other actuators directly from the app.

- **Macros:**  
  Create and execute sequences of commands for complex actions.

- **Audio Commands:**  
  Trigger audio playback on the droid.

- **Status Monitoring:**  
  View connection status and receive feedback from the droid.

## Communication Protocol

- Uses JSON messages over Bluetooth to communicate with the ESP32 receiver MCU.

## Getting Started

### Prerequisites

- Android device (Android 8.0 or higher recommended)
- C1-1OP droid with ESP32 receiver MCU configured for Bluetooth communication

### Installation

1. Clone this repository:
   ```
   git clone https://github.com/yourusername/C1-1OP.git
   ```
2. Open the `AndroidController` project in Android Studio.
3. Build and run the app on your Android device.

### Usage

1. Launch the app on your Android device.
2. Pair with the droid's ESP32 receiver via Bluetooth.
3. Use the interface to send commands, run macros, or trigger audio.

## Project Structure

- `app/src/main/java/`  
  Main source code for activities, Bluetooth communication, and UI.
- `app/src/main/res/`  
  Layouts, drawables, and other resources.
- `app/src/main/assets/`  
  Macro definitions and audio files.

## Contributing

Contributions are welcome! Please submit issues or pull requests for improvements or new features.

## License

This project is licensed under the MIT License.

## Authors

- [@andcoderes](https://github.com/andcoderes) maintainer

---

For more details on the communication protocol and supported commands, see the [Protocol Documentation](docs/protocol.md).
