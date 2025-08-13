#include "ComunicationController.h"
#include <memory>
#include <vector>

bool ComunicationController::setup(){
    Serial.begin(9600);
    Serial1.begin(19200);
    Serial2.begin(19200);
    Serial1.setTimeout(5);
}

std::vector<CommunicationReceived> ComunicationController::readCommunication() {
    std::vector<CommunicationReceived> communication;
    if(Serial1.available()){ 
        JsonDocument doc;
        String jsonText = Serial1.readStringUntil('\0');
        Serial.println(jsonText);
        DeserializationError err = deserializeJson(doc, jsonText);
        if (err == DeserializationError::Ok) 
        {
            Serial2.write(jsonText.c_str());
            JsonArray actions = doc["o"];
            for(unsigned int i = 0 ; i < actions.size(); i++) {
                CommunicationReceived coms;
                int status = actions[i]["s"];
                coms.status = status;
                if(status == ARDUINO_STATUS){
                    coms.status = status;                        
                    coms.connectionStatus = actions[i]["c"].as<int>();
                    communication.push_back(coms);
                }
                if(status == BUTTON_STATUS || status == PLAY_AUDIO_STATUS) {
                    coms.pb = jsonArrayToVector<String>(actions[i]["p"]);
                    coms.macro = jsonArrayToVector<int>(actions[i]["m"]);
                    communication.push_back(coms);
                }
                if(status == MOVEMENT_STATUS) {
                    JsonArray lAnalog = actions[i]["l"];
                    JsonArray rAnalog = actions[i]["r"];
                    coms.status = status;
                    coms.lx = lAnalog.size() == 0 ? 0 : lAnalog[0];
                    coms.ly = lAnalog.size() == 0 ? 0 : lAnalog.size() > 1 ? lAnalog[1] : 0;    
                    coms.rx = rAnalog.size() == 0 ? 0 : rAnalog[0];
                    coms.ry = rAnalog.size() == 0 ? 0 : rAnalog.size() > 1 ? rAnalog[1] : 0;                 
                    communication.push_back(coms);
                }
                if(status == SETTINGS_STATUS) {
                    coms.status = status;
                    coms.v = actions[i]["v"].as<int>();
                    coms.ea = actions[i]["ea"].as<bool>();
                    communication.push_back(coms);
                }
            }
        }
        else 
        {
            Serial.print("Error on deserializate => ");
            Serial.println(jsonText);
        }
    }
    return communication;
}
void ComunicationController::updateAudio(int currentAudio) {
    String s = "{\"s\":-1, \"v\":";
    s.concat(currentAudio);
    s.concat("}");
    Serial.print(s);
    Serial1.println(s);
}
template <typename T> 
std::vector<T> ComunicationController::jsonArrayToVector(JsonArray json){
    std::vector<T> vectorReturn;
    for(int i = 0 ; i < json.size() ; i++){
        vectorReturn.push_back(json[i]);
    }
    return vectorReturn;
}
template <typename T> 
String ComunicationController::printVector(std::vector<T> vectorToPrint){
    String returnText = "[";
    for(int i = 0 ; i < vectorToPrint.size() ; i++){
        returnText = returnText + " " + vectorToPrint[i];
        if( i < vectorToPrint.size() -1){
            returnText = returnText + ",";
        }
    }
    returnText = returnText + "]";
    return returnText;
}