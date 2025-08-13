#pragma once 
#include <Arduino.h>
#include <Adafruit_NeoPixel.h>


#define ORIGINAL_R 0
#define ORIGINAL_G 0
#define ORIGINAL_B  40
#define ORIGINAL_DELTA_R 0
#define ORIGINAL_DELTA_G 0
#define ORIGINAL_DELTA_B 100


class JewelEye{
    public:
      JewelEye(int eyePosition, int numberPixels);
      void setup();
      void standBy();
      void changeColor(uint8_t r, uint8_t g, uint8_t b);
      void setDefaulColor();
      void redEye();
    private:
        int eyePosition_;
        int numberPixels_;
        Adafruit_NeoPixel neoEye_;
        unsigned long latestMillis_;
        int baseR = ORIGINAL_R;
        int baseG = ORIGINAL_G;
        int baseB = ORIGINAL_B;
};