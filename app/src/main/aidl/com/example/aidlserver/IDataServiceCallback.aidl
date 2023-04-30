// IDataServiceCallback.aidl
package com.example.aidlserver;
//回调接口
interface IDataServiceCallback {
    void onMessageReceived(String message); //用于接收客户端回传的消息。
}
