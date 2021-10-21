#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <WiFiClient.h>

#include <ESP8266WiFiMulti.h>
ESP8266WiFiMulti WiFiMulti;

HTTPClient http;

const char* ssid = "SILAB2.4";
const char* password = "SILAB302";

const char* szURL = "http://3.34.255.8/Retrofit/test.php";

unsigned long previousMillis = 0;

String strPHPSESSID;
int iWorkCount = 0;



String httpGETRequest(const char* serverName);



void setup()
{
  Serial.begin(115200);
  Serial.println();
  
  WiFi.mode(WIFI_STA);
  WiFiMulti.addAP(ssid, password);
  while (WiFiMulti.run() != WL_CONNECTED)
  { 
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.println("Connected to WiFi");
  
  http.setReuse(true);
}



void loop()
{
  if (millis() - previousMillis >= 500)
  {
    if (WiFiMulti.run() == WL_CONNECTED)
    {
      String res = httpGETRequest(szURL);
      Serial.print("Result: ");
      Serial.println(res);
      
      // save the last HTTP GET Request
      iWorkCount++;
      if (iWorkCount >= 5)
      {
        Serial.println("Work done!!");
        while (1) yield();
      }
    }
    else
    {
      Serial.println("WiFi Disconnected");
    }
    
    previousMillis = millis();
  }
}



String httpGETRequest(const char* serverName)
{
  WiFiClient client;
  
  http.begin(client, serverName);

  if (strPHPSESSID.length() > 0)
  {
    Serial.print("send PHPSESSID: ");
    Serial.println(strPHPSESSID);
    http.addHeader("Cookie", "PHPSESSID=" + strPHPSESSID);
  }

  const char *keys[] = {"Set-Cookie"};
  http.collectHeaders(keys, 1);

  int httpResponseCode = http.GET();
  
  String strResult = ""; 
  
  if (httpResponseCode == HTTP_CODE_OK || httpResponseCode == HTTP_CODE_MOVED_PERMANENTLY)
  {
    Serial.print("HTTP Response code: ");
    Serial.println(httpResponseCode);
    strResult = http.getString();

    String strCookie = http.header("Set-Cookie");
    if (strCookie.length() > 0)
    {
      int iFind = strCookie.indexOf("PHPSESSID=");
      if (iFind >= 0)
      {
        strPHPSESSID = "";

        int iLen = strCookie.length();
        for (int i = iFind + 10; i < iLen; i++)
        {
          char ch = strCookie.charAt(i);
          if (!isalnum(ch)) break;
          strPHPSESSID += ch;
        }
        
        Serial.print("found PHPSESSID: ");
        Serial.println(strPHPSESSID);
      }
    }
  }
  else
  {
    Serial.print("Error code: ");
    Serial.println(httpResponseCode);
  }

  http.end();

  return strResult;
}
