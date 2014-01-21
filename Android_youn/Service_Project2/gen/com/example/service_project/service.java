/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /home/yyoun/workingdirectory/Service_Project/src/com/example/service_project/service.aidl
 */
package com.example.service_project;
public interface service extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.example.service_project.service
{
private static final java.lang.String DESCRIPTOR = "com.example.service_project.service";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.example.service_project.service interface,
 * generating a proxy if needed.
 */
public static com.example.service_project.service asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.example.service_project.service))) {
return ((com.example.service_project.service)iin);
}
return new com.example.service_project.service.Stub.Proxy(obj);
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
case TRANSACTION_getvalue:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getvalue();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_Change_Mode:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.Change_Mode();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.example.service_project.service
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
@Override public int getvalue() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getvalue, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean Change_Mode() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_Change_Mode, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_getvalue = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_Change_Mode = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
}
public int getvalue() throws android.os.RemoteException;
public boolean Change_Mode() throws android.os.RemoteException;
}
