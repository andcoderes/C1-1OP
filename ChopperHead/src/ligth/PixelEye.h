#pragma once 
#include <Arduino.h>
#include <Adafruit_NeoPixel.h>


#define ORIGINAL_R 0
#define ORIGINAL_G 0
#define ORIGINAL_B  40
#define ORIGINAL_DELTA_R 0
#define ORIGINAL_DELTA_G 0
#define ORIGINAL_DELTA_B 100


class PixelEye{
    public:
      PixelEye(int eyePosition);
      void setBlink(bool shouldBlink);
      void setup();
      void standBy();
      void changeColor(uint8_t r, uint8_t g, uint8_t b);
      void off();
      void on();
      void swapLight();
    private:
        int eyePosition_;
        Adafruit_NeoPixel neoEye_;
        unsigned long latestMillis_;
        bool standByRigthTurnOn_ = false;
        bool shouldBlink_ = true;
        bool onLigth = true;
};