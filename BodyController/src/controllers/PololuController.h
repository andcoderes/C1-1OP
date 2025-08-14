#pragma once
#include <Arduino.h>
#include <map>
#include <PololuMaestro.h>
#include <SoftwareSerial.h>
#include <vector>

// Animation 0 take out the saw
// Animation 1 Take out the coms
// Animation 2 animation head
// Animation 3 hide the saw
// Animation 4 Use the com 
// Animation 5 Hide Comms
// Animation 6 Take out body Arm
// Animation 7 play body Arm (idk if use it)
// Animation 8 Put Body Arm Back
// Animation 9 Starting Animation
// Animation 10 Bubble Head
class PololuController{ 
    public:
        PololuController(int rx, int tx);
        void setup();
        void animate(std::vector<String> keys, std::vector<int> macros);
        void stop();
    private:
        std::vector<String> arrayKeys = {"l1", "r1", "a", "102", "107"};
        std::vector<int> currentIndex = {0, 0, 0, 0, 0};
        std::vector<std::vector<int>> scripts = {{0,1,2}, {3,100,4}, {5,6}, {7}, {8}};
        SoftwareSerial maestroSerial;
        MiniMaestro miniMaestro;

};