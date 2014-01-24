/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /home/hjeong/Jhs_Android.git/TestProject/Android_hjeong/Service_project/hjeong_assignment3/src/com/example/hjeong_assignment3/IRemoteService.aidl
 */
package com.example.hjeong_assignment3;
public interface IRemoteService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.example.hjeong_assignment3.IRemoteService
{
private static final java.lang.String DESCRIPTOR = "com.example.hjeong_assignment3.IRemoteService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.example.hjeong_assignment3.IRemoteService interface,
 * generating a proxy if needed.
 */
public static com.example.hjeong_assignment3.IRemoteService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.example.hjeong_assignment3.IRemoteService))) {
return ((com.example.hjeong_assignment3.IRemoteService)iin);
}
return new com.example.hjeong_assignment3.IRemoteService.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_Registercallback:
{
data.enforceInterface(DESCRIPTOR);
com.example.hjeong_assignment3.ISend_Value _arg0;
_arg0 = com.example.hjeong_assignment3.ISend_Value.Stub.asInterface(data.readStrongBinder());
this.Registercallback(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_Unregistercallback:
{
data.enforceInterface(DESCRIPTOR);
com.example.hjeong_assignment3.ISend_Value _arg0;
_arg0 = com.example.hjeong_assignment3.ISend_Value.Stub.asInterface(data.readStrongBinder());
this.Unregistercallback(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.example.hjeong_assignment3.IRemoteService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void Registercallback(com.example.hjeong_assignment3.ISend_Value cb) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((cb!=null))?(cb.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_Registercallback, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void Unregistercallback(com.example.hjeong_assignment3.ISend_Value cb) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((cb!=null))?(cb.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_Unregistercallback, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_Registercallback = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_Unregistercallback = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
}
public void Registercallback(com.example.hjeong_assignment3.ISend_Value cb) throws android.os.RemoteException;
public void Unregistercallback(com.example.hjeong_assignment3.ISend_Value cb) throws android.os.RemoteException;
}
