#include "Adafruit_FONA.h"
#include <SoftwareSerial.h>

//pin initialization
#define FONA_RST 4
#define FONA_RX 2
#define FONA_TX 3

Adafruit_FONA fona = Adafruit_FONA(FONA_RST);

SoftwareSerial fonaSS = SoftwareSerial(FONA_TX, FONA_RX);
SoftwareSerial *fonaSerial = &fonaSS;

float latitude, longitude;

void setup() {
  while(!Serial);

  Serial.begin(115200);
  Serial.println(F("FONA basic test"));
  Serial.println(F("Initializing....(May take 3 seconds)"));

  fonaSerial->begin(4800);
  if (! fona.begin(*fonaSerial)) {
    Serial.println(F("Couldn't find FONA"));
    while (1);
  }

  fona.setGPRSNetworkSettings(F("net"), F(""), F(""));
  delay(1000);

  while (!fona.enableGPS(true)) {
    Serial.println(F("Failed to turn on GPS"));
    delay(1000);
  }

  while (!fona.getGPS(&latitude, &longitude)) {
    Serial.println(F("Waiting for GPS!"));
    delay(10000);
  }

  while (!fona.enableGPRS(true)) {
    Serial.println(F("Failed to turn on"));
  }
       
}

void loop() {
  if(fona.getGPS(&latitude, &longitude)) {
    uint16_t statuscode;
    int16_t length;
    //initialising the path for the latitude and longitude
    String url = "http://90.95.45.22:3300/findme/script.php?d=c49YyIYLs1NxoTq6LIYYd7niRab2&la=";
    
    Serial.println(latitude);
    Serial.println(longitude);

    //adding the latitude and longitude to the url
    url += String(latitude, 4) + "&lo=" + String(longitude, 4);
    char buf[80];
    url.toCharArray(buf, url.length());
    
    if(!fona.HTTP_GET_start(buf, &statuscode, (uint16_t*)&length)) {
       Serial.println(F("Failed!"));
    }
    
    while (length > 0) {
      while (fona.available()) {
        char c = fona.read();
        Serial.write(c);
        length--;
      }
    }

    fona.HTTP_GET_end();
      
  }
  delay(20000);
}
