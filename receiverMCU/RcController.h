#include <string>
#include <vector>
#include <map>
#include <memory>
#include <Arduino.h>
#include <HardwareSerial.h>


#define IBUS_BUFFSIZE 32    
#define IBUS_MAXCHANNELS 10 

static uint8_t ibusIndex = 0;
static uint8_t ibus[IBUS_BUFFSIZE] = {0};
static uint16_t rcValue[IBUS_MAXCHANNELS];

class RcController {
    public:
        void setup(int rx, int tx);
        std::string readControllerValue(); 
    private:
        std::map<int,uint16_t> fillListChannels();
        std::string transformValueToJsonString();
        int getVolumenValue(int data);
        int normalizeAnalogValue(int data);
        void setState(int data);
        std::string addStringToData(const std::string &data, const std::string &currentString, bool previousAdded);
        HardwareSerial hs = HardwareSerial(1);
        bool firstRead = true;
        int xValue_ = 0;
        int yValue_ = 0;
        int xHeadValue_ = 0;
        int xDeltaValue = 0; 
        int yDeltaValue = 0; 
        int xHeadDeltaValue_ = 0;
        int currentChannel_ = 0;
        std::map<int, std::vector<std::string>> valueString = {
            {4, {"{\"s\":0,\"p\":[\"y\"],\"m\":[]}","{\"s\":0,\"p\":[\"a\"],\"m\":[]}","{\"s\":0,\"p\":[\"a\"],\"m\":[l2]}"}},
            {5, {"{\"s\":0,\"p\":[\"l1\"],\"m\":[]}","{\"s\":0,\"p\":[\"r1\"],\"m\":[]}", "{\"s\":0,\"p\":[],\"m\":[103]}"}},
            {7, {"{\"s\":0,\"p\":[],\"m\":[104]}","{\"s\":0,\"p\":[\"r1\"],\"m\":[105]}", "{\"s\":0,\"p\":[],\"m\":[17]}"}},
        };
};