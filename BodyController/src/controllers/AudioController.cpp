#include "controllers/AudioController.h"

AudioController::AudioController(int rx, int tx): audioSerial(rx, tx){}
void AudioController::setup(){
  audioSerial.begin(38400);
  audioSerial.print( "v" );
  audioSerial.write( volumen_ );
  latestMillis = millis();
}

void AudioController::play(int audio){
    Serial.print("Playing audio: ");
    Serial.println(audio);
    audioSerial.print( "t" );
    audioSerial.write( audio );
}

void AudioController::setVolumen(int volumen){
    volumen_ = volumen;
    audioSerial.print( "v" );
    audioSerial.write( volumen_ );
}

int AudioController::getVolumen(){
    return volumen_;
}

void AudioController::enableAudio(bool enabledAudio){
    if(!enabledAudio)
    {
        volumen_ = 0;
    } 
    audioSerial.print( "v" );
    audioSerial.write( volumen_ );
}

void AudioController::playSound(std::vector<String> keys, std::vector<int> macros){
    String animation;
    int sound = -1; 
    if((keys.size() == 1 || macros.size() == 1) && macros.size() != keys.size()){
        if(keys.size() > 0){
            animation = keys.at(0);
        } else {
            animation = String(macros.at(0));
        }
        for(unsigned int i = 0 ; i < arrayKeys.size() ; i++){
            if(animation == arrayKeys.at(i)){
                sound = i;
                break;
            }
        }
    }
    if(sound != -1 && enabledAudio_){
        Serial.print("Starting audio ");
        Serial.println(songs.at(sound));
        audioSerial.print( "t" );
        audioSerial.write( songs.at(sound) );
    }
    
}