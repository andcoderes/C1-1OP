#pragma once
#include <Arduino.h>
#include "Communication.h"
#include <ArduinoJson.h>
#include <memory>

#define ARDUINO_STATUS -1
#define BUTTON_STATUS 0
#define MOVEMENT_STATUS 1
#define SETTINGS_STATUS 2
#define PLAY_AUDIO_STATUS 3

class ComunicationController{ 
    public:
        bool setup();
        std::vector<CommunicationReceived> readCommunication();
        void updateAudio(int currentAudio);
    private:
        template <typename T> 
        std::vector<T> jsonArrayToVector(JsonArray json);
        template <typename T> 
        String printVector(std::vector<T> vectorToPrint);
        bool newData = false;
};