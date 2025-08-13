#pragma once
#include <Arduino.h>
#include "ButtonsAction.h"
#include <ArduinoJson.h>

#define ARDUINO_STATUS -1
#define BUTTON_STATUS 0
#define MOVEMENT_STATUS 1
#define SETTINGS_STATUS 2
#define DEBUG_STATUS 99

using CommunicationCallback = void (*)(const ButtonsAction);

class ComunicationController{ 
    public:
        bool setup();
        void setCallback(CommunicationCallback callback);
        void readCommunication();
    private:
        template <typename T> 
        String printVector(const T vectorToPrint[], size_t size);
        bool newData = false;
        CommunicationCallback callback_;
};