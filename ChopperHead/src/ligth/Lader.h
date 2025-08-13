#pragma once 
#include <Arduino.h>
#include <Adafruit_NeoPixel.h>


#define ORIGINAL_R 0
#define ORIGINAL_G 0
#define ORIGINAL_B  40
#define ORIGINAL_DELTA_R 0
#define ORIGINAL_DELTA_G 0
#define ORIGINAL_DELTA_B 100


class Lader{
    public:
      Lader(int laderPosition, int numberPixels);
      void setup();
      void standBy();
      void changeColor(uint8_t r, uint8_t g, uint8_t b);
      void setConnectionStatus(bool connectionStatus);
    private:
      int position_;
      int numberPixels_;
      int currentPixel = 0;
      bool laderBottomDirection_ = true;
      Adafruit_NeoPixel laderLigth_;
      unsigned long latestMillis_;
      bool bluethoothConnection_ = false;
      void bluethoothDisconectedAnimation();    
      void standByAnimation();    
};