# Arduino Controller 

This proyect use an esp32 to work as a bridge between a controller and the brain of chopper, the controllers supported now are a [Bluethooth app](https://github.com/andcoderes/ChopperControllerAndroid) and a [Flysky FS-I6X](https://www.flysky-cn.com/fsi6). By default the controller is getting information from the RC controller except when the bluetooth connection is stablish, when that happend the app take over. 

When a bluethooth connection is stablish, it will query the app every 200 MS to see if we have an active connection, if we hit a timeout this controller will send a stop all to the brain of chopper 

This component also work as a iddle device, that mean when that is one it will send actions between 0 an 10 seconds to the brain to execute actions


## Android App in commands 

The communication between the Android app and the esp32 is througth bluethooth with a string in a JSON formart. This message is forwarded to the body and then to the head and depending of the message is what each component do.

### JSON format
The json format could vary between messages but the constant is the one with the "s" where "s" stand for settings. This settings are the following 

|  value |  definition|
| :-------:| :------------|
| 0       | Buttons / Mactos|
| 1 | Movements|
|2| Settings|
|3| Audios|
|4|Ping|


### Buttons AND Macros
This are actions that can be triggered by controllers buttons or apps buttons, the format of the object that we recive is 

```
{
    "s":0,
    "p":["a"],
    "m":[100]
}
```

| button | Description |
| :-----: | :-----|
| A| Open and Close front arm|
| B | Take out rigth head arm / Take in rigth head arm |
| Y | Take out both hands / Take In both Hands|
| X |Take out left head arm / Take in left head arm|
| L1 |Open left breakpad door / Breakpad action / Close left breakpad door|
| L2 | Bubbles |
| L3 | L3 + R3 emergency stop|
| R1 |Open rigth breakpad door / Breakpad action / Close rigth breakpad door|
| R2 | Smoke |
| R3 | L3 + R3 emergency stop|
| DUP |Take out periscope|
| DDOWN |Take down Periscope|
| DLEFT |Periscope animation|
| DRIGTH |Periscope animation| 
 

| Macos | Description| 
| :--: | :--|
| 100 | Rotate head to the left |
| 101 | Rotate head to the rigth |
| 102 | Animate body Arm |
| 103 | Periscope animation |
| 104 | Greetings with left hand |
| 105 | Dance with hands |
| 106 | Greetings with rigth hand |
| 107 | Bubble like broke  |


### Movements

This command manage the movements of DC motors, this motors are 2 in the feets and 1 in the head the format is the following 

```
{
    "s":1,
    "l":[0,0],
    "r":[0,0]
}
```
where l is the left analog and r is the rigth analog the array value is the x and y position of each controller component, the values are from 0 to 100  

### Settings

This are the configurations available for the droid and are set up as the following: 
```
{
    "s":2,
    "v":0,
    "i":true
}
```


| setting | description | 
| :--: | :-- | 
| v | Volume goes from 0 to 255 | 
| i | iddle mode |

### Audio 

Here we send the signal of wich audio we want to play (there are some macros that are also set up with audio), on the app are configurated 31 audio but the code play the audio that is in the sd with the number.
```
{
    "s":3,
    "p":[],
    "m":[1]
}
```

### Ping

This command is used to be sure about the connection between devices, we are checking every 200 ms each other to see if everything is ok 

```
{
    "s":4,
    "v":0,
}
```