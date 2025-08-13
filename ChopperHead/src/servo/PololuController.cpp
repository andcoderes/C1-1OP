#include "PololuController.h"

PololuController::PololuController(int rx, int tx) : 
    maestroSerial(rx,tx), 
    miniMaestro(maestroSerial) {}

void PololuController::setup(){
    maestroSerial.begin(9600);
    delay(500);
}

void PololuController::stop(){
    miniMaestro.stopScript();
}

void PololuController::animate(const String keys[], const int macros[], int size){
    String keysAction = "";
    for(unsigned int i = 0; i < size && keys[i] != ""; i++){  // Avoid out-of-bounds
        keysAction += keys[i];  // More efficient concatenation
    }
    if(keysAction != ""){
        Serial.println(keysAction);
        playScriptButton(keysAction);
        return;
    }
    String macrosString = "";
    for(unsigned int i = 0; i < size && macros[i] != 0; i++){ // Avoid out-of-bounds
        macrosString += String(macros[i]);  // Ensure conversion
    }

    if(macrosString != ""){
        Serial.println(macrosString);
        playScriptButton(macrosString);
    }
}

void PololuController::playScriptButton(String keysAction){
    int actionIndex = -1;
    for(int i = 0; i < 7; i++){
        if(arrayKeys_[i] == keysAction){
            actionIndex = i;
            break;
        }
    }
    if(actionIndex != -1){
        int currentIndex = currentIndex_[actionIndex];
        int scriptToRun  = scripts_[actionIndex][currentIndex];
        currentIndex = (currentIndex + 1) % 2; // Fix looping logic
        currentIndex_[actionIndex] = currentIndex;

        if(checkAnimation(scriptToRun)){
            miniMaestro.restartScript(scriptToRun);
        }
    }
}


void PololuController::execute(int scriptToRun){
    if(checkAnimation(scriptToRun)){
        miniMaestro.restartScript(scriptToRun);        
    }
}

bool PololuController::checkAnimation(int animation){
    if(animation == outHandAnimation_){
        outComponent_[0] = true;  // Fixed assignment
        return true;
    }
    if(outComponent_[0] == true){
        if(animation == inHandAnimation_){
            outComponent_[0] = false; // Fixed assignment
            return true;
        }
        
        for(int x = 0; x < 6; x++){
            if(animationRequieredHandOut_[x] == animation){
                return true;
            }
        }
    } 
    if(animation == outPeriscopeAnimation_){
        outComponent_[1] = true; // Fixed assignment
        return true;
    }
    if(outComponent_[1] == true){
        if(animation == inPeriscopeAnimation_){
            outComponent_[1] = false; // Fixed assignment
            return true;
        }
        for(int i = 0; i < 2; i++){
            if(animationRequieredPeriOut_[i] == animation){
                return true;
            }
        }
    }
    return false;
}
