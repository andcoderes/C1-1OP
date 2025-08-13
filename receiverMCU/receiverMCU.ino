#include "BLController.h"
#include <ArduinoJson.h>
#include <string>
#include <queue>
#include "RcController.h"

#define TIMEOUT 200
long currentTime = millis();
uint32_t feetsTs = 0;
unsigned long lastMillis = 0;
bool bluetoothConnected = false;
std::string message = "";
auto blController = BlController();

void setup() {
  Serial.begin(9600);
  Serial0.begin(19200);
  Serial1.begin(9600, SERIAL_8N1, D6, D7);
  blController.setup();
  delay(2000); 
  Serial.println("Starting setup");
}

void sendMessageToBody(){
  if(millis() - currentTime > TIMEOUT){
    std::string text = "{\"o\":[]}";
    text = blController.getStringMessage();
    if(text != "" && text != "{\"o\":[]}"){
      Serial0.write(text.c_str());
      Serial.println("sending on normal serial");
      currentTime = millis();
    }
  }
}


void bluetoothLoop(){
  blController.loop();
  if(message != "" && (lastMillis == 0 || (millis() - lastMillis) >= 1000)){
    Serial.print("Sending message: ");
    Serial.println(message.c_str());
    blController.sendMessage(message);
    lastMillis = millis();
    message = "";
  }
}

void loop() {
  if(bluetoothConnected && !blController.isConnected()){
    Serial0.write("{\"o\":[{\"s\":-1, \"c\":0}]}");
    bluetoothConnected = false;
  }
  if(!bluetoothConnected && blController.isConnected()){
    Serial0.write("{\"o\":[{\"s\":-1, \"c\":1}]}");
    bluetoothConnected = true;
  }
  bluetoothConnected =  blController.isConnected();
  if(bluetoothConnected){
    bluetoothLoop();
  }
  sendMessageToBody(); 
  if(Serial0.available()){
    std::string data = std::string(Serial0.readStringUntil('\n').c_str());
    JsonDocument doc;
    DeserializationError err = deserializeJson(doc, data.c_str());
    if (err == DeserializationError::Ok) {
     message = data;
     lastMillis = millis();
    } else {
      Serial.println("error on deserialize");
    }
  }
}