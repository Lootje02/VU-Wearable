# VU 8



## Getting started
The VU stands for VU University Amsterdam and is a broad research and education university. In addition to being a university institution, it is also a teaching hospital. These are both located in the same location. VU was founded on October 20, 1880 by Abraham Kuyper.

VU University Amsterdam is researching the rhythm of the heart and wants to use an app for this. They want to be able to take heart measurements and find out what the situation is by means of an app. This allows them to research why the heart beats a certain amount when a certain situation occurs. Because they themselves are very busy with other assignments, they have called in the HvA to help with this.

## Product vision
Our goal is to make the process of using wearable technology simple and intuitive, allowing users and professors to focus on their research and studies.

Our Mobile Android Application aims to provide a seamless and convenient experience for both users and professors by connecting wearable devices with an easy-to-use interface. The app will offer a variety of features such as real-time monitoring, data visualization, and notifications to enhance the user experience. Additionally, the app will empower professors to set up and manage wearable devices, allowing them to efficiently distribute and collect data for research and classroom use.

## Technologies & Tools
| Tool | Description |
|------|-------------|
| Android Studio | IDE for developing Android Applications |
| Android Emulator | Available in Android Studio |
| Ubuntu on Windows | Windows Only (See Redirect Traffic From Wearable) |
| WireShark | Tool to check network activity, used to check if data is coming in from the wearable. ([Wireshark](https://www.wireshark.org/)) |
| RawCap | RawCap is a free command line network sniffer for Windows that uses raw sockets. Can sniff any interface that has got an IPv4 address, including 127.0.0.1 (localhost/loopback). ([RawCap](https://www.netresec.com/?page=RawCap)) <br><br>Tool can be used on Windows to check if the data of the wearable is being redirected to your local network by Ncat when developing and testing. |

# App Permissions

The Android Application uses several Android app permissions specified in the `AndroidManifest.xml` as follows:

<table>
<tr>
<th>

**<span dir="">Permission</span>**
</th>
<th>

**<span dir="">Usage</span>**
</th>
<td>

**<span dir="">Shows prompt</span>**
</td>
</tr>
<tr>
<td>

* <span dir="">INTERNET</span>
* <span dir="">ACCESS_NETWORK_STATE</span>
</td>
<td>

<span dir="">Used to connect to the wearable and use the network the device is connected to.</span>
</td>
<td>

<span dir="">No</span>
</td>
</tr>
<tr>
<td>

* <span dir="">FOREGROUND_SERVICE</span>
</td>
<td>

<span dir="">Used to send notifications such as connection notifications.</span>
</td>
<td>

<span dir="">Yes</span>
</td>
</tr>
<tr>
<td>

* <span dir="">ACCESS_WIFI_STATE</span>
* <span dir="">ACCESS_FINE_LOCATION</span>
* <span dir="">ACCESS_COARSE_LOCATION</span>
* <span dir="">CHANGE_WIFI_STATE</span>
</td>
<td>

<span dir="">Used to read the SSID of the current network a user is connected to.</span>
</td>
<td>

<span dir="">Yes -</span>

<span dir="">_custom implementation_</span>
</td>
</tr>
</table>

<span dir="">If a permission needs to be granted by the user, the application will ask for permission by showing a prompt. These prompts are often handled by Android itself, but for the location permission prompt a custom implementation was used in the **`MainActivity`** class of the application.</span>

## WebSocket Service

<span dir="">Every VU Wearable is equipped with a WebSocket. This WebSocket can be reached when connected to the WiFi network of the wearable at the following URI: </span>\
**<span dir="">`ws://192.168.4.1:80/ws`</span>**\
\
<span dir="">The **`SocketService`** class of the Android Application allows the app to connect to the websocket on the device and send messages. The app acts as the client and the wearable as the server as shown in this scheme: </span>

<span dir="">![](https://lh6.googleusercontent.com/Q5mK64XSclzjYPzoELlghAMV3ahJk4AzmBw2JLjU06WsxtaoaF0FcI2DlG9jqSdk7dsH77XFxh-pgfOOVssFaVwxL1uMXf6DUZ2e0u5C6sHNdkfhJdQSd1ECMsbIMR7zD69aiucdPGW1WxzxyQSJTeBiL6tYQYAca91ROOlS48xAjNgka8tucMuhbp9XPg){width="505" height="303"}</span>

<span dir="">The wearable can perform certain actions such as stopping a measurement based on a text message which is received on its WebSocket. These messages can be sent from the Android Application using the **`sendMessage()`** method of the **`SocketService`** class. The wearable listens to the following messages/commands:</span>

<table>
<tr>
<td>

**<span dir="">Message</span>**
</td>
<td>

**<span dir="">Action</span>**
</td>
</tr>
<tr>
<td>

<span dir="">r</span>
</td>
<td>

<span dir="">Run</span>
</td>
</tr>
<tr>
<td>

<span dir="">s</span>
</td>
<td>

<span dir="">Stop</span>
</td>
</tr>
<tr>
<td>

<span dir="">w</span>
</td>
<td>

<span dir="">WiFi On/Off</span>
</td>
</tr>
<tr>
<td>

<span dir="">3a</span>
</td>
<td>

<span dir="">Start Live Data</span>
</td>
</tr>
<tr>
<td>

<span dir="">0a</span>
</td>
<td>

<span dir="">Stop Live Data</span>
</td>
</tr>
<tr>
<td>

<span dir="">m</span>
</td>
<td>

<span dir="">Marker</span>
</td>
</tr>
</table>

<span dir="">_Please note that these messages use small letters._</span>

## Researcher premissions

It is also possible to see the app as a researcher. A researcher has more functions than a participant. To log in as a researcher, you must use the login button at the top of the screen. Then you have to enter 'nextgen2022' as password. As a researcher you have the following rights:

- Other FAQ questions, which have more to do with the technical side.
- An extra start stop button on the device to start the measurement.

## **<span dir="">Redirect Traffic from Wearable to Emulator</span>**

<span dir="">In order to test the connection with the device, we need to route the incoming traffic from the device from our laptop to our emulator. To make this work, please follow the following steps:</span>

### **<span dir="">_Windows_</span>**

 1. <span dir="">Download and install Ubuntu for WSL2 on Windows</span>[<span dir=""> here</span>](https://ubuntu.com/tutorials/install-ubuntu-on-wsl2-on-windows-10#1-overview)<span dir=""> (**Follow Steps 1 to 4**) . You can install Ubuntu by running the following command in PowerShell: </span>
    1. **<span dir="">wsl --install -d ubuntu</span>**
       1. <span dir=""> (d = distro), without the -d flag Windows does not know which Linux distro to install.</span>
 2. <span dir="">telnet is by default disabled on Windows but available without having to be downloaded. Enable this feature by following the steps</span>[<span dir=""> here</span>](https://social.technet.microsoft.com/wiki/contents/articles/38433.windows-10-enabling-telnet-client.aspx)<span dir="">.</span>
 3. <span dir="">Open Ubuntu through Windows Search or through</span>[<span dir=""> Windows Terminal</span>](https://apps.microsoft.com/store/detail/9N0DX20HK701?hl=nl-nl&gl=NL)<span dir="">. If it is your first time using Ubuntu, you will be asked to set a username and password, remember these values as you will need them when performing sudo commands!</span>
 4. <span dir="">In your Ubuntu CLI run **sudo apt-get update** and after that **sudo apt install ncat**.</span>
 5. <span dir="">Connect to the AMS device's WiFi.</span>
 6. <span dir="">Open the emulator you use via the terminal. On Windows the emulator is located within the hidden %appdata% folder. The path should be as follows: C:\\Users\\<your-username>\\AppData\\Local\\Android\\Sdk\\emulator. Open a cmd window in this path. When your cmd has opened, run the following command to start the emulator: </span>
    1. **<span dir="">emulator -avd DEVICE_NAME_HERE -feature -Wifi</span>**
       1. <span dir="">To see what the name of your device is, run adb devices, this is often the name displayed in your device manager in Android Studio but with  _replacing the spaces in the name (for example: Resizable_API_33).![](https://lh4.googleusercontent.com/1ckNp5WD3jwabpz7SIdb76hCrT-yTzumuSTz93LcheBxqhw6ebwxeJ8wP9A3pz4TALsQlV6UbxJMhVTIfyxUrmAFsLh0ekoE760i6kseIxgXLwwMvzwX6pDONPdLb7qTkKajoD4DZ1TjxuU7EzqWoyY9r8ek88vfKuPGvvCROlWrR8Fu9S1hI9a4TOyo5w){width="427" height="189"}</span>
 7. <span dir="">Once the emulator is open, open a new cmd window. In this window, open a telnet connection to the emulator by executing **telnet**</span> [**<span dir="">localhost</span>**](http://localhost) **<span dir="">5554</span>**<span dir="">. You will be prompted to authorize yourself. Follow the instructions in the terminal.</span>
    1. <span dir="">To authorize, type **auth <code here>**.</span>
 8. <span dir="">After you have been authorized, run the following commands:</span>
    1. **<span dir="">redir add udp:1234:1234</span>** 
    2. **<span dir="">redir add udp:54440:54440</span>**
 9. <span dir="">Quit the Telnet CLI.</span>
10. <span dir="">Now run the following command in your Ubuntu window: </span>
    1. **<span dir="">ncat -l -u 192.168.4.2 1234 -v | ncat -4 -u 127.0.0.1 1234 -v -p 54440</span>**
11. <span dir="">Now run the app and check if it is connected or not and data is coming in.</span>

### **<span dir="">_MacOS_</span>**

<span dir="">If you use MacOS, you can just use your system terminal.</span>

1. <span dir="">Install</span>[<span dir=""> HomeBrew</span>](https://brew.sh/)<span dir=""> .</span>
2. <span dir="">Run **brew install telnet** in the terminal to install telnet.</span>
3. <span dir="">Connect to the AMS device's WiFi.</span>
4. <span dir="">Open the emulator you use via a terminal window. On MacOS it is the following path: </span>
   1. <span dir="">cd \~/Library/Android/sdk/emulator and then ./emulator -avd DEVICE_NAME_HERE -feature -Wifi</span>
      1. <span dir="">To see what the name of your device is, run **adb devices**.</span>
5. <span dir="">In your terminal, type the following: **telnet**</span> [**<span dir="">localhost</span>**](http://localhost) **<span dir="">5554</span>**<span dir=""> and then you need to authorize yourself. Check what it says in the terminal.</span>
6. <span dir="">To authorize, type **auth <code here>**.</span>
7. <span dir="">Run the following commands: </span>
   1. **<span dir="">redir add udp:1234:1234</span>**<span dir=""> </span>
   2. **<span dir="">redir add udp:54440:54440</span>**
      1. <span dir=""> (**_NOTE_**: Replace 54440 with the port you see the data is going to in WireShark).</span>
8. <span dir="">Quit the telnet CLI and type run: **nc -l -u 1234 | nc -u 127.0.0.1 1234 -v**.</span>
9. <span dir="">Run the app and check if it is connected or not.</span>
  
* First you have to go to the next file: app/src/main/java/nl/hva/vuwearable/ui/faq/FaqViewModel.kt
* Next, you need to look into the next function '<span dir="">getFaqQuestionsAndAnswers</span>'
* Then there is an if statement. In the if you add the participant questions. In the else you can add the questions for the researcher.
* **Always add one element in the questions list and also one element in the answers list.**
