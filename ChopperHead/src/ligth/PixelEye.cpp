#include "PixelEye.h"

PixelEye::PixelEye(int eyePosition) : 
    eyePosition_{eyePosition}, 
    neoEye_{Adafruit_NeoPixel(1, eyePosition,  NEO_GRB)} {}

 void PixelEye::setBlink(bool shouldBlink){
  shouldBlink_ = shouldBlink;
 }

 void PixelEye::off(){
  onLigth = false;
  neoEye_.setPixelColor(0, neoEye_.Color(0, 0, 0));
  neoEye_.show();
 }
 void PixelEye::swapLight(){
  if(onLigth){
    off();
  } else {
    on();
  }
 }

void PixelEye::changeColor(uint8_t r, uint8_t g, uint8_t b){
  neoEye_.setPixelColor(0, neoEye_.Color(r, g, b));
  neoEye_.show();
}

void PixelEye::on(){
  onLigth = true;
  neoEye_.setPixelColor(0, neoEye_.Color(255, 0, 0));
  neoEye_.show();
 }

void PixelEye::setup(){
    neoEye_.begin();
}

void PixelEye::standBy(){
  long currentTime = millis();
  if(onLigth && (latestMillis_ == 0 || (currentTime - latestMillis_) >= 50)){
    int changeStatusRigth = random(100);
    if(changeStatusRigth < 20){
      standByRigthTurnOn_ = !standByRigthTurnOn_;
    }
    if(shouldBlink_ && !standByRigthTurnOn_){
      neoEye_.setPixelColor(0, neoEye_.Color(0, 0, 0));
    } else {
      neoEye_.setPixelColor(0, neoEye_.Color(255, 0, 0));
    }
    neoEye_.show();
    latestMillis_ = currentTime;
  }
}