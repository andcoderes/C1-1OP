#pragma once
#include <Arduino.h>
#include <vector>

// Animation 99 bubbles
class ButtonController{ 
    public:
        ButtonController();
        void setup();
        void stop();
        void update(const std::vector<String>& keys);
        void loop();
    private:
        const int kBubbleId = 12;
        bool kBubbleRunning = false;
        void toggleBubble();

};