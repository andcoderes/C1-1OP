#include <string>
#include "RcController.h"

void RcController::setup(int rx, int tx){
    hs.begin(115200, SERIAL_8N1, rx, tx);
    fillListChannels();
    int x = rcValue[1]; 
    int y = rcValue[0]; 
}

std::string RcController::readControllerValue(){
  std::map<int, uint16_t> listsChannels = fillListChannels();
  if(firstRead && listsChannels.size() > 0){
    firstRead = false;
    return "{\"o\":[]}";
  }
  std::string data = "{\"o\":[";
  int head_movement = 0;
  bool addedPreviousValue = false;
  for(auto const& updatedChannel : listsChannels){
    int value = updatedChannel.second;
    // 1 Rigth Vertical 
    // 0 Rigth Horizontal
    switch(updatedChannel.first){
      case 0:
        xDeltaValue = normalizeAnalogValue(map(updatedChannel.second, 1012, 1952, -100, 100));
        if(xDeltaValue <= 4 && xDeltaValue >= -4){
            xDeltaValue = 0;
        }
      break;
      case 1:
        yDeltaValue = normalizeAnalogValue(map(updatedChannel.second, 1043, 1998, -100, 100));
        if(yDeltaValue >= -6 && yDeltaValue <= 4){
          yDeltaValue = 0;
        }
      break;
      case 3:
        xHeadDeltaValue_ = normalizeAnalogValue(map(updatedChannel.second, 1085 , 1988, -100, 100));            
        if(xHeadDeltaValue_ >= -8 && xHeadDeltaValue_ <= 3){
          xHeadDeltaValue_ = 0;
        }
      break;
      case 6:
        setState(updatedChannel.second);
      break;
      case 9:
        value = getVolumenValue(value);
        data = addStringToData(data, "{\"s\":2,\"i\":false, \"v\": " + std::to_string(value) + "}", addedPreviousValue);
        addedPreviousValue = true;
      break;
      case 2:
      case 8:
      break;
      default:
        if(updatedChannel.second < 1400 || updatedChannel.second > 1600){
          std::string textElement = "";
          if(valueString.find(updatedChannel.first) != valueString.end()){
            textElement = valueString.at(updatedChannel.first)[currentChannel_];
            Serial.println(" Value found");
          } else {
            Serial.println(" Value not found");
          }
          if(textElement != ""){
            data = addStringToData(data, textElement, addedPreviousValue);
            addedPreviousValue = true;
          }
        }       
      break;
    }


  }
  if(xDeltaValue != xValue_ || yDeltaValue != yValue_ || xHeadDeltaValue_ != xHeadValue_ ){
    xValue_ = xDeltaValue;
    yValue_ = yDeltaValue;
    xHeadValue_ = xHeadDeltaValue_;
    std::string movementText = "{\"s\":1, \"l\":[" + std::to_string(xValue_) +"," + std::to_string(yValue_) + "], \"r\":[" + std::to_string(xHeadValue_) + ",0], \"lr\":[0,0]}";
    data = addStringToData(data, movementText, addedPreviousValue);
  }
  
  if(data != "{\"o\":["){
    //  Serial.println(data.c_str());
  }
  data = data + "]}";
  return data;
}

std::string RcController::addStringToData(const std::string &data, const std::string &currentString, bool previousAdded){
    std::string currentData = data;
    if(previousAdded){
        currentData = currentData + ",";
    } 
    currentData = currentData + currentString;
    return currentData;
}

int RcController::normalizeAnalogValue(int value){
    if(value < -100){
        value = -100;
    } 
    if(value > 100){
        value = 100;
    }
    return value;
}

void RcController::setState(int value){
    switch (value)
    {
        case 1000:
            currentChannel_ = 0;
        break;
        case 1500:
            currentChannel_ = 1;
        break;
        case 2000:
            currentChannel_ = 2;
        break;
    }
}

int RcController::getVolumenValue(int data){
    int value = map(data, 1000, 2000, 0, 255);
    if(value > 255){
        value = 255;
    } 
    if(value < 0){
        value = 0;
    }
    return value;
}

std::map<int,uint16_t> RcController::fillListChannels(){
    std::map<int,uint16_t> mapData;
    if(hs.available()){
        uint8_t val = hs.read();
         // Look for 0x2040 as start of packet
        if (ibusIndex == 0 && val != 0x20)
        {
            ibusIndex = 0;
            return mapData;
        }
        if (ibusIndex == 1 && val != 0x40)
        {
            ibusIndex = 0;
            return mapData;
        }

        if (ibusIndex == IBUS_BUFFSIZE)
        {
          ibusIndex = 0;
          int high=3;
          int low=2;
          for(int i=0;i<IBUS_MAXCHANNELS; i++)
          {
            uint16_t currentValue = (ibus[high] << 8) + ibus[low];
            if(abs(currentValue - rcValue[i]) > 5){
                mapData[i] = currentValue;
            }
            rcValue[i] = currentValue;
            high += 2;
            low += 2;
          }
        }
        else
        {
            ibus[ibusIndex] = val;
            ibusIndex++;
        }
    }
    return mapData;
}