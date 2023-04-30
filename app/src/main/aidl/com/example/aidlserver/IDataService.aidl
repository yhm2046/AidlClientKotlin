// IDataService.aidl
package com.example.aidlserver;
import com.example.aidlserver.IDataServiceCallback; //引用另外一个aidl
interface IDataService {
    void sendMessage(String message);   //用于向客户端发送消息
    void registerCallback(IDataServiceCallback callback);   //注册绑定回调
    void unregisterCallback(IDataServiceCallback callback); //注销解绑回调
}

