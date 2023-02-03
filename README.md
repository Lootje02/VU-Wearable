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
