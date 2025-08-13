#include <ArduinoJson.h>
#include <Cytron_SmartDriveDuo.h>

#define TIME_OUT 300
unsigned long currentTime = 0;
bool motorsOn = false;
#define IN1 4 // Arduino pin 4 is connected to MDDS30 pin IN1.
#define AN1 5 // Arduino pin 5 is connected to MDDS30 pin AN1.
#define AN2 6 // Arduino pin 6 is connected to MDDS30 pin AN2.
#define IN2 7 // Arduino pin 7 is connected to MDDS30 pin IN2.
Cytron_SmartDriveDuo kSmartDriveDuo30(PWM_INDEPENDENT, IN1, IN2, AN1, AN2);

void setup() {
  Serial.begin(9600);
  Serial1.begin(9600);
}

void loop() {
  if(millis() - currentTime > TIME_OUT && motorsOn ){
    turnOffFeets();
    motorsOn = false;
  }
  if(Serial1.available()){
    String message = Serial1.readStringUntil('\n');
    if(message == "off"){
      turnOffFeets();
    } else {
        JsonDocument doc;
        DeserializationError err = deserializeJson(doc, message);
        if (err != DeserializationError::Ok) 
        {
          Serial.println("Error getting string");
        }
        currentTime = millis();
        motorsOn = true;
        Serial.print("getting signal");
        int xSignal = doc[0].as<int16_t>();
        int ySignal = doc[1].as<int16_t>();

        
        Serial.print("x: ");
        Serial.print(xSignal);
        Serial.print(" - y: ");
        Serial.println(ySignal);
        movement(xSignal,ySignal);
    }
  }
}

void turnOffFeets(){
  Serial.println("off");
  movement(0,0);
}

void movement(int x, int y){
    if(abs(x) <= 5){
        x = 0;
    }
    if(abs(y) <= 5){
        y = 0;
    }
    y = y * -1;
    float acceleration = 0;
    if(abs(x) > 80 || abs(y) > 80){
        acceleration = -1;
    }
    else if(abs(x) > 60 || abs(y) > 60){
        acceleration = -0.8;
    }
    else{
        acceleration = -0.5;
    }
    long leftSpeed = (y + x) * acceleration;
    long rightSpeed = (y - x) * acceleration;

    if(((leftSpeed > 85 && rightSpeed > 85) || (leftSpeed < -85 && rightSpeed < -85)) && (abs(leftSpeed) - abs(rightSpeed)  < 10)){
        leftSpeed = rightSpeed > leftSpeed ? leftSpeed : rightSpeed;
    }

    leftSpeed = constrain(leftSpeed, -100, 100);
    rightSpeed = constrain(rightSpeed, -100, 100);
    kSmartDriveDuo30.control(leftSpeed, rightSpeed);
}
