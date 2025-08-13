#include <Arduino.h>
#include <ArduinoJson.h>
#include <StandardCplusplus.h>
#include <ArduinoJson.h>
#include "controllers/AudioController.h"
#include "controllers/ButtonController.h"
#include "controllers/Communication.h"
#include "controllers/ComunicationController.h"
#include "controllers/HeadController.h"
#include "controllers/PololuController.h"

#define MOTOR_HEAD_LEFT 7
#define MOTOR_HEAD_RIGHT 8
#define HEAD_ACCELERATION 9
#define POLOLU_RX 10
#define POLOLU_TX 11
#define AUDIO_RX 6
#define AUDIO_TX 5
bool isStarted = false;


AudioController ac(AUDIO_RX, AUDIO_TX);
ButtonController bc;
ComunicationController coms;
HeadController hc(MOTOR_HEAD_LEFT, MOTOR_HEAD_RIGHT, HEAD_ACCELERATION);
PololuController pc(POLOLU_RX, POLOLU_TX);

void setup() {
  // put your setup code here, to run once:
  coms.setup();
  bc.setup();
  hc.setup();
  pc.setup();
  ac.setup();
}

void loop() {
  if(!isStarted){
    ac.play(4);
    isStarted = true;
  }
  std::vector<CommunicationReceived> com = coms.readCommunication();
  if(com.size() > 0){
    for(unsigned int i = 0 ; i < com.size() ; i++){
      switch (com[i].status)
      {
        case ARDUINO_STATUS: 
         if(com[i].connectionStatus == 0){
            bc.stop();
            hc.stop();
            pc.stop();
          } else {
            coms.updateAudio(ac.getVolumen());
          }
        break;
        case MOVEMENT_STATUS:
          hc.moveHead(com[i].rx);
          break;
        case BUTTON_STATUS:      
          pc.animate(com[i].pb, com[i].macro);
          ac.playSound(com[i].pb, com[i].macro);
          hc.animate(com[i].macro);
          bc.update(com[i].pb);
          break;
        case PLAY_AUDIO_STATUS:
          if(com[i].macro.size() > 0) {
            Serial.print("Audio: ");
            Serial.print(com[i].macro[0]);
            ac.play(com[i].macro[0]);
          }
        case SETTINGS_STATUS:
          ac.setVolumen(com[i].v);
          ac.enableAudio(com[i].ea);
          break;
      }
    }
  }
  bc.loop();
  hc.update();
}
