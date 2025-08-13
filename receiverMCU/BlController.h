#include <Arduino.h>
#include <BLEServer.h>
#include <BLEDevice.h>
#include <BLEUtils.h>
#include <string>

#define SERVICE_UUID        "96e3f2cd-28cf-4d37-9b39-a291f917620e"
#define CHARACTERISTIC_UUID "9c5c0655-5501-4c0d-a91b-defbd9dea110"
#define STOP_TIMEOUT 300

class BlController: public BLECharacteristicCallbacks, BLEServerCallbacks {
    public:
        void onWrite(BLECharacteristic *pCharacteristic);
        void onConnect(BLEServer* pServer);
        void onDisconnect(BLEServer* pServer);
        void sendMessage(const std::string& message);
        std::string getStringMessage();
        std::string getMovementMessage();
        void loop();
        void setup();
        bool isConnected();
        bool shouldStartBubbles();
    private:
        BLEServer *pServer = nullptr;
        BLEService *pService = nullptr;
        bool connected = false;
        BLECharacteristic *globalCharacteristic;
        std::string lastMovementMessageReceived = "";
        std::string lastButtonMessageReceived = "";
        std::string lastSettingsMessageReceived = "";
};
