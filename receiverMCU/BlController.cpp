#include "BLController.h"
#include <ArduinoJson.h>

void BlController::setup(){
    BLEDevice::init("Chopper Droid");
    BLEDevice::setMTU(128);
    pServer = BLEDevice::createServer();
    pServer->setCallbacks(this);

    pService = pServer->createService(SERVICE_UUID);
    globalCharacteristic = pService->createCharacteristic(
                                            CHARACTERISTIC_UUID,
                                            BLECharacteristic::PROPERTY_READ |
                                            BLECharacteristic::PROPERTY_WRITE | 
                                            BLECharacteristic::PROPERTY_NOTIFY |
                                            BLECharacteristic::PROPERTY_INDICATE
                                        );
    globalCharacteristic->setCallbacks(this);
    pService->start();
    BLESecurity *pSecurity = new BLESecurity();
    pSecurity->setAuthenticationMode(ESP_LE_AUTH_REQ_SC_MITM_BOND);
    pSecurity->setCapability(ESP_IO_CAP_NONE);
    pSecurity->setInitEncryptionKey(ESP_BLE_ENC_KEY_MASK | ESP_BLE_ID_KEY_MASK);
    BLEAdvertising *pAdvertising = pServer->getAdvertising();
    pAdvertising->addServiceUUID(SERVICE_UUID);
    pAdvertising->setScanResponse(true);
    pAdvertising->setMinPreferred(0x12);
    BLEDevice::startAdvertising();
    Serial.println("look in the  phone");
}


void BlController::onWrite(BLECharacteristic *pCharacteristic) {
    std::string value = pCharacteristic->getValue();
    if (value.length() > 0) {
        JsonDocument doc;
        Serial.println(value.c_str());
        DeserializationError err = deserializeJson(doc, value.c_str());
        if (err != DeserializationError::Ok) 
        {
            Serial.println("ERROR JSON");
        }
        else {
  //          lastMillisecondMovement = millis();
            int status = doc["s"].as<int>();
            switch(status){
            case 3:
                lastButtonMessageReceived = value;
              break;
            case 0:
                lastButtonMessageReceived = value;
            break;
            case 1:
                serializeJson(doc["l"], Serial1);
                Serial1.write('\n');
                lastMovementMessageReceived = value;
                
            break;
            case 2:
                lastSettingsMessageReceived = value;
            break;
            
            }
        }
    }
}

void BlController::onConnect(BLEServer* pServer){
    pServer->getAdvertising()->stop();
    Serial.println("Connect");
    connected = true;
    if(globalCharacteristic){
        globalCharacteristic->setValue("test string ");
    } else {
        Serial.println("not writting data");
    }
}

void BlController::onDisconnect(BLEServer* pServer){
    Serial.println("Disconnect");
    Serial1.write("off");
    Serial1.write('\n');
    BLEAdvertising *pAdvertising = pServer->getAdvertising();
    connected = false;
    pAdvertising->start();
}

bool BlController::isConnected(){
    return connected;
}

void BlController::loop(){
}

void BlController::sendMessage(const std::string& message){
  Serial.println(message.c_str());
  globalCharacteristic->setValue(message);
  globalCharacteristic->notify(true);
}

std::string BlController::getStringMessage(){
    std::string text = "{\"o\":[";
    bool previous = false;
    if(lastMovementMessageReceived != ""){
        text = text + lastMovementMessageReceived;
        lastMovementMessageReceived = "";
        previous = true;
    }
    if(lastButtonMessageReceived != ""){
        if(previous){
            text = text + ",";
        }
        text = text + lastButtonMessageReceived;
        lastButtonMessageReceived = "";
        previous = true;
    }
    if(lastSettingsMessageReceived != ""){
        if(previous){
            text = text + ",";
        }
        text = text +  lastSettingsMessageReceived;
        lastSettingsMessageReceived = "";
    }
    text = text + "]}";
    return text;
}

std::string BlController::getMovementMessage(){
    std::string text = "{\"o\":[";
    bool previous = false;
    if(lastMovementMessageReceived != ""){
        text = text + lastMovementMessageReceived;
        lastMovementMessageReceived = "";
    }
    text = text + "]}";
    return text;
}
