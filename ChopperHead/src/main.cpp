#include <Arduino.h>
#include "ligth/LigthController.h"
#include "servo/PololuController.h"
#include "comunication/ComunicationController.h"


LigthController eyeController = LigthController();
PololuController pololuController = PololuController(10,11);
ComunicationController coms = ComunicationController();


void communicationCallback(const ButtonsAction ba)
{ 
  if(ba.status == BUTTON_STATUS){
    Serial.println("button status");
    pololuController.animate(ba.pb, ba.macro, 20);
    eyeController.animate(ba);
  }
  if(ba.status == ARDUINO_STATUS){
    eyeController.animate(ba);
  }
  if(ba.status == DEBUG_STATUS){
    pololuController.execute(ba.macro[0]);
  }
}

void setup() {
  eyeController.setup();
  pololuController.setup();
  coms.setup();
  coms.setCallback(communicationCallback);
}

void loop() {
  eyeController.standBy();
  coms.readCommunication();
}

