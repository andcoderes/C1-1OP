#pragma once
#include <Arduino.h>
#include <PololuMaestro.h>
#include <SoftwareSerial.h>

// Animation 0 take out arms
// Animation 1 put arms back
// Animation 2 Hand macro
// Animation 3 say hi
// Animation 4 move hand side to side
// Animation 5 take out left arm
// Animation 6 put left arm back
// Animation 7 take out rigth arm
// Animation 8 put rigth arm back
// Animation 9 periscope out
// Animation 10 periscope move
// Animation 11 Periscope in
// Animation 12 Periscope macro


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
        int scripts_[7][2] = {{0,1},{4,4}, {2,3}, {7,6}, {5,5}, {9,9}, {8,8}};
        SoftwareSerial maestroSerial;
        MiniMaestro miniMaestro;
        bool outComponent_ [2] = {false, false};
        int animationRequieredHandOut_[6] = {1,2,3,4,5,9};
        int animationRequieredPeriOut_[2] = {7,8};
        int outHandAnimation_ = 0;
        int inHandAnimation_ = 1;
        int inPeriscopeAnimation_ = 6;
        int outPeriscopeAnimation_ = 7;
        void playScriptButton(String keysAction);
        bool checkAnimation(int animation); 
};