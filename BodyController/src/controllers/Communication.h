#pragma once

#include <Arduino.h>
#include <vector>

struct CommunicationReceived {
    int status;
    int connectionStatus;
    std::vector<String> pb;
    std::vector<int> macro;
    int lx;
    int ly;
    int rx;
    int ry;
    int v;
    bool ea;
};

struct CommunicationSent {
    int status;
    int v;
};