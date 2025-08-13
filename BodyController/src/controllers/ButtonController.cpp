#include "ButtonController.h"

// Animation 99 Bubbles


ButtonController::ButtonController() {}

void ButtonController::setup(){
    pinMode(kBubbleId, OUTPUT);
}

void ButtonController::stop(){
   digitalWrite(kBubbleId, LOW);
}

void ButtonController::loop(){
    digitalWrite(kBubbleId, kBubbleRunning ? HIGH : LOW);
}

void ButtonController::toggleBubble(){
    kBubbleRunning = !kBubbleRunning;    
}

void ButtonController::update(const std::vector<String>& keys){
    Serial.println("Calling update");
    if(keys.size() > 0 && keys.at(0) == "99"){
        toggleBubble();        
    }
}