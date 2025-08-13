#include "HeadController.h"

HeadController::HeadController(int pinLeft, int pinRight, int pinAcceleration) : 
    kPinLeft{pinLeft},
    kPinRight{pinRight},
    kPinAcceleration{pinAcceleration} {}

void HeadController::setup(){
  pinMode(kPinLeft, OUTPUT);
  pinMode(kPinRight, OUTPUT);
  pinMode(kPinAcceleration, OUTPUT);
}

void HeadController::stop(){
    moveHead(0);
}

void HeadController::update(){
    if(macroRunning && millis() - lastCurrent >= whenToStop ) {
        digitalWrite(kPinRight, LOW);
        digitalWrite(kPinLeft, LOW);
        analogWrite(kPinAcceleration, 0);
        macroRunning = false;
    }
}

void HeadController::animate(std::vector<int> macros){
    if(macros[0] == 100 || macros[0] == 101){
        int index = -1; 
        for(unsigned int i = 0 ; i < macroId.size(); i++){
            if(macroId[i] == macros[0]){
                index = i;
            }
        }
        whenToStop = timesToStop[index];
        digitalWrite(kPinRight, macros[0] == 100 ? LOW : HIGH);
        digitalWrite(kPinLeft, macros[0] == 100 ? HIGH : LOW);
        analogWrite(kPinAcceleration, 255);
        lastCurrent = millis();
        macroRunning = true;
    }
    
}

void HeadController::moveHead(int rx){
    macroRunning = 0;
    if(abs(rx) < 3){
        rx = 0;        
    }
    float acceleration = map(abs(rx),0,100,0,255);
    if(rx != 0){
        digitalWrite(kPinRight, rx < 0 ? HIGH : LOW);
        digitalWrite(kPinLeft, rx < 0 ? LOW: HIGH);
        analogWrite(kPinAcceleration, acceleration);
    }
    else {
        digitalWrite(kPinRight, LOW);
        digitalWrite(kPinLeft, LOW);
        analogWrite(kPinAcceleration, 0);
    }
}