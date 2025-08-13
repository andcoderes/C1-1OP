#include "Lader.h"

Lader::Lader(int laderPosition,  int numberPixels) : 
    position_{laderPosition}, 
    numberPixels_{numberPixels}, 
    laderLigth_{Adafruit_NeoPixel(numberPixels_, position_ , NEO_GRB + NEO_KHZ800)} {}

void Lader::setup(){
    laderLigth_.begin();
     Serial.begin(9600);

}

void Lader::bluethoothDisconectedAnimation(){
    long currentTime = millis();
    if(latestMillis_ == 0 || (currentTime - latestMillis_) >= 100){
        if(laderBottomDirection_){
            currentPixel = currentPixel + 15;
            if(currentPixel >= 240){
                laderBottomDirection_ = false;
                currentPixel = 250;
            }
        } else {
            currentPixel = currentPixel - 15;
            if(currentPixel <= 5){
                laderBottomDirection_ = true;
                currentPixel = 5;
            }
        }
        laderLigth_.fill(laderLigth_.Color(currentPixel, 0, 0));
        laderLigth_.show();
        latestMillis_ = currentTime;
    }
}

void Lader::changeColor(uint8_t r, uint8_t g, uint8_t b){
    if(r == 0 && g == 0 && b == 0){
        laderLigth_.fill(laderLigth_.Color(0, 0, 0));
        laderLigth_.show();
        return;
    }
    if(laderBottomDirection_){
        currentPixel++;
        if(currentPixel == numberPixels_){
        laderBottomDirection_ = false;
        currentPixel = numberPixels_ - 2;
        }
    } else {
        currentPixel--;
        if(currentPixel == -1){
        laderBottomDirection_ = true;
        currentPixel = 1;
        }
    }
    laderLigth_.fill(laderLigth_.Color(r - 10, g - 10, b - 10));
    laderLigth_.setPixelColor(currentPixel, laderLigth_.Color(r + 50, g + 50 ,  b + 50));
    laderLigth_.show();
}

void Lader::setConnectionStatus(bool connectionStatus){
    currentPixel = 0;
    bluethoothConnection_ = connectionStatus;
}

void Lader::standByAnimation(){
    long currentTime = millis();
    if(latestMillis_ == 0 || (currentTime - latestMillis_) >= 100){
        if(laderBottomDirection_){
            currentPixel++;
            if(currentPixel == numberPixels_){
            laderBottomDirection_ = false;
            currentPixel = numberPixels_ - 2;
            }
        } else {
            currentPixel--;
            if(currentPixel == -1){
            laderBottomDirection_ = true;
            currentPixel = 1;
            }
        }
        laderLigth_.fill(laderLigth_.Color(50,0,0));
        laderLigth_.setPixelColor(currentPixel, laderLigth_.Color(255, 0, 0));
        laderLigth_.show();
        latestMillis_ = currentTime;
    }
}

void Lader::standBy(){
    if(bluethoothConnection_){
        standByAnimation();
    } else {
        bluethoothDisconectedAnimation();
    }
   
}