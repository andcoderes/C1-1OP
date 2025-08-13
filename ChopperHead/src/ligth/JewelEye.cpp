#include "JewelEye.h"

JewelEye::JewelEye(int eyePosition,  int numberPixels) : 
    eyePosition_{eyePosition}, 
    numberPixels_{numberPixels}, 
    neoEye_{Adafruit_NeoPixel(numberPixels_, eyePosition,  NEO_RGBW)} {}

void JewelEye::setup(){
    neoEye_.begin();
}

void JewelEye::setDefaulColor(){
    baseR = ORIGINAL_R;
    baseG = ORIGINAL_G;
    baseB = ORIGINAL_B;
}

void JewelEye::redEye(){
    baseR = 40;
    baseG = 0;
    baseB = 0;
}

void JewelEye::changeColor(uint8_t r, uint8_t g, uint8_t b){
    for(int i = 0; i < 5 ; i++){
       neoEye_.setPixelColor(random(0, numberPixels_), neoEye_.Color(g, r, b));
    }
    neoEye_.show();
}

void JewelEye::standBy(){
    long currentTime = millis();
    if(latestMillis_ == 0 || (currentTime - latestMillis_) >= 50){
        neoEye_.fill(neoEye_.Color(baseG,baseR,baseB));
        for(int i = 0; i < 3 ; i++){
            int deltaG = baseG == 0 ? 0 : ORIGINAL_G + 60;
            int deltaR = baseR == 0 ? 0 : ORIGINAL_R + 60;
            int deltaB = baseB == 0 ? 0 : ORIGINAL_B + 60;
            neoEye_.setPixelColor(random(0, numberPixels_), neoEye_.Color(deltaG, deltaR, deltaB));
        }
        neoEye_.show();
        latestMillis_ = millis();
    }
} 