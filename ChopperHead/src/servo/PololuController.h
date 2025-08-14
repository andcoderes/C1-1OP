#pragma once
#include <Arduino.h>
#include <PololuMaestro.h>
#include <SoftwareSerial.h>

// Animation 0 take out arms
// Animation 1 put arms back
// Animation 2 say hi
// Animation 3 say hi left arm
// Animation 4 Persicope out
// Animation 5 Periscope in
// Animation 6 Periscope animation


class PololuController{ 
    public:
        PololuController(int rx, int tx);
        void setup();
        void animate(const String keys[], const int macros[], int size);
        void execute(int macros);
        void stop();
    private:
        String arrayKeys_[7] = {"y", "x", "b", "du", "dd", "dr", "dl"};
        int currentIndex_[7] = {0, 0, 0, 0, 0, 0, 0};
        int scripts_[7][2] = {{0,1},{2,2}, {3,3}, {4,4}, {5,5}, {6,6}, {6,6}};
        SoftwareSerial maestroSerial;
        MiniMaestro miniMaestro;
        bool outComponent_ [2] = {false, false};
        int animationRequieredHandOut_[3] = {1,2,3};
        int outHandAnimation_ = 0;
        int inHandAnimation_ = 1;
        int inPeriscopeAnimation_ = 6;
        int outPeriscopeAnimation_ = 7;
        void playScriptButton(String keysAction);
        bool checkAnimation(int animation); 
};