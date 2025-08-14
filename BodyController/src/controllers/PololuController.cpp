#include "PololuController.h"

// Animation 0 take out the saw
// Animation 1 Take out the coms
// Animation 2 hide the saw
// Animation 3 Use the com 
// Animation 4 Hide Comms
// Animation 5 Take out body Arm
// Animation 6 play body Arm (idk if use it)
// Animation 7 Put Body Arm Back
// Animation 8 Starting Animation


PololuController::PololuController(int rx, int tx) : 
    maestroSerial(rx,tx), 
    miniMaestro(maestroSerial) {}

void PololuController::setup(){
    maestroSerial.begin(9600);
    delay(500);
}

void PololuController::stop(){
    Serial.println("Stopping script");
    miniMaestro.stopScript();
}

void PololuController::animate(std::vector<String> keys, std::vector<int> macros){
    String animation;
    int index = -1; 
    if((keys.size() == 1 || macros.size() == 1) && macros.size() != keys.size()){
        if(keys.size() > 0){
            animation = keys.at(0);
        } else {
            animation = String(macros.at(0));
        }
        for(unsigned int i = 0 ; i < arrayKeys.size() ; i++){
            if(animation == arrayKeys.at(i)){
                index = i;
            }
        }
        if(index != -1){
            miniMaestro.stopScript();
            Serial.print("Starting script: ");
            Serial.println(scripts[index][currentIndex[index]]);
            if(scripts[index][currentIndex[index]] == 100){
                miniMaestro.setTarget(5, 2000);
            }  else {
                miniMaestro.setTarget(5, 0);
               miniMaestro.restartScript(scripts[index][currentIndex[index]]);
            }
           if( currentIndex[index] + 1 == (signed int )scripts[index].size()){
            currentIndex[index] = 0;
           } else {
            currentIndex[index] = currentIndex[index] + 1;
           }
        }
    }
}