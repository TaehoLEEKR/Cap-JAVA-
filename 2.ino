#include <WiFi.h>
#define LED2 2

const char* ssid = "Home";
const char* password = "justmusic";

WiFiServer server(80);

void setup()
{
    Serial.begin(115200);
    pinMode(LED2, OUTPUT);      //  LED pin 모드 설정
 
    delay(10);
 
    // WiFi network에 접속
 
    Serial.println();
    Serial.println();
    Serial.print("Connecting to ");
    Serial.println(ssid);
 
    WiFi.begin(ssid, password);
 
    while (WiFi.status() != WL_CONNECTED) {
        delay(500);
        Serial.print(".");
    }
 
    Serial.println("");
    Serial.println("WiFi connected.");
    Serial.print("IP address: ");
    Serial.println(WiFi.localIP());
    
    server.begin();
}
 
int value = 0;
 
void loop(){
 WiFiClient client = server.available();   // 새로운 클라이언트 생성
 
  if (client) {                             // 클라이언트가 만들어지고
    Serial.println("New Client.");           // 시리얼 포트에 쓰고
    String currentLine = "";                // 클라이언트에서 받을 문자열 변수준비
    while (client.connected()) {            // 연결될 때까지...
      if (client.available()) {             // 클라이언트에서 문자를 받으면
        char c = client.read();             // 문자를 byte 단위로 읽고, 문자로 저장
        Serial.write(c);                    // 시리얼 모니터에 표시
        if (c == '\n') {                    // 문자가 개행문자이면..
 
          // 현재 라인에 아무것도 없으면,  행당 2개라인을 가짐
          // 이는 client HTTP 응답의 마지막이라는 것이므로 응답을 전송할 수 있다:
          if (currentLine.length() == 0) {
            // HTTP 헤더는 항상 응답코드 (e.g. HTTP/1.1 200 OK)로 시작하고
            // content-type이 나오고 빈칸이 나온다:
            client.println("HTTP/1.1 200 OK");
            client.println("Content-type:text/html");
            client.println();
 
            // HTTP 응답은 다음과 같은 헤더를 가진다:
            client.print("<font size=20> <a href=\"/H\"> On </a> <br></font>");
            client.print("<font size=20> <a href=\"/L\"> Off </a> <br></font>");
 
            // HTTP 응답은 빈칸으로 끝난다.
            client.println();
            // while loop를 종료
            break;
          } else {    // 새로운 라인을 가지면, 현재라인 지움:
            currentLine = "";
          }
        } else if (c != '\r') {  // carriage return 문자라면.
          currentLine += c;      // 현재라인 뒤에 붙임
        }
 
        // 클라이언트 응답이 "GET /H" 또는 "GET /L" 였는지 확인:
        if (currentLine.endsWith("GET /H")) {
          digitalWrite(LED2, HIGH);               // GET /H  LED 켬
        }
        if (currentLine.endsWith("GET /L")) {
          digitalWrite(LED2, LOW);                // GET /L LED 끔
        }
      }
    }
    // 연결 종료:
    client.stop();
    Serial.println("Client Disconnected.");
  }
}
