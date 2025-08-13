#pragma once 
#include <Arduino.h>
#include "comunication/ButtonsAction.h"
#include "ligth/JewelEye.h"
#include "ligth/Lader.h"
#include "ligth/PixelEye.h"

#define CENTER_EYE 3
#define RIGHT_EYE 5
#define LEFT_EYE 4
#define PERISCOPE_LED 6
#define LADER_PIN 7

#define NUMBER_EYE_PIXEL 7
#define NUMBER_LADER_PIXEL 11


class LigthController{
    public:
        LigthController();
        void setup();
        void standBy();
        void animate(const ButtonsAction ba);
        void turOn(int eye);    
        void turOff(int eye);    
    private: 
        String macro_[3] = {"101", "103", "107"};
        int time_[3] = {2450, 15500, 8300};
        unsigned long latestTime = 0;
        unsigned long limitTime = 0;
        unsigned long routineTime = 0;
        int customAnimationStep = 0;
        String currentRoutine = "";
        JewelEye centerEye_;
        JewelEye leftEye_;
        Lader lader_;
        PixelEye pixelEye_;
        PixelEye periscope_;
};