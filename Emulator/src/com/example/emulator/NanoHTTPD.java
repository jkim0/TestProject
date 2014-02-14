package com.example.emulator;
//ss/////////////////////sdlkfjsldkfjskldjfslkdjfslkdjflskjdfkl
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.Vector;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.TransactionTooLargeException;
import android.util.Log;
import android.widget.Toast;

import com.example.emulator.NanoHTTPD.HTTPSession;
import com.example.emulator.NanoHTTPD.Response;

class NanoHTTPD {
	private final String TAG = "NanoHTTPD";

	// ==================================================
	// API parts
	// ==================================================
	/**
	 * Override this to customize the server.
	 * <p>
	 * 
	 * (By default, this delegates to serveFile() and allows directory listing.)
	 * 
	 * @param uri
	 *            Percent-decoded URI without parameters, for example
	 *            "/index.cgi"
	 * @param method
	 *            "GET", "POST" etc.
	 * @param parms
	 *            Parsed, percent decoded parameters from URI and, in case of
	 *            POST, data.
	 * @param header
	 *            Header entries, percent decoded
	 * @return HTTP response, see class Response for details
	 */
	//servefile을 호출한
	public Response serve(String uri, String method, Properties header,Properties parms) {
		// 여기서 갑자기 post 'index2.html' 생김 / uri 발생
		myOut.println(method + " '" + uri + "' ");

		Enumeration e = header.propertyNames();
		Log.i("check", "header.propertyNames:" + header.propertyNames());
		Log.i("check", "e:" + e);

		while (e.hasMoreElements()) {
			Log.e("CHECK", "e.has");
			String value = (String) e.nextElement();
			myOut.println("  HDR: '" + value + "' = '"
					+ header.getProperty(value) + "'");
		}
		e = parms.propertyNames();
		// /prm에서 여기서 value== memosite
		while (e.hasMoreElements()) {
			String value = (String) e.nextElement();
			// ///////memosite분기위치에요여기가....
			Log.d("ENUMERATION", "e =" + e);
			if (value.equalsIgnoreCase("memosite")) {

				CmdData cd = new CmdData(value, parms.getProperty(value));
				mHandler.sendMessage(mHandler.obtainMessage(
						NOTIFY_CMD_RECEIVED, cd));

			}
			myOut.println("  PRM: '" + value + "' = '"
					+ parms.getProperty(value) + "'");
		}
		
		Log.e("CHECK", "myRootDir:" + myRootDir);
		return serveFile(uri, header, true);
	}

	/**
	 * HTTP response. Return one of these from serve().
	 */
	public class Response {

		/**
		 * Default constructor: response = HTTP_OK, data = mime = 'null'
		 */
		public Response() {
			this.status = HTTP_OK;
			Log.i("RESPONSE", "response_0");
		}

		/**다
		 * Basic constructor.
		 */
		public Response(String status, String mimeType, InputStream data) {
			this.status = status;
			this.mimeType = mimeType;
			this.data = data;
			Log.i("RESPONSE", "status= " + status + " mimeType= " + mimeType
					+ " data= " + data);
			Log.i("RESPONSE", "response_1");
		}

		/**
		 * Convenience method that makes an InputStream out of given text.
		 */
		public Response(String status, String mimeType, String txt) {
			this.status = status;
			this.mimeType = mimeType;
			try {
				this.data = new ByteArrayInputStream(txt.getBytes("UTF-8"));
			} catch (java.io.UnsupportedEncodingException uee) {
				uee.printStackTrace();
			}

			Log.i("RESPONSE", "status=" + status + "mimeType=" + mimeType
					+ "data=" + data);
			Log.i("RESPONSE", "response2");
		}

		/**
		 * Adds given line to the header.
		 */
		public void addHeader(String name, String value) {

			Log.i("RESPONSE", "name= " + name + "value= " + value);
			header.put(name, value);
		}

		/**
		 * HTTP status code after processing, e.g. "200 OK", HTTP_OK
		 */
		public String status;

		/**
		 * MIME type of content, e.g. "text/html"
		 */
		public String mimeType;

		/**
		 * Data of the response, may be null.
		 */
		public InputStream data;

		/**
		 * Headers for the HTTP response. Use addHeader() to add lines.
		 */
		public Properties header = new Properties();
	}

	/**
	 * Some HTTP response status codes
	 */
	public static final String HTTP_OK = "200 OK",
			HTTP_PARTIALCONTENT = "206 Partial Content",
			HTTP_RANGE_NOT_SATISFIABLE = "416 Requested Range Not Satisfiable",
			HTTP_REDIRECT = "301 Moved Permanently",
			HTTP_NOTMODIFIED = "304 Not Modified",
			HTTP_FORBIDDEN = "403 Forbidden", HTTP_NOTFOUND = "404 Not Found",
			HTTP_BADREQUEST = "400 Bad Request",
			HTTP_INTERNALERROR = "500 Internal Server Error",
			HTTP_NOTIMPLEMENTED = "501 Not Implemented";

	/** 내가 어떤형식의 파이릉ㄹ 보내겠다. 
	 * Common mime types for dynamic content
	 */
	public static final String MIME_PLAINTEXT = "text/plain",
			MIME_HTML = "text/html",
			MIME_DEFAULT_BINARY = "application/octet-stream",
			MIME_XML = "text/xml";

	// ==================================================
	// Socket & server code
	// ==================================================

	/**
	 * Starts a HTTP server to given port.
	 * <p>
	 * Throws an IOException if the socket is already in use
	 */

	public interface CommandReceiver {
		public void onCommandReceived(String cmd, String value);
	}
	private ArrayList<CommandReceiver> mCommandReceivers = new ArrayList<CommandReceiver>();

	public void registerCommandReceiver(CommandReceiver cr) {
		if (!mCommandReceivers.contains(cr))
			mCommandReceivers.add(cr);
	}

	public void unregisterCommandReceiver(CommandReceiver cr) {
		if (mCommandReceivers.contains(cr))
			mCommandReceivers.remove(cr);
	}

	private void notifyCommandReceived(String cmd, String value) {
		if (mService != null) {
			if (cmd.equalsIgnoreCase("screen")) {
				try {
					mService.screenOnOff(value);
				} catch (RemoteException ex) {
					Log.e(TAG, "exception occured when request screen on/off.",
							ex);
				}
				return;
			}
		}
		for (int i = 0; i < mCommandReceivers.size(); i++) {
			CommandReceiver cr = mCommandReceivers.get(i);
			if (cr != null)
				cr.onCommandReceived(cmd, value);
		}
	}
	private EmulatorService mService=null;
	private HandlerThread mHandlerThread = null; // cuzof CTx
	private Handler mHandler = null; //

	private final int NOTIFY_CMD_RECEIVED = 0;

	private class CmdData {
		public String mCmd;
		public String mValue;

		public CmdData(String cmd, String value) {
			mCmd = cmd;
			mValue = value;
		}
	}

	public NanoHTTPD(int port) throws IOException {
		this(null, port);
	}

	public NanoHTTPD(EmulatorService service, int port) throws IOException {
		mService = service;
		myTcpPort = port;
		mHandlerThread = new HandlerThread("PgsServiceHandler");
		mHandlerThread.start();
		mHandler = new Handler(mHandlerThread.getLooper()) {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case NOTIFY_CMD_RECEIVED:
					CmdData cd = (CmdData) msg.obj;
					notifyCommandReceived(cd.mCmd, cd.mValue);
					// case MAKING_HTML:
					// CmdData cd2=(CmdData)msg.obj;

				}
			}
		};

		myServerSocket = new ServerSocket(myTcpPort); // myTcpPort로 소켓 생성
		myThread = new Thread(new Runnable() {
			public void run() {
				try {
					while (true)
						new HTTPSession(myServerSocket.accept());// browser에서
																	// 접속할 때 까지
																	// 대기
				} catch (IOException ioe) {
				}
			}
		});
		myThread.setDaemon(true);
		myThread.start();
	}

	/**
	 * import NanoHTTPD.java; Stops the server.
	 */
	public void stop() {
		try {
			myServerSocket.close();
			myThread.join();
		} catch (IOException ioe) {
		} catch (InterruptedException e) {
		}
	}

	/**
	 * Starts as a standalone file server and waits for Enter.
	 */

	/**
	 * HTTP request를 파싱하고, 반응을 return해 준다. 소켓에서의 아웃풋스트림을, 인풋스트림으로 받아와서 파싱한다. 소켓을
	 * 열면, 거기에 html파일이있었잖아, 그러면 그거를 파싱해주는거지??? 그래서 response를 보내준다~~~ Handles one
	 * session, i.e. parses the HTTP request and returns the response.
	 */
	public class HTTPSession implements Runnable {
		private EmulatorAIDL mService = null;

		public HTTPSession(Socket s) {
			mySocket = s;
			Thread t = new Thread(this);
			t.setDaemon(true);
			t.start();
		}

		public void run() {
			try {
				InputStream is = mySocket.getInputStream();
				if (is == null)
					return;

				// Read the first 8192 bytes.
				// The full header should fit in here.
				// Apache's default header limit is 8KB.
				int bufsize = 8192;
				byte[] buf = new byte[bufsize];
				// rlen is not 8192, it stores 312.
				int rlen = is.read(buf, 0, bufsize); // save data in byte array,
														// buf from 0 to bufsize
				if (rlen <= 0)
					return;

				// Create a BufferedReader for parsing the header.
				ByteArrayInputStream hbis = new ByteArrayInputStream(buf, 0,
						rlen); // 기반스트림으로부터 처리하는거야
				BufferedReader hin = new BufferedReader(new InputStreamReader(
						hbis)); // reader는 문자기반 스트림

				Properties pre = new Properties();
				Properties parms = new Properties();
				Properties header = new Properties();
				Properties files = new Properties();

				// Decode the header into parms and header java properties
				decodeHeader(hin, pre, parms, header);
				String method = pre.getProperty("method");
				String uri = pre.getProperty("uri");

				long size = 0x7FFFFFFFFFFFFFFFl;
				String contentLength = header.getProperty("content-length");
				if (contentLength != null) {
					try {
						size = Integer.parseInt(contentLength);
						Log.i("HEAD", "size:" + size + "contentLength"
								+ contentLength);

					} // size = Integer.paseInt(String)/콘텐트 길이
						// Parses the specified string as a signed decimal
						// integer value.
					catch (NumberFormatException ex) {
					}
				}

				// We are looking for the byte separating header from body.
				// It must be the last byte of the first two sequential new
				// lines.
				int splitbyte = 0;
				boolean sbfound = false;
				while (splitbyte < rlen) {
					if (buf[splitbyte] == '\r' && buf[++splitbyte] == '\n'
							&& buf[++splitbyte] == '\r'
							&& buf[++splitbyte] == '\n') {
						sbfound = true;
						break;
					}
					splitbyte++;
				}
				splitbyte++;

				// Write the part of body already read to ByteArrayOutputStream
				// f
				ByteArrayOutputStream f = new ByteArrayOutputStream();
				if (splitbyte < rlen)
					f.write(buf, splitbyte, rlen - splitbyte);

				// While Firefox sends on the first read all the data fitting
				// our buffer, Chrome and Opera sends only the headers even if
				// there is data for the body. So we do some magic here to find
				// out whether we have already consumed part of body, if we
				// have reached the end of the data to be sent or we should
				// expect the first byte of the body at the next read.
				if (splitbyte < rlen) // 3<10
					size -= rlen - splitbyte + 1;
				else if (!sbfound || size == 0x7FFFFFFFFFFFFFFFl)
					size = 0;
				// Now read all the body and write it to f
				buf = new byte[512]; // 512개,
				while (rlen >= 0 && size > 0) {
					rlen = is.read(buf, 0, 512);
					size -= rlen;
					if (rlen > 0)
						f.write(buf, 0, rlen);
				}
				// Get the raw body as a byte []
				byte[] fbuf = f.toByteArray();

				// Create a BufferedReader for easily reading it as string.
				ByteArrayInputStream bin = new ByteArrayInputStream(fbuf);
				BufferedReader in = new BufferedReader(new InputStreamReader(bin));

				// If the method is POST, there may be parameters
				// in data section, too, read it:
				if (method.equalsIgnoreCase("POST")) {
					Log.i("POST", "inside");

					String contentType = "";
					String contentTypeHeader = header.getProperty("content-type");
					Log.i("POST", "contentTypeHeader:" + contentTypeHeader);
					StringTokenizer st = new StringTokenizer(contentTypeHeader,	"; ");
					if (st.hasMoreTokens()) {
						contentType = st.nextToken();
					}
					Log.i("POST", "contentType:" + contentType);

					// Handle application/x-www-form-urlencoded
					String postLine = "";
					char pbuf[] = new char[512];
					int read = in.read(pbuf);
					while (read >= 0 && !postLine.endsWith("\r\n")) {
						postLine += String.valueOf(pbuf, 0, read);
						read = in.read(pbuf);
					}
					Log.i("POST", "postLine:b4:" + postLine);
					postLine = postLine.trim(); // postLine에서 좌우 빈공간 스페이스 제거하고
					Log.i("POST", "postLine:a4:" + postLine);
					Log.i("POST", "parms" + parms);
					decodeParms(postLine, parms);
					Log.i("POST", "parms= " + parms);
					Log.d("POST",	"pre.getProperty(uri)= " + pre.getProperty("uri"));
					Log.d("POST", "uri = " + uri);
					// uri = "/index3.html";
					Log.d("POST",
							"pre.getProperty(uri)= " + pre.getProperty("uri"));
					Log.d("POST", "uri = " + uri);
				}// end of MULTI_PART아닐 경우
					// end of cast method==POST

				// ////// Ok, now do the serve()
				Response r = serve(uri, method, header, parms);
				if (r == null)
					sendError(HTTP_INTERNALERROR,
							"SERVER INTERNAL ERROR: Serve() returned a null response.");
				else
					// //////////////
					sendResponse(r.status, r.mimeType, r.header, r.data);

				in.close();
				is.close();
			} catch (IOException ioe) {
				try {
					sendError(	HTTP_INTERNALERROR,"SERVER INTERNAL ERROR: IOException:"
									+ ioe.getMessage());
				} catch (Throwable t) {
				}
			} catch (InterruptedException ie) {
				// Thrown by sendError, ignore and exit the thread.
			}
		}

		/**
		 * Decodes the sent headers and loads the data into java Properties' key
		 * - value pairs
		 **/
		private void decodeHeader(BufferedReader in, Properties pre,
				Properties parms, Properties header)
				throws InterruptedException {
			try {
				// Read the request line
				String inLine = in.readLine();
				if (inLine == null)
					return;
				StringTokenizer st = new StringTokenizer(inLine);
				if (!st.hasMoreTokens())
					sendError(HTTP_BADREQUEST,
							"BAD REQUEST: Syntax error. Usage: GET /example/file.html");

				String method = st.nextToken();
				pre.put("method", method);

				if (!st.hasMoreTokens())
					sendError(HTTP_BADREQUEST,
							"BAD REQUEST: Missing URI. Usage: GET /example/file.html");

				String uri = st.nextToken();

				// Decode parameters from the URI
				int qmi = uri.indexOf('?');
				if (qmi >= 0) {
					decodeParms(uri.substring(qmi + 1), parms);
					uri = decodePercent(uri.substring(0, qmi));
				} else
					uri = decodePercent(uri);

				// If there's another token, it's protocol version,
				// followed by HTTP headers. Ignore version but parse headers.
				// NOTE: this now forces header names lowercase since they are
				// case insensitive and vary by client.
				if (st.hasMoreTokens()) {
					String line = in.readLine();
					while (line != null && line.trim().length() > 0) {
						int p = line.indexOf(':');
						if (p >= 0) // 상대편이 받으면, 그걸 보고 상대편이 header값을 받아가지고, 파싱해서
									// 그사람들이 헤더에 맞는 동작을 한
							header.put(line.substring(0, p).trim()
									.toLowerCase(), line.substring(p + 1)
									.trim());
						line = in.readLine();
					}
				}

				pre.put("uri", uri);
			} catch (IOException ioe) {
				sendError(
						HTTP_INTERNALERROR,
						"SERVER INTERNAL ERROR: IOException: "
								+ ioe.getMessage());
			}
		}

		/**
		 * Decodes the Multipart Body data and put it into java Properties' key
		 * - value pairs.
		 **/
		/**
		 * Find the byte positions where multipart boundaries start.
		 **/

		/**
		 * Retrieves the content of a sent file and saves it to a temporary
		 * file. The full path to the saved file is returned.
		 **/
		

		/**
		 * It returns the offset separating multipart file headers from the
		 * file's data.
		 **/

		/**
		 * Decodes the percent encoding scheme. <br/>
		 * For example: "an+example%20string" -> "an example string"
		 */
		private String decodePercent(String str) throws InterruptedException {
			try {
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < str.length(); i++) {
					char c = str.charAt(i);
					switch (c) {
					case '+':
						sb.append(' ');
						break;
					case '%':
						sb.append((char) Integer.parseInt(
								str.substring(i + 1, i + 3), 16));
						i += 2;
						break;
					default:
						sb.append(c);
						break;
					}
				}
				return sb.toString();
			} catch (Exception e) {
				sendError(HTTP_BADREQUEST, "BAD REQUEST: Bad percent-encoding.");
				return null;
			}
		}

		/**
		 * Decodes parameters in percent-encoded URI-format ( e.g.
		 * "name=Jack%20Daniels&pass=Single%20Malt" ) and adds them to given
		 * Properties. NOTE: this doesn't support multiple identical keys due to
		 * the simplicity of Properties -- if you need multiples, you might want
		 * to replace the Properties with a Hashtable of Vectors or such.
		 */
		private void decodeParms(String parms, Properties p)
				throws InterruptedException {
			if (parms == null)
				return;

			String Compare = null;

			StringTokenizer st = new StringTokenizer(parms, "&");
			while (st.hasMoreTokens()) {
				String e = st.nextToken();
				Log.e("e", "" + e);
				int sep = e.indexOf('=');
				if (sep >= 0)
					p.put(decodePercent(e.substring(0, sep)).trim(),
							decodePercent(e.substring(sep + 1)));
				Compare = e.substring(0, sep).trim();
			}
			Log.e("NanoHttpdError", "" + Compare);
			// //여기서 추가
			if (Compare.equalsIgnoreCase("screen")
					|| Compare.equalsIgnoreCase("keyboard")) {
				Log.d("screnn/na", "value=" + p.getProperty(Compare));
				// notifyCommandReceived(Compare, p.getProperty(Compare));
				CmdData cd = new CmdData(Compare, p.getProperty(Compare));
				mHandler.sendMessage(mHandler.obtainMessage(
						NOTIFY_CMD_RECEIVED, cd));
			} else if (Compare.equalsIgnoreCase("keyboard")) {
				CmdData cd = new CmdData(Compare, p.getProperty(Compare));
				mHandler.sendMessage(mHandler.obtainMessage(
						NOTIFY_CMD_RECEIVED, cd));
			} else if (Compare.equalsIgnoreCase("wifi")
					|| Compare.equalsIgnoreCase("bluetooth")) {
				CmdData cd = new CmdData(Compare, p.getProperty(Compare));
				mHandler.sendMessage(mHandler.obtainMessage(
						NOTIFY_CMD_RECEIVED, cd));
			}

			// dot anything.
		}

		/**
		 * Returns an error message as a HTTP response and throws
		 * InterruptedException to stop further request processing.
		 */
		private void sendError(String status, String msg)
				throws InterruptedException {
			sendResponse(status, MIME_PLAINTEXT, null,
					new ByteArrayInputStream(msg.getBytes()));
			throw new InterruptedException();
		}

		/**
		 * Sends given response to the socket.
		 */
		private void sendResponse(String status, String mime,
				Properties header, InputStream data) {
			// data : file경로가 들어있어!!
			try {
				Log.d("sendresponse", "status = " + status);
				Log.d("sendresponse", "mime = " + mime);
				Log.d("sendresponse", "header = " + header);
				Log.d("sendresponse", "InputStream = " + data);

				if (status == null)
					throw new Error("sendResponse(): Status can't be null.");

				OutputStream out = mySocket.getOutputStream();
				PrintWriter pw = new PrintWriter(out);
				pw.print("HTTP/1.0 " + status + " \r\n");

				if (mime != null)
					pw.print("Content-Type: " + mime + "\r\n");

				if (header == null || header.getProperty("Date") == null)
					pw.print("Date: " + gmtFrmt.format(new Date()) + "\r\n");

				if (header != null) {
					Enumeration e = header.keys();
					while (e.hasMoreElements()) {
						String key = (String) e.nextElement();
						String value = header.getProperty(key);
						pw.print(key + ": " + value + "\r\n");
						Log.d("sendresponse", "while) key = " + key
								+ " (value= " + value);
					}
				}

				pw.print("\r\n");
				pw.flush();

				if (data != null) {
					int pending = data.available(); // This is to support
													// partial sends, see
													// serveFile()
					byte[] buff = new byte[theBufferSize];

					char yjk1;
					int i = 0;
					while (pending > 0) {
						int read = data.read(buff, 0,
								((pending > theBufferSize) ? theBufferSize : pending));
						String yjk = new String(buff);
						if (read <= 0)
							break;
						out.write(buff, 0, read);
						pending -= read;
						// Log.d("sendresponse2","string data.read= "+yjk);
					}

				}
				out.flush();
				out.close();
				if (data != null)
					data.close();
			} catch (IOException ioe) {
				// Couldn't write? No can do.
				try {
					mySocket.close();
				} catch (Throwable t) {
				}
			}
		}

		private Socket mySocket;
	}

	/**
	 * URL-encodes everything between "/"-characters. Encodes spaces as '%20'
	 * instead of '+'.
	 */

	public void If(boolean b) {
		// TODO Auto-generated method stub

	}

	private int myTcpPort;
	private final ServerSocket myServerSocket;
	private Thread myThread;
	private File myRootDir;

	// ==================================================
	// File server code
	// ==================================================

	/**
	 * Serves file from homeDir and its' subdirectories (only). Uses only URI,
	 * ignores all headers and HTTP parameters.
	 */
	public Response serveFile(String uri, Properties header, boolean allowDirectoryListing) {
		Log.d("SERVEFILE", "uri=" + uri);

		Log.d("SERVEFILE", "header=" + header);

		Response res = null;
		Log.e("CHECK", "uri:" + uri);
		Log.e("CHECK", "header:" + header);
		Log.e("CHECK", "ADL:" + allowDirectoryListing);

		// 여기까진 들어온다

		// 파일이 디렉토리면 트루, 근데 fulse야 그렇다면 디렉토리 아니라는거지.
		// Make sure we won't die of an exception later
		Log.d("8989", "uir=" + uri + "+");
		Log.d("8989", "len=" + uri.length());
	
		if (res == null) {
			// Get MIME type from file name extension, if possible
			String mime = null;
			mime = (String) theMimeTypes.get("html");

			if (mime == null)
				mime = MIME_DEFAULT_BINARY;

			String context = "<html><head><title>Emulator ver 0.1</title></head><body><textarea rows=1 cols=10>Screen</textarea><text><br></text><text>value<br></text><form method=\"post\"><select name=\"Screen\"><option value=\"on\"selected>on</option><option value=\"off\"selected>off</option></select> <input type=\"submit\"value =\"send\"/></form><text><br><br></text><textarea rows=1 cols=10>Key_Event</textarea><text><br></text><text>key_code<br></text><form method=\"post\"><select name=\"Key_Event\"><option value=\"10\"selected>10</option><option value=\"20\"selected>20</option><option value=\"30\"selected>30</option></select> <input type=\"submit\"value =\"send\"/></form><text><br><br></text><textarea rows=1 cols=10>Power</textarea><text><br></text><text>value<br></text><form method=\"post\"><select name=\"Power\"><option value=\"on\"selected>on</option><option value=\"off\"selected>off</option></select> <input type=\"submit\"value =\"send\"/></form><text><br>Screen : off</text><text><br>Key_Event : <text><br>Power : off</text></body></html>";
			InputStream is = new ByteArrayInputStream(context.getBytes());
			long fileLen = context.length();
			res = new Response(HTTP_OK, mime,is);
			res.addHeader("Content-Length", "" + fileLen);
		}
		res.addHeader("Accept-Ranges", "bytes"); // Announce that the file
													// server accepts partial													// content requestes
		return res;
	}
	
	
	

	public static void getStatus(String cmd, String value){
		Log.d("EXAMPLE","cmd = "+cmd + "value = "+value);
///여기서 스트링만들고..
	}
	
	
	
	

	/**(서버) <> 브라우저 
	 * Hashtable mapping (String)FILENAME_EXTENSION -> (String)MIME_TYPE
	 */
	private static Hashtable theMimeTypes = new Hashtable();
	static {
		StringTokenizer st = new StringTokenizer("css		text/css "
				+ "htm		text/html " + "html		text/html " + "xml		text/xml "
				+ "txt		text/plain " + "asc		text/plain " + "gif		image/gif "
				+ "jpg		image/jpeg " + "jpeg		image/jpeg " + "png		image/png "
				+ "mp3		audio/mpeg " + "m3u		audio/mpeg-url "
				+ "mp4		video/mp4 " + "ogv		video/ogg " + "flv		video/x-flv "
				+ "mov		video/quicktime "
				+ "swf		application/x-shockwave-flash "
				+ "js			application/javascript " + "pdf		application/pdf "
				+ "doc		application/msword " + "ogg		application/x-ogg "
				+ "zip		application/octet-stream "
				+ "exe		application/octet-stream "
				+ "class		application/octet-stream ");
		while (st.hasMoreTokens())
			theMimeTypes.put(st.nextToken(), st.nextToken());

	}

	private static int theBufferSize = 16 * 1024;

	// Change these if you want to log to somewhere else than stdout
	protected static PrintStream myOut = System.out;
	protected static PrintStream myErr = System.err;

	/**
	 * GMT date formatter
	 */
	private static java.text.SimpleDateFormat gmtFrmt;
	static {
		gmtFrmt = new java.text.SimpleDateFormat(
				"E, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
		gmtFrmt.setTimeZone(TimeZone.getTimeZone("GMT"));
	}
	/**
	 * The distribution licence
	 */
	private static final String LICENCE = "Copyright (C) 2001,2005-2011 by Jarno Elonen <elonen@iki.fi>\n"
			+ "and Copyright (C) 2010 by Konstantinos Togias <info@ktogias.gr>\n"
			+ "\n"
			+ "Redistribution and use in source and binary forms, with or without\n"
			+ "modification, are permitted provided that the following conditions\n"
			+ "are met:\n"
			+ "\n"
			+ "Redistributions of source code must retain the above copyright notice,\n"
			+ "this list of conditions and the following disclaimer. Redistributions in\n"
			+ "binary form must reproduce the above copyright notice, this list of\n"
			+ "conditions and the following disclaimer in the documentation and/or other\n"
			+ "materials provided with the distribution. The name of the author may not\n"
			+ "be used to endorse or promote products derived from this software without\n"
			+ "specific prior written permission. \n"
			+ " \n"
			+ "THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR\n"
			+ "IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES\n"
			+ "OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.\n"
			+ "IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,\n"
			+ "INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT\n"
			+ "NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,\n"
			+ "DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY\n"
			+ "THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT\n"
			+ "(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE\n"
			+ "OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.";
}
