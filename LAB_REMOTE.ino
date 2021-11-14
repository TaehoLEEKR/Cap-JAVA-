#include <ArduinoJson.h>
#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <WiFiClient.h>
#include <ESP8266WiFiMulti.h>
#include <IRremoteESP8266.h>
#include <IRsend.h>  // Needed if you want to send IR commands.
//#include <IRrecv.h>  // Needed if you want to receive IR commands.


///////////////////////////////////////////// 전처리 /////////////////////////////////////////////
IRsend irsend(12); //D6 전송할 핀 설정
ESP8266WiFiMulti wifiMulti; // 와이파이 객체 생성

const char* ssid = "SILAB2.4"; // SSID
const char* password = "SILAB302"; // AP P/W
int onOffFlag = 0; // 현재 에어컨 상태 플래그 / 0 = OFF, 1 = ON
int temperature = 18; // 온도


/////////////////////////////////////////////////////////////////////////////////////////////////
int getRemoteSign(int type) {
  WiFiClient client; // 와이파이 클라이언트 객체 생성
  client.setTimeout(10000); // 타임아웃 설정 메서드 호출
  if (!client.connect("3.34.255.8", 80)) return -1; // 연결에 실패했으면 -1반환

  // Send HTTP request
  client.print(F("GET /Retrofit/arduinoAC.php"));
  client.print(F("?SET="));
  client.print(F("2"));
  client.println(F(" HTTP/1.1"));
  client.println(F("Host: 3.34.255.8"));
  client.println(F("Connection: close"));
    
  
  
  if (client.println() == 0) { // request fali 예외처리
    Serial.println(F("Failed to send request"));
    client.stop();
    return -1;
  }

  // Check HTTP status
  char status[32] = {0};
  client.readBytesUntil('\r', status, sizeof(status)); // "HTTP/1.1 200 OK"
  if (strcmp(status + 9, "200 OK") != 0) { // 수신 데이터 예외처리
    Serial.print(F("Unexpected response: "));
    Serial.println(status);
    client.stop();
    return -1;
  }

  // Skip HTTP headers
  char endOfHeaders[] = "\r\n\r\n";
  if (!client.find(endOfHeaders)) {
    Serial.println(F("Invalid response"));
    client.stop();
    return -1;
  }

  // Allocate the JSON document
  // Use https://arduinojson.org/v6/assistant to compute the capacity.
  const size_t capacity = JSON_OBJECT_SIZE(3) + JSON_ARRAY_SIZE(2) + 60;
  DynamicJsonDocument doc(capacity);

  // Parse JSON object
  DeserializationError error = deserializeJson(doc, client);
  if (error) {
    Serial.print(F("deserializeJson() failed: "));
    Serial.println(error.f_str());
    client.stop();
    return -1;
  }

  // Disconnect
  client.stop();

  if (type == 0){
    Serial.print(F("POWER : "));
    Serial.println(doc["POWER"].as<int>());
    return doc["POWER"].as<int>();
  }
  if (type == 1) {
    Serial.print(F("TEMP : "));
    Serial.println(doc["TEMP"].as<int>());
    return doc["TEMP"].as<int>();
  }
}

/////////////////////////////////////////////////////////////////////////////////////////////////
void setup() {
  // Initialize Serial port
  Serial.begin(115200);
  irsend.begin();
  
  while (!Serial) continue;

  // Initialize Ethernet library
  WiFi.mode(WIFI_STA);
  wifiMulti.addAP(ssid, password);
  while (wifiMulti.run() != WL_CONNECTED)
  { 
    delay(500);
    Serial.print(".");
  }
}

/////////////////////////////////////////////////////////////////////////////////////////////////
void loop() { 
  if (onOffFlag != getRemoteSign(0)) {
    onOffFlag = getRemoteSign(0);

    switch(onOffFlag) {
      case -1:
        Serial.println("Error");
        break;
      case 1:
        irsend.sendLG(0x8800549, 28);  //ON
        Serial.println("Send On Signal");
        break;
      case 0:
        irsend.sendLG(0x88C0051, 28);  //OFF
        Serial.println("Send Off Signal");
        break;
    }
  }

  if (onOffFlag && temperature != getRemoteSign(1)){
      temperature = getRemoteSign(1);
      int sig = 0x880834F;
      int temp = 0x400;

      if (temperature != 18) {
        for (int i = temperature ; i > 19; i--) temp += 0x101;
        sig = 0x8808040 + temp;
      }
      
      irsend.sendLG(sig, 28);
      Serial.print("Temperature Send : ");
      Serial.println(temperature);
  }


  delay(500);
}
