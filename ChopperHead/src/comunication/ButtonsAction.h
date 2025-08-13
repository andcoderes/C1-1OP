#pragma once
#include <Arduino.h>

struct ButtonsAction{
    int status;
    int connectionStatus = -1;
    int lx;
    int ly;
    int rx;
    int ry;
    String pb[20];
    int macro[20];
};