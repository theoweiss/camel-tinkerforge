[![Build Status](https://travis-ci.org/eddi888/camel-tinkerforge.svg?branch=master)](https://travis-ci.org/eddi888/camel-tinkerforge)
## Tinkerforge Component (generated)


The **Tinkerforge** Apache Camel Component for a simple access for use the Tinkerforge Daemon, Bricks and Bricklets.
```xml
<dependency>
    <groupId>org.atomspace.camel</groupId>
    <artifactId>camel-tinkerforge</artifactId>
    <version>2.15.2</version>
</dependency>
```


### Key Features


- Support 100% of all Bricks and Bricklets

- Support Selection over all Callbacks from every Bricklet

- Support Configuration over Path-Variables

- Support Configuration over Header-Variables inside Exchange

- Support Request Reply Route like the Command Messages Pattern

- Based on the model of the [Tinkerforge-Generators](https://github.com/Tinkerforge/generators)


### Version Matrix


camel-tinkerforge-Version  | Apache-Camel-Version  | Tinkerforge-SDK-Version
-------------------------- | --------------------- | -------------------------
2.14.0                     | 2.14.0                | 2.1.2
2.15.2                     | 2.15.2                | 2.1.5
2.15.3                     | 2.15.3                | 2.1.5
2.17.0                     | 2.17.0                | 2.1.8




### URI format


```
tinkerforgegen:[host[:port]/]deviceType?[uid=uid&options...]
```

### General Options


Name           | Default Value | Description
-------------- | ------------- | -------------
host           | localhost     | Hostname / IP-Address
port           | 4223          | Port
secret         |               | Secret String for authenticate
autoReconnect  | true          | reconnection on broken connection
timeout        | 2500          | Timeout
deviceType     |               | Temperatur, Line, IO-4, Color, MotionDetector, ...
uid            |               | UID of the Bricklet
callback       |               | add Device Listener to ConsumerEndpoint for receive values, if callback is null all Listeners will be registrate
function       |               | execute Device Function, can use for configure a ConsumerEndpoint and ProducerEndpoint or use dynamic in header for producerEndpoints


### Supported Components

[Accelerometer](src/main/java/org/atomspace/camel/component/tinkerforge/device/Accelerometer.md)

[AmbientLight](src/main/java/org/atomspace/camel/component/tinkerforge/device/AmbientLight.md)

[AmbientLightV2](src/main/java/org/atomspace/camel/component/tinkerforge/device/AmbientLightV2.md)

[AnalogIn](src/main/java/org/atomspace/camel/component/tinkerforge/device/AnalogIn.md)

[AnalogInV2](src/main/java/org/atomspace/camel/component/tinkerforge/device/AnalogInV2.md)

[AnalogOut](src/main/java/org/atomspace/camel/component/tinkerforge/device/AnalogOut.md)

[AnalogOutV2](src/main/java/org/atomspace/camel/component/tinkerforge/device/AnalogOutV2.md)

[Barometer](src/main/java/org/atomspace/camel/component/tinkerforge/device/Barometer.md)

[CO2](src/main/java/org/atomspace/camel/component/tinkerforge/device/CO2.md)

[Color](src/main/java/org/atomspace/camel/component/tinkerforge/device/Color.md)

[Current12](src/main/java/org/atomspace/camel/component/tinkerforge/device/Current12.md)

[Current25](src/main/java/org/atomspace/camel/component/tinkerforge/device/Current25.md)

[DC](src/main/java/org/atomspace/camel/component/tinkerforge/device/DC.md)

[DistanceIR](src/main/java/org/atomspace/camel/component/tinkerforge/device/DistanceIR.md)

[DistanceUS](src/main/java/org/atomspace/camel/component/tinkerforge/device/DistanceUS.md)

[DualButton](src/main/java/org/atomspace/camel/component/tinkerforge/device/DualButton.md)

[DualRelay](src/main/java/org/atomspace/camel/component/tinkerforge/device/DualRelay.md)

[DustDetector](src/main/java/org/atomspace/camel/component/tinkerforge/device/DustDetector.md)

[GPS](src/main/java/org/atomspace/camel/component/tinkerforge/device/GPS.md)

[HallEffect](src/main/java/org/atomspace/camel/component/tinkerforge/device/HallEffect.md)

[Humidity](src/main/java/org/atomspace/camel/component/tinkerforge/device/Humidity.md)

[IMU](src/main/java/org/atomspace/camel/component/tinkerforge/device/IMU.md)

[IMUv2](src/main/java/org/atomspace/camel/component/tinkerforge/device/IMUV2.md)

[IndustrialAnalogOut](src/main/java/org/atomspace/camel/component/tinkerforge/device/IndustrialAnalogOut.md)

[IndustrialDigitalIn4](src/main/java/org/atomspace/camel/component/tinkerforge/device/IndustrialDigitalIn4.md)

[IndustrialDigitalOut4](src/main/java/org/atomspace/camel/component/tinkerforge/device/IndustrialDigitalOut4.md)

[IndustrialDual020mA](src/main/java/org/atomspace/camel/component/tinkerforge/device/IndustrialDual020mA.md)

[IndustrialDualAnalogIn](src/main/java/org/atomspace/camel/component/tinkerforge/device/IndustrialDualAnalogIn.md)

[IndustrialQuadRelay](src/main/java/org/atomspace/camel/component/tinkerforge/device/IndustrialQuadRelay.md)

[IO16](src/main/java/org/atomspace/camel/component/tinkerforge/device/IO16.md)

[IO4](src/main/java/org/atomspace/camel/component/tinkerforge/device/IO4.md)

[Joystick](src/main/java/org/atomspace/camel/component/tinkerforge/device/Joystick.md)

[LCD16x2](src/main/java/org/atomspace/camel/component/tinkerforge/device/LCD16x2.md)

[LCD20x4](src/main/java/org/atomspace/camel/component/tinkerforge/device/LCD20x4.md)

[LEDStrip](src/main/java/org/atomspace/camel/component/tinkerforge/device/LEDStrip.md)

[LaserRangeFinder](src/main/java/org/atomspace/camel/component/tinkerforge/device/LaserRangeFinder.md)

[Line](src/main/java/org/atomspace/camel/component/tinkerforge/device/Line.md)

[LinearPoti](src/main/java/org/atomspace/camel/component/tinkerforge/device/LinearPoti.md)

[LoadCell](src/main/java/org/atomspace/camel/component/tinkerforge/device/LoadCell.md)

[Master](src/main/java/org/atomspace/camel/component/tinkerforge/device/Master.md)

[Moisture](src/main/java/org/atomspace/camel/component/tinkerforge/device/Moisture.md)

[MotionDetector](src/main/java/org/atomspace/camel/component/tinkerforge/device/MotionDetector.md)

[MultiTouch](src/main/java/org/atomspace/camel/component/tinkerforge/device/MultiTouch.md)

[NFCRFID](src/main/java/org/atomspace/camel/component/tinkerforge/device/NFCRFID.md)

[OLED128x64](src/main/java/org/atomspace/camel/component/tinkerforge/device/OLED128x64.md)

[OLED64x48](src/main/java/org/atomspace/camel/component/tinkerforge/device/OLED64x48.md)

[PiezoBuzzer](src/main/java/org/atomspace/camel/component/tinkerforge/device/PiezoBuzzer.md)

[PiezoSpeaker](src/main/java/org/atomspace/camel/component/tinkerforge/device/PiezoSpeaker.md)

[RS232](src/main/java/org/atomspace/camel/component/tinkerforge/device/RS232.md)

[PTC](src/main/java/org/atomspace/camel/component/tinkerforge/device/PTC.md)

[RealTimeClock](src/main/java/org/atomspace/camel/component/tinkerforge/device/RealTimeClock.md)

[RED](src/main/java/org/atomspace/camel/component/tinkerforge/device/RED.md)

[RemoteSwitch](src/main/java/org/atomspace/camel/component/tinkerforge/device/RemoteSwitch.md)

[RotaryEncoder](src/main/java/org/atomspace/camel/component/tinkerforge/device/RotaryEncoder.md)

[RotaryPoti](src/main/java/org/atomspace/camel/component/tinkerforge/device/RotaryPoti.md)

[SegmentDisplay4x7](src/main/java/org/atomspace/camel/component/tinkerforge/device/SegmentDisplay4x7.md)

[Servo](src/main/java/org/atomspace/camel/component/tinkerforge/device/Servo.md)

[SolidStateRelay](src/main/java/org/atomspace/camel/component/tinkerforge/device/SolidStateRelay.md)

[SoundIntensity](src/main/java/org/atomspace/camel/component/tinkerforge/device/SoundIntensity.md)

[Stepper](src/main/java/org/atomspace/camel/component/tinkerforge/device/Stepper.md)

[Temperature](src/main/java/org/atomspace/camel/component/tinkerforge/device/Temperature.md)

[TemperatureIR](src/main/java/org/atomspace/camel/component/tinkerforge/device/TemperatureIR.md)

[Thermocouple](src/main/java/org/atomspace/camel/component/tinkerforge/device/Thermocouple.md)

[Tilt](src/main/java/org/atomspace/camel/component/tinkerforge/device/Tilt.md)

[UVLight](src/main/java/org/atomspace/camel/component/tinkerforge/device/UVLight.md)

[Voltage](src/main/java/org/atomspace/camel/component/tinkerforge/device/Voltage.md)

[VoltageCurrent](src/main/java/org/atomspace/camel/component/tinkerforge/device/VoltageCurrent.md)



### General Message Headers


Header             | Description
------------------ | -------------
uid                | Device UID
connectedUid       | Connected UID
deviceIdentifier   | Identifier of Device
position           | Device Postiontion on Brick   

### Samples endpoints
---------------------------------------------------
Typically connection where the daemon running on same system 
```
<route>
  <from uri="timer://foo?period=10000"/>
  <to uri="tinkerforgegen:temperture?uid=ABC&function=getTemperature" />
  <to uri="log:temperature?level=INFO&showHeaders=true"/>
</route>

```

Use remote temperatur sensor inside an Request Reply Route
```
<route>
  <from uri="jetty:0.0.0.0:8044/bathroom" />
  <to uri="tinkerforgegen:192.168.4.17:1433/temperature?uid=ABC&function=getTemperature" />
</route>

```

Temperatur consumer from remote masterbrick 
```
<route>
  <from uri="tinkerforgegen:192.168.4.17:1433/temperature?uid=ABC&init=setTemperatureCallbackPeriod&period=5000" />
  <to uri="bean:save"/>
</route>
```


