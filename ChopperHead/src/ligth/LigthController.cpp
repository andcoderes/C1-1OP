#include "ligth/LigthController.h"
#include <comunication/ComunicationController.h>

LigthController::LigthController() : 
    centerEye_{JewelEye(CENTER_EYE, NUMBER_EYE_PIXEL)}, 
    leftEye_{JewelEye(LEFT_EYE, NUMBER_EYE_PIXEL)}, 
    lader_{Lader(LADER_PIN, NUMBER_LADER_PIXEL)}, 
    pixelEye_{PixelEye(RIGHT_EYE)}, 
    periscope_{PixelEye(PERISCOPE_LED)}{}

void LigthController::setup(){
    leftEye_.setup();
    centerEye_.setup();
    lader_.setup();
    pixelEye_.setup();
    periscope_.setup();
    periscope_.setBlink(false);
    periscope_.off();
}

void LigthController::turOn(int eye){
    if(eye == PERISCOPE_LED){
        periscope_.on();
    }
}

void LigthController::turOff(int eye){
    if(eye == PERISCOPE_LED){
        periscope_.off();
    }
}

void LigthController::standBy(){
    if(currentRoutine == "107"){
        if(customAnimationStep == 0 ){
            leftEye_.changeColor(random(30, 190),random(30, 190),random(30, 190));
            centerEye_.changeColor(random(30, 190),random(30, 190),random(30, 190));
            lader_.changeColor(random(30, 190),random(30, 190),random(30, 190));
            pixelEye_.changeColor(random(30, 190),random(30, 190),random(30, 190));
            if(millis() - routineTime > 4000){
                customAnimationStep = 1;
            }
        }
        if(customAnimationStep == 1){
            leftEye_.changeColor(0, 0, 0);
            centerEye_.changeColor(0, 0, 0);
            lader_.changeColor(0,0,0);
            pixelEye_.changeColor(0, 0, 0);
            if(millis() - routineTime > 6000){
                customAnimationStep = 2;
            }
        }
        if(customAnimationStep == 2){
            leftEye_.changeColor(random(30, 190), 0, 0);
            centerEye_.changeColor(random(30, 190), 0, 0);
            lader_.changeColor(random(30, 190), 0, 0);
            pixelEye_.changeColor(random(30, 190), 0, 0);
        }
    } else {
        leftEye_.standBy();
        centerEye_.standBy();
        lader_.standBy();
        pixelEye_.standBy();
        periscope_.standBy();    
    }
  
    if(currentRoutine != "" && millis() - latestTime > limitTime){
        Serial.println("reboot elements");
        centerEye_.setDefaulColor();
        leftEye_.setDefaulColor();
        latestTime = millis();
        periscope_.off();
        currentRoutine = "";
        customAnimationStep = 0;
    }
}
void LigthController::animate(const ButtonsAction ba){
    if(ba.connectionStatus == 0){
        lader_.setConnectionStatus(false); 
    }
    if(ba.connectionStatus == 1){
        lader_.setConnectionStatus(true); 
    }

    if(ba.status == BUTTON_STATUS){
        if(ba.macro[0] == 101){
            currentRoutine = "101";
            latestTime = millis();
            for(int i = 0; i < 2; i++){
                if(macro_[i] == "101"){
                    limitTime = time_[i];
                }
            }
            Serial.println("Red eye");
            centerEye_.redEye();
            leftEye_.redEye();
        }
        if(ba.macro[0] == 103){
            currentRoutine = "103";
            latestTime = millis();
            for(int i = 0; i < 2; i++){
                if(macro_[i] == "103"){
                    limitTime = time_[i];
                }
            }
            periscope_.on();
        }
        if(ba.macro[0] == 107){
            currentRoutine = "107";
            routineTime = millis();
            latestTime = millis();
            for(int i = 0; i < 3; i++){
                if(macro_[i] == "107"){
                    limitTime = (unsigned long)time_[i];
                }
            }
        }
        if(ba.pb[0] == "du"){
            periscope_.swapLight();
        }
        
    }
}