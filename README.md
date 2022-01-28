# ServiceSeparateProcess
A example to present how to communicate between service with different process.

This example present three method to communicate between service with different process.

1. [By startService](#by-startservice), send intent via startService.
2. [By BroadcastReceiver](#by-broadcastreceiver), send intent via sendBroadcast.
3. [By AIDL](#by-aidl), feed callback to another service.


<img src="./screenshot/Screenshot_1643362404.png" width="300" />

#### By StartService
```
30122-30122 D/Service1: sendMessageByIntent from Service2
30128-30128 D/Service2: sendMessageByIntent from Service1
30128-30128 D/Service2: sendMessageByIntent from Service1
30122-30122 D/Service1: sendMessageByIntent from Service2
30122-30122 D/Service1: sendMessageByIntent from Service2
30128-30128 D/Service2: sendMessageByIntent from Service1
30128-30128 D/Service2: sendMessageByIntent from Service1
30122-30122 D/Service1: sendMessageByIntent from Service2
...
```
#### By BroadcastReceiver

```
30128-30128 D/Service2: sendMassageByBroadcast from Service1
30122-30122 D/Service1: sendMassageByBroadcast from Service2
30128-30128 D/Service2: sendMassageByBroadcast from Service1
30122-30122 D/Service1: sendMassageByBroadcast from Service2
30128-30128 D/Service2: sendMassageByBroadcast from Service1
30122-30122 D/Service1: sendMassageByBroadcast from Service2
30128-30128 D/Service2: sendMassageByBroadcast from Service1
30122-30122 D/Service1: sendMassageByBroadcast from Service2
30128-30128 D/Service2: sendMassageByBroadcast from Service1
...
```

#### By AIDL
```
30128-30164 D/Service2: Service2 call sayHelloAndReturnString, return "How are you?"
30122-30177 D/Service1: "How are you?"
30122-30147 D/Service1: sendMassageByAIDL from Service2
```
