// MessageAidlInterface.aidl
package com.example.serviceseparateprocess;

interface MessageAidlInterface {
    void messageReceiver(String code, in Bundle data);
}
