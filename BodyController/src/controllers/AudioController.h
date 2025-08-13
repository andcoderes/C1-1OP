#pragma once
#include <Arduino.h>
#include <SoftwareSerial.h>
#include <vector>

class AudioController{
    public:
        AudioController(int rx, int tx);
        void setup();
        void playSound(std::vector<String> keys, std::vector<int> macros);
        void setVolumen(int volumen);
        int getVolumen();
        void play(int audio);
        void enableAudio(bool enabledAudio);
    private:
        bool enabledAudio_ = true;
        unsigned long latestMillis = 0;
        unsigned long startupTarget = 2000;
        int startingAudio = 8;
        SoftwareSerial audioSerial;
        std::vector<String> arrayKeys = {"-1", "100", "101", "102", "103", "104", "107"};
        std::vector<int> songs = {8, 13, 13, 16, 28, 21, 1};
        std::vector<int> durationMillis = {1000, 2350, 2450, 2500, 15500, 6200, 8200};
        int volumen_ = 50;
};