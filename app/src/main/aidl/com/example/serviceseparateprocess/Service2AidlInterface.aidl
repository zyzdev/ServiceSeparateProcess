// Service2AidlInterface.aidl
package com.example.serviceseparateprocess;

import com.example.serviceseparateprocess.MessageAidlInterface;

interface Service2AidlInterface {
    String sayHelloAndReturnString();

    oneway void setUpMessageSender(MessageAidlInterface messageReceiver);
}
