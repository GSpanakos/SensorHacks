# SensorHacks
Sensors, Actuators, Arduino, Graphs

Arduino Uno Code

#include <Adafruit_Sensor.h>
#include <DHT.h>
#include <DHT_U.h>
#include <SoftwareSerial.h>       //import Software Serial library

#define DHTPIN  2                 //define pin for sensor
#define DHTTYPE DHT11             //define sensors type

DHT_Unified dht(DHTPIN, DHTTYPE);

SoftwareSerial myStream(10, 11);  //pins for Rx and Tx respectively that controll bluetooth input/output

String command = "";              //read and store user's choice, sent from app
boolean is_written = false;       //to control printing of data
String choice;                    //store sensor data in string
int readValue;
int writeValue;
//int potPin = A1;                  //potentiometer controlling voltage on LED
//int LEDPin = 9;                   //LED pin
int var = 0;
bool flag = false;
uint32_t delayMS;

void setup() {
  // put your setup code here, to run once:
  pinMode(LED_BUILTIN, OUTPUT);       //LED
  //pinMode(potPin, INPUT);        //Potentiometer
  Serial.begin(57600);           //Output stream
  myStream.begin(9600);          //Input Stream
  dht.begin();                   //Start Sensor

  Serial.println("DHTxx Unified Sensor Example");

  // Print temperature sensor details.
  sensor_t sensor;
  dht.temperature().getSensor(&sensor);
  Serial.println("------------------------------------");
  Serial.println("Temperature");
  Serial.print  ("Sensor:       "); Serial.println(sensor.name);
  Serial.print  ("Driver Ver:   "); Serial.println(sensor.version);
  Serial.print  ("Unique ID:    "); Serial.println(sensor.sensor_id);
  Serial.print  ("Max Value:    "); Serial.print(sensor.max_value); Serial.println(" *C");
  Serial.print  ("Min Value:    "); Serial.print(sensor.min_value); Serial.println(" *C");
  Serial.print  ("Resolution:   "); Serial.print(sensor.resolution); Serial.println(" *C");
  Serial.println("------------------------------------");

  // Print humidity sensor details.
  dht.humidity().getSensor(&sensor);
  Serial.println("------------------------------------");
  Serial.println("Humidity");
  Serial.print  ("Sensor:       "); Serial.println(sensor.name);
  Serial.print  ("Driver Ver:   "); Serial.println(sensor.version);
  Serial.print  ("Unique ID:    "); Serial.println(sensor.sensor_id);
  Serial.print  ("Max Value:    "); Serial.print(sensor.max_value); Serial.println("%");
  Serial.print  ("Min Value:    "); Serial.print(sensor.min_value); Serial.println("%");
  Serial.print  ("Resolution:   "); Serial.print(sensor.resolution); Serial.println("%");
  Serial.println("------------------------------------");
  // Set delay between sensor readings based on sensor details.
  delayMS = sensor.min_delay / 1000;

}

void loop() {

  //readValue = analogRead(potPin);                       //Read the voltage on the Potentiometer
  //writeValue =  (255. / 1023.) * readValue;             //Calculate Write Value for LED

  //analogWrite(LEDPin, writeValue);                      //Write to the LED

  //Serial.print("This is the value of the LED: ");       //for debugging print your values
  //Serial.println(writeValue / 51.);
  //Serial.print("This is the value of the Potentiometer: ");
  //Serial.println(readValue / 204.);
  delay(7000);

  while (myStream.available())    //if the Bluetooth client is available
  {
    char ch = myStream.read();    //read data from stream
    command = command + ch;       //create string command
    delay(3);
  }

  Serial.print("Edw typwnei meta ton elegco tou inputstream to new Command ");
  Serial.println(command);


  Serial.println(command);
  delay(delayMS);

  if (command == "Temp Sensor") {
    var = 1;
  } else if (command == "Humidity Sensor") {
    var = 2;
  } else if (command == "Potentiometer") {
    var = 3;
  } else if (command == "Stop") {
    var = 4;
  } else if (command == "LEDON") {
    var = 5;
  } else if (command = "LEDOFF") {
    var = 6;
  }

  switch (var) {
    case 1:   {                                              //matching case for Temp Sensor
        //      is_written = true;                             //set 'is_written' to true


        // Get temperature event and print its value.
        sensors_event_t event;
        dht.temperature().getEvent(&event);
        if (isnan(event.temperature)) {
          Serial.println("Error reading temperature!");
        }
        else {
          Serial.print("Temperature: ");
          Serial.print(event.temperature);
          Serial.println(" *C");
          choice = String(event.temperature);
          is_written = true;
          delay(3000);
        }
        command = "";
        break;
      }
    case 2:  {                                                   //matching case for Humidity Sensor


        //      is_written = true;                              //set 'is_written' to true
        // Get humidity event and print its value.
        sensors_event_t event;
        dht.humidity().getEvent(&event);
        if (isnan(event.relative_humidity)) {
          Serial.println("Error reading humidity!");
        }
        else {
          Serial.print("Humidity: ");
          Serial.print(event.relative_humidity);
          Serial.println("%");
          choice = String(event.relative_humidity);           //convert 'h' to String data type
          is_written = true;                                  //set 'is_written' to true
          delay(3000);
        }
        command = "";
        break;
      }
    case 3:    {                             //matching case for potentiometer
        int val = analogRead(A1);             //read sensor data
        Serial.println(val);
        choice = String(val);                 //convert 'val' to String data type
        is_written = true;                    //set 'is_written' to true
        command = "";
        break;
      }
    case 4:       {                         //Stop
        choice = "";
        command = "";                       //set 'command' to null
        break;
      }
    case 5:       {                       //Turn LED ON
       
        digitalWrite(LED_BUILTIN, HIGH);
        command = "LEDON";
        break;
      }
    case 6:       {                       //Turn LED OFF
  
        digitalWrite(LED_BUILTIN, LOW);
        command = "";
        break;
      }
  }

  for (int x = 0; x < choice.length(); x++) //send each character of data to HC-06
  {
    char ch = choice.charAt(x);
    myStream.write(ch);
    Serial.println(ch);
    flag = true;
  }

  if (flag == true ) {
    myStream.write("~");
    flag = false;
  }

}

