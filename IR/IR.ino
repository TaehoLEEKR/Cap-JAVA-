#include <ESP8266WiFi.h>
#include <IRremoteESP8266.h>
#include <IRsend.h>  // Needed if you want to send IR commands.
#include <IRrecv.h>  // Needed if you want to receive IR commands.


IRsend irsend(4); //an IR emitter led is connected to GPIO pin 4 (4)


void setup ()
{
  Serial.begin (115200);
  irsend.begin();
}

void loop ()
{
  irsend.sendLG(0x8800549, 28);  //ON
  Serial.println("Send On Signal");
  delay(5000);

  irsend.sendLG(0x88C0051, 28);  //ON
  Serial.print("Sent");
  delay(5000);
}
 
