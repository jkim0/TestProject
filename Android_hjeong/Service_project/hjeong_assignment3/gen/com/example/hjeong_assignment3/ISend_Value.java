/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /home/hjeong/WindRiver/hjeong_assignment3/src/com/example/hjeong_assignment3/ISend_Value.aidl
 */
package com.example.hjeong_assignment3;
public interface ISend_Value extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.example.hjeong_assignment3.ISend_Value
{
private static final java.lang.String DESCRIPTOR = "com.example.hjeong_assignment3.ISend_Value";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.example.hjeong_assignment3.ISend_Value interface,
 * generating a proxy if needed.
 */
public static com.example.hjeong_assignment3.ISend_Value asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.example.hjeong_assignment3.ISend_Value))) {
return ((com.example.hjeong_assignment3.ISend_Value)iin);
}
return new com.example.hjeong_assignment3.ISend_Value.Stub.Proxy(obj);
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
case TRANSACTION_Recieve_Value:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _result = this.Recieve_Value(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.example.hjeong_assignment3.ISend_Value
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
@Override public int Recieve_Value(int value) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(value);
mRemote.transact(Stub.TRANSACTION_Recieve_Value, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_Recieve_Value = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public int Recieve_Value(int value) throws android.os.RemoteException;
}
