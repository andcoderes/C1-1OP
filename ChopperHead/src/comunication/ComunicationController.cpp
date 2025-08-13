#include "comunication/ComunicationController.h"

bool ComunicationController::setup() {
    Serial.begin(9600);
    Serial1.begin(19200);
    Serial1.setTimeout(10);
    return true;  // Ensure function actually returns a value
}

void ComunicationController::setCallback(CommunicationCallback callback) {
    callback_ = callback;
}

void ComunicationController::readCommunication() {                                                                    
    if (Serial1.available()) { 
        JsonDocument doc;
        String jsonText = Serial1.readStringUntil('\0');

        DeserializationError err = deserializeJson(doc, jsonText);
        if (err != DeserializationError::Ok) {
            Serial.print("Error on deserialization: ");
            Serial.println(err.c_str());  // Print actual error message
            Serial.print("Received JSON: ");
            Serial.println(jsonText);
            return;
        }

        Serial.println(jsonText);
        JsonArray actions = doc["o"];

        for (unsigned int i = 0; i < actions.size(); i++) {
            ButtonsAction ba;
            int status = actions[i]["s"];
            ba.status = status;   

            if (status == ARDUINO_STATUS) {
                ba.connectionStatus = actions[i]["c"].as<int>();      
            }
            else if (status == BUTTON_STATUS || status == DEBUG_STATUS) {
                JsonArray buttons = actions[i]["p"];
                JsonArray macros = actions[i]["m"];
                // Fill up `pb` array with up to 20 buttons
                for (unsigned int j = 0; j < buttons.size() && j < 20; j++) {
                    ba.pb[j] = buttons[j].as<String>();
                }

                // Fill up `macro` array with up to 20 macros
                for (unsigned int j = 0; j < macros.size() && j < 20; j++) {
                    ba.macro[j] = macros[j].as<int>();  // Assuming it's an integer
                }
            }

            if (callback_) {
                callback_(ba);
            }
        }
    }
}

template <typename T>
String ComunicationController::printVector(const T vectorToPrint[], size_t size) {
    String returnText = "[";
    
    for (size_t i = 0; i < size; i++) {
        returnText += " " + String(vectorToPrint[i]);
        if (i < size - 1) {
            returnText += ",";
        }
    }
    
    returnText += " ]";
    return returnText;
}
