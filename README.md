# C1-1OP

Repository for the Android app used as a controller and MCU projects for C1-1OP (Chopper).  
This workspace contains multiple subprojects for controlling and interfacing with the droid, including Android and microcontroller code.

## Projects

- [AndroidController](AndroidController/README.md)  
  Android app for controlling Chopper via Bluetooth. It uses JSON as the format of communication with the ESP32.

- [BodyController](BodyController/README.md)  
  PlatformIO project for the main body MCU. Handles commands from the receiver and controls body actuators. This is meant to be used with an Arduino Mega.

- [ChopperHead](ChopperHead/README.md)  
  PlatformIO project for the head MCU. Manages head movements and lights. It is meant to be used with an Arduino Nano Every.

- [FeetsController](FeetsController/README.md)  
  Arduino sketch for controlling the feet motors. It was created to be used with a Cytron SmartDriveDuo-30.

- [receiverMCU](receiverMCU/README.md)  
  ESP32-based bridge between controllers (Android app, Flysky FS-I6X) and the droid's brain. Handles Bluetooth and RC input, forwards commands, and manages idle actions.

---

See each project's README for build instructions and details.
