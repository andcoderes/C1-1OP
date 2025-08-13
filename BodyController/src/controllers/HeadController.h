#pragma once
#include <Arduino.h>
#include <SoftwareSerial.h>
#include <vector>


#define MOTOR_HEAD_LEFT 7
#define MOTOR_HEAD_RIGHT 8
#define HEAD_ACCELERATION 9

class HeadController{
    public:
        HeadController(int pinLeft, int pinRight, int pintAcceleration);
        void setup();
        void moveHead(int rx);
        void animate(std::vector<int> macros);
        void update();
        void stop();
    private: 
        std::vector<int> macroId = {100, 101};
        unsigned long whenToStop = 2350;
        std::vector<unsigned long> timesToStop = {2350, 2450};
        int kPinLeft;
        int kPinRight;
        int kPinAcceleration;
        unsigned long lastCurrent = 0;
        bool macroRunning = false;
};