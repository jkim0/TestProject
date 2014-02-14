package com.example.nanohttpd_change;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.TimeZone;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {
	private static final String TAG = "WebServer";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		Log.e(TAG, "############## strat = " + "nanohttpd");

		try {
			new NanoHTTPD(8095);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

class NanoHTTPD {
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
	public Response serve(String uri, String method, Properties header,
			Properties parms) {
		Log.e("Respose_serve", "_serve_start");
		myOut.println(method + " '" + uri + "' ");
		// adp locgcat으로 출력 내용을 볼수 있다.
		// myOut = System.out;

		Enumeration e = header.propertyNames();
		Log.e("Enumeration_e", "" + e);
		while (e.hasMoreElements()) {
			String value = (String) e.nextElement();
			Log.e("headers_value", "" + value);

			myOut.println("  HDR: '" + value + "' = '"
					+ header.getProperty(value) + "'");
		}// header key, value확인! 안드로이드 애플리케이션 프로젝트는 콘솔창에
			// println 출력 안됨.! adb logcat으로 확인 할 수 있다. HDR->header
		e = parms.propertyNames();
		while (e.hasMoreElements()) {
			String value = (String) e.nextElement();
			Log.e("parms_value", "" + value);
			myOut.println("  PRM: '" + value + "' = '"
					+ parms.getProperty(value) + "'");
		}// parms의 key, value확인 PRM->parms , PRM:'screen' = '1'
		
		Log.e("myRootDir", "" + myRootDir);// /storage/sdcard 출력
		Log.e("serve_uri", "" + uri); // method가 POST 일때 / ,
		
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
		}

		/**
		 * Basic constructor.
		 */
		public Response(String status, String mimeType, InputStream data) {
			Log.e("Response_status", "" + status);
			Log.e("Response_mimeType", "" + mimeType);
			Log.e("Response_data", "" + data);
			this.status = status;
			this.mimeType = mimeType;
			this.data = data;
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
		}

		/**
		 * Adds given line to the header.
		 */
		public void addHeader(String name, String value) {
			Log.e("addHeader_name", "" + name);
			Log.e("addHeader_value", "" + value);
			header.put(name, value);
			Enumeration addHeader = header.propertyNames();
			while (addHeader.hasMoreElements()) {
				String check_addvalue = (String) addHeader.nextElement();
				Log.e("headers_value", "" + check_addvalue);

				Log.e("addHeader", "" + header.getProperty(check_addvalue));
			}
			// ETag -> 7ac7d491, Accept-Ranges -> bytes
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

	/**
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
	public NanoHTTPD(int port) throws IOException {

		myTcpPort = port;
		myServerSocket = new ServerSocket(myTcpPort); // port번호가 8090인 socket
														// 인스턴스 생성
		myThread = new Thread(new Runnable() {

			public void run() {
				try {
					Log.e("Therad", "start");
					while (true) {
						Log.e("Before_HTTPSession", "" + myServerSocket);
						// myServerSocket =
						// ServerSocket[addr=0.0.0.0/0.0.0.0,port=0,localport=8090]
						// socket에 정보가 없으므로 addr과 port는 0 이다.
						new HTTPSession(myServerSocket.accept());
						// 인터넷에 접속 할때 까지 대기. 연결 되면socket을 리턴한다.
						// 인터텟 접속하면 HTTPSession으로 이동!
						Log.e("after_HTTPSession", "" + myServerSocket);
					}
				} catch (IOException ioe) {
				}
			}
		});
		myThread.setDaemon(true);
		myThread.start();
	}

	/**
	 * Stops the server.
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
	 * Handles one session, i.e. parses the HTTP request and returns the
	 * response.
	 */
	private class HTTPSession implements Runnable {
		public HTTPSession(Socket s) {
			Log.e("StartHTTPSession", "" + s);
			// (Socket[address=/127.0.0.1,port=59039,localPort=8090])
			// Screen on click 후 HTTPSession에서 부터 다시시작.
			// 소켓의 포트 정보가 변해서 HTTPSession을 다시 호출 하는 것 같다.
			// (Socket[address=/127.0.0.1,port=59040,localPort=8090])
			mySocket = s;// mySocket에 소켓 저장.
			Thread t = new Thread(this);

			t.setDaemon(true);
			t.start();
			// new HTTPSession( myServerSocket.accept()); 호출
			// 바로 run()으로 시작 된다.
		}

		public void run() {
			try {
				Log.e("StartHTTPSession_run", "" + mySocket);
				// Screeon on click 전
				// Socket[address=/127.0.0.1,port=59039,localPort=8090]
				// Screen on click 후
				// Socket[address=/127.0.0.1,port=59040,localPort=8090]
				// 소켓 정보가 변경됨.
				InputStream is = mySocket.getInputStream();
				// Returns an InputStream to read data from this socket.
				// return The InputStream object.
				Log.e("InputStream", "" + is);
				// java.net.PlainSocketImpl$PlainSocketInputStream@4178d8a8
				// 객체 번호.
				if (is == null)
					return;

				// Read the first 8192 bytes.
				// The full header should fit in here.
				// Apache's default header limit is 8KB.
				int bufsize = 8192;
				byte[] buf = new byte[bufsize];// byte 배열 생성
				Log.e("bufsize=8192", "" + buf);
				int rlen = is.read(buf, 0, bufsize);

				// Reads up to byteCount bytes(8192) from this stream
				// and stores them in the byte array buffer(buf) starting at
				// byteOffset(0).
				// Returns the number of bytes actually read
				// or -1 if the end of the stream has been reached.
				// This method read bytes from a stream and stores them into a
				// caller supplied buffer.

				if (rlen <= 0)
					return;
				char[] check_buf = new char[rlen + 1];
				Log.e("HTTPSession_run_rlen", "" + rlen);

				// Create a BufferedReader for parsing the header.
				ByteArrayInputStream hbis = new ByteArrayInputStream(buf, 0,rlen);
				// this.mark = 0;
				// this.buf = buf;
				// this.count = buf.length;
				// ByteArrayInputStream 의 buf가 buf를 참고 할 수 있게 한다.this.buf = buf;
				// /libcore/luni/src/main/java/java/io/ByteArrayInputStream.java
				// 참고!

				Log.e("ByteArrayInputStream hbis_buf.length", "" + buf.length); // 8192

				Log.e("ByteArrayInputSream_hbis", "" + hbis);
				BufferedReader hin = new BufferedReader(new InputStreamReader(hbis));
				// InputStreamReader는 Charset을 이용하여 8bit(byte)로된 InputStream 내용을
				// Char(16bits)로 변환
				// BufferedReader는 char배열은 만든다.! buf = new char[size]; size는
				// defalut일 경우 8192!

				Properties pre = new Properties();
				Properties parms = new Properties();
				Properties header = new Properties();
				Properties files = new Properties();

				Log.e("before_decodeHeader_hin", "" + hin);
				Log.e("before_decodeHeader_pre", "" + pre);
				Log.e("before_decodeHeader_parms", "" + parms);
				Log.e("before_decodeHeader_header", "" + header);

				// Decode the header into pre and header java properties
				decodeHeader(hin, pre, parms, header);

				Log.e("after_decodeHeader_hin", "" + hin);
				Log.e("after_decodeHeader_pre", "" + pre); 
				Log.e("after_decodeHeader_parms", "" + parms); 
				Log.e("after_decodeHeader_header", "" + header);

				String method = pre.getProperty("method");
				Log.e("method_pre.getProperty", "" + method); // GET 출력 , 클릭 후 POST 출력
				String uri = pre.getProperty("uri");
				Log.e("uri_pre.getProperty", "" + uri); // / 출력
				long size = 0x7FFFFFFFFFFFFFFFl;
				Log.e("first_size", "" + size); // 9223372036854775807 출력
				String contentLength = header.getProperty("content-length");

				Log.e("contentLength_header.getProperty", "" + contentLength);
				// null 출력 header에 content-length key 값이 없어서.
				// 클릭 후 header에 contentLength이 존재, 8 출력

				if (contentLength != null) {
					try {
						size = Integer.parseInt(contentLength);
						// Converts the specified String into an int.
						Log.e("contentLength_size", "" + size);// 8
					} catch (NumberFormatException ex) {
					}
				}

				// We are looking for the byte separating header from body.
				// It must be the last byte of the first two sequential new
				// lines.
				int splitbyte = 0;
				boolean sbfound = false;

				while (splitbyte < rlen) {
					// Log.e("splibyte < rlen","splitbyte: "+splitbyte+", rlen: "+rlen);
					if (buf[splitbyte] == '\r' && buf[++splitbyte] == '\n'
							&& buf[++splitbyte] == '\r'
							&& buf[++splitbyte] == '\n') {
						// buf에 '\r', '\n', '\r', '\n'이 연속적으로 있는 곳을 확인.
						// Log.e("splibyte_true_check",""+splitbyte); // 클릭전 435
						// , 클릭후 551
						sbfound = true;
						break; // while문 빠져나온다.중요!!
					}
					splitbyte++;
				}
				splitbyte++;

				ByteArrayOutputStream f = new ByteArrayOutputStream();

				if (splitbyte < rlen) {

					Log.e("splitbyte < rlen", "" + true);
					f.write(buf, splitbyte, rlen - splitbyte);
					// buf splibyte부터 rlen-splitebyte만큼 내용을 f 객체의 buf에 저장!
					// system.arraraycopy 이용
					// write 할때 f의 cout를 증가시킨다.
				}

				// While Firefox sends on the first read all the data fitting
				// our buffer, Chrome and Opera sends only the headers even if
				// there is data for the body. So we do some magic here to find
				// out whether we have already consumed part of body, if we
				// have reached the end of the data to be sent or we should
				// expect the first byte of the body at the next read.
				if (splitbyte < rlen) {
					size -= rlen - splitbyte + 1; // //size = size -
													// rlen+splitbyte-1;
					Log.e("size -= rlen - splitbyte +1", "" + size);
				} else if (!sbfound || size == 0x7FFFFFFFFFFFFFFFl) { 
					size = 0;
				}

				// Now read all the body and write it to f
				buf = new byte[512];
				Log.e("bufsize=512", "" + buf);
				while (rlen >= 0 && size > 0) // size가 0 이어서 while에 못들어감. , 클릭후
												// size가 -1 조건을 만족 못함!
				{
					rlen = is.read(buf, 0, 512);
					Log.e("changed_rlen", "changed_rlen=" + rlen
							+ " changed_size= " + size);
					size -= rlen;
					Log.e("size -= rlen", "" + size);
					if (rlen > 0)
						f.write(buf, 0, rlen);
				}

				// Get the raw body as a byte []
				byte[] fbuf = f.toByteArray();
				// byte[] newArray = new byte[count];
				// System.arraycopy(buf, 0, newArray, 0, count);
				// return newArray;
				// 위 함수를 이용하여 f에서 newArray byte배열을 만든후
				// newArray를 가리키는 메모리값을 리턴한다.

				// Create a BufferedReader for easily reading it as string.
				Log.e("fbuf_length", "" + fbuf.length); // 클릭 후 8 출력
				ByteArrayInputStream bin = new ByteArrayInputStream(fbuf);
				// this.buf = buf;
				// bin의 byte배열인 buf가 fbuf를 가리키는 인스턴스를 생성
				BufferedReader in = new BufferedReader(new InputStreamReader(bin));

				// If the method is POST, there may be parameters
				// in data section, too, read it:
				if (method.equalsIgnoreCase("POST")) // click하기전 method에 POST
														// key 가 없다! 클릭후 POST가
														// 있다.
				{
					Log.e("POST", "IN_POT");
					String contentType = "";
					String contentTypeHeader = header
							.getProperty("content-type");
					Log.e("contentTypeHeader", "" + contentTypeHeader);
					StringTokenizer st = new StringTokenizer(contentTypeHeader,
							"; ");
					if (st.hasMoreTokens()) {
						Log.e(";_true", "" + true);
						contentType = st.nextToken();
						Log.e(";_true", "" + contentType);
					}

					// Handle application/x-www-form-urlencoded
					String postLine = "";
					char pbuf[] = new char[512];
					int read = in.read(pbuf);
					Log.e("read", "" + read); // 8 출력.
					while (read >= 0 && !postLine.endsWith("\r\n")) {
						// .endsWith This method tests if this string ends with
						// the specified suffix.
						postLine += String.valueOf(pbuf, 0, read);
						Log.e("postLine", "" + postLine); // screen=1 출력
						read = in.read(pbuf);
						Log.e("while_read", "" + read); // -1 출력
					}
					postLine = postLine.trim();
					Log.e("after_postLine", "" + postLine);
					decodeParms(postLine, parms);

				}
				// click하기전 method에 PUT 없음.!

				// Ok, now do the serve()
				//Response r = serve(uri, method, header, parms, files);
				Response r = serve(uri, method, header, parms);
				if (r == null)
					sendError(HTTP_INTERNALERROR,
							"SERVER INTERNAL ERROR: Serve() returned a null response.");
				else {
					Log.e("sendResponse", "sendResponse");
					sendResponse(r.status, r.mimeType, r.header, r.data);
				}

				in.close();
				is.close();
			} catch (IOException ioe) {
				try {
					sendError(
							HTTP_INTERNALERROR,
							"SERVER INTERNAL ERROR: IOException: "
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
				Log.e("decodeHeader_inLine", "" + inLine);
				// GET / HTTP/1.1 BufferedReader in(HTTPSession의 hin)의 버퍼에서 한줄
				// 읽어 들인다.
				// screeon on 클릭후 >> POST / HTTP/1.1
				if (inLine == null)
					return;
				StringTokenizer st = new StringTokenizer(inLine);
				// String inLine에서 공백을 이용 token으로 끊어준다. GET, / , HTTP/1.1
				// POST, / , HTTP/1.1
				if (!st.hasMoreTokens()) // 리턴할 다음 토큰이 있으면 true를 다음 토큰이 없으면
											// false를 리턴한다.
					sendError(HTTP_BADREQUEST,
							"BAD REQUEST: Syntax error. Usage: GET /example/file.html");

				String method = st.nextToken(); // 다음 토큰을 리턴한다. 이전 토큰은 제거한다.
				Log.e("method", "" + method); // GET 출력 , 클릭 후 POST 출력
				pre.put("method", method);
				// Associate the specified value with the specified key in this
				// Hashtable.
				// preoperties extends hashtable 을 해서 properties에서 hashtable
				// 메소드를 사용할 수 있다.
				Log.e("pre", "" + pre); // {method=GET} 출력 , 클릭 후 {method=POST}

				if (!st.hasMoreTokens())
					sendError(HTTP_BADREQUEST,
							"BAD REQUEST: Missing URI. Usage: GET /example/file.html");

				String uri = st.nextToken();
				Log.e("decodeHeader_uri", "" + uri); // / 출력

				// Decode parameters from the URI
				int qmi = uri.indexOf('?');
				// uri 문자열에서 특정문자의 위치를 구 할 수 있다. 문자가 없으면 -1 반환
				Log.e("qmi", "" + qmi); // -1 출력
				if (qmi >= 0) {
					Log.e("qmi>=0", "true");
					decodeParms(uri.substring(qmi + 1), parms);
					uri = decodePercent(uri.substring(0, qmi));
				} else
					uri = decodePercent(uri);
				// uri에 + 나 %로 인코딩된것을 디코딩 하기 위해서 decodePerect를 호출. 디코딩 한 것을
				// string으로 반환.!

				Log.e("decodeHeader_uri", "" + uri);// / 출력.!( +나 %가 없었다.)

				// If there's another token, it's protocol version,
				// followed by HTTP headers. Ignore version but parse headers.
				// NOTE: this now forces header names lowercase since they are
				// case insensitive and vary by client.
				if (st.hasMoreTokens()) {
					String line = in.readLine();
					Log.e("HTTP headers", "" + line); // Host: localhost:8090 출력
					Log.e("line.trim()", "" + line.trim());
					// line.trim() line의 처음과 끝의 공백을 제거한다. Host: localhost:8090
					// 출력
					while (line != null && line.trim().length() > 0) 
					{
						int p = line.indexOf(':'); // line 안에 : 특정 문자(:)가 시작되는
													// 인덱스를 리턴한다.!
						Log.e("HTTP_header_line.indexof( : )", "" + p);
						Log.e("line_in_while", "in_while");
						if (p >= 0) {
							// key
							Log.e("line.substring(0,p)", line.substring(0, p));
							// line.substring 문자열의 시작위치(0)에서 끝위치(p)까지의 문자를 뽑아내게
							// 된다.
							// 단 끝위치는 포함이 안된다
							Log.e("line.substring(0,p).trim()", ""
									+ line.substring(0, p).trim());// 처음과 끝의 공백
																	// 제거
							Log.e("line.substring(0,p).trim().toLowerCase()",
									""
											+ line.substring(0, p).trim()
													.toLowerCase());// 모두 소문자로
																	// 변경

							// value
							Log.e("line.substring(p+1)", line.substring(p + 1));// line
																				// 에서
																				// p+1
																				// 부터
																				// 끝의
																				// 문자를
																				// 생성해서
																				// 반환.
							Log.e("line.substring(p+1).trim()",
									line.substring(p + 1).trim());// 처음과 끝의 공백
																	// 제거

							// put(key,value)
							header.put(line.substring(0, p).trim()
									.toLowerCase(), line.substring(p + 1)
									.trim());
							// The put(K key, V value) method is used
							// to map the specified key to the specified value
							// in this hashtable.
							// .getProperty(key) 를 하면 value 값을 return 한다.
							Log.e("Http_header_header.put", "" + header);
						}
						line = in.readLine();
						Log.e("HTTP_header_next_line", "" + line);
					}
				}
				Log.e("before_pre.put(uri, uri)", "" + pre); // {method=GET} ,
															// 클릭 후
																// {method=POST}
				pre.put("uri", uri);
				Log.e("after_pre.put(uri, uri)", "" + pre); // {uri=/,
															// method=GET}, 클릭 후
															// {uri=/,
															// method=POST}
				// HTTPSession으로 돌아간다!
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
				Log.e("decodePercent_str->uri", "" + str);
				// / 출력
				// 클릭 후 decodeparms-> decodePercent( e.substring( 0, sep ))
				// screen 출력
				// 클릭 후 decodeparms-> e.substring( sep+1 )) 1 출력
				StringBuffer sb = new StringBuffer();
				Log.e("decodePercent_str.length", "" + str.length());
				// 1 출력
				// 클릭 후 decodePercent( e.substring( 0, sep )) , 6출력
				// 클릭 후 decodeparms-> e.substring( sep+1 )) , 1 출력
				for (int index = 0; index < str.length(); index++)
					Log.e("str_print", "" + str.charAt(index));// / 출력

				// Log.e("str_print_null",null);
				for (int i = 0; i < str.length(); i++) {

					char c = str.charAt(i); // index로 지정한 string의 특정 offset(좌표)에
											// 문자를 반환
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
				Log.e("sb.toString", "" + sb.toString());
				// / 출력
				// 클릭 후 decodePercent( e.substring( 0, sep )) 호출 screen 출력
				// 클릭 후 decodeparms-> e.substring( sep+1 )) 1 출력
				return sb.toString();// string 자료 형으로 변경.
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

			StringTokenizer st = new StringTokenizer(parms, "&");
			Log.e("decodeParms_st", "" + st);
			while (st.hasMoreTokens()) {
				String e = st.nextToken();
				int sep = e.indexOf('=');
				Log.e("e", "" + e);// screen=1
				Log.e("sep", "" + sep); // 6
				Log.e("sep_key", "" + e.substring(0, sep));// screen
				Log.e("sep_value", "" + e.substring(sep + 1));// 1

				if (sep >= 0)
					p.put(decodePercent(e.substring(0, sep)).trim(),
							decodePercent(e.substring(sep + 1)));
				Log.e("after_p", "" + p);
			}
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
			try {
				if (status == null)
					throw new Error("sendResponse(): Status can't be null.");

				OutputStream out = mySocket.getOutputStream();
				PrintWriter pw = new PrintWriter(out);
				pw.print("HTTP/1.0 " + status + " \r\n");
				// This method prints a String to the stream.
				// The actual value printed depends on the system default
				// encoding.

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
					}
				}

				pw.print("\r\n");
				pw.flush();

				if (data != null) {
					int pending = data.available(); // This is to support
													// partial sends, see
													// serveFile()
					Log.e("pending", "" + pending);
					// This method returns the number of bytes that can be read
					// from this stream before a read can block.
					// 403 출력
					byte[] buff = new byte[theBufferSize]; // theBufferSize =
															// 16* 1024
					while (pending > 0) {
						int read = data.read(buff, 0,
								((pending > theBufferSize) ? theBufferSize
										: pending));
						// This method read bytes from a stream and stores them
						// into a caller supplied buffer.
						Log.e("sendResponse_read", "" + buff);
						
						if (read <= 0)
							break;
						out.write(buff, 0, read);
						pending -= read;
					}
				}
				out.flush();
				out.close();
				if (data != null)
					data.close();
			} catch (IOException ioe) {
				// Couldn't write? No can do.
				try {
					Log.e("socketclose", "" + true);
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
	public Response serveFile(String uri, Properties header,boolean allowDirectoryListing) {
		Log.e("serveFile", "start_serveFile");
		Response res = null;


		if (res == null) {
			// Get MIME type from file name extension, if possible			
			String mime = null;
			mime = (String) theMimeTypes.get("html");
					
			if (mime == null) {
				mime = MIME_DEFAULT_BINARY;
				Log.e("mime == null", "" + mime);
			}

			// Change return code and add Content-Range header when skipping
			// is requested
			String context = "<html><head><title>Emulator ver 0.1</title></head><body><textarea rows=1 cols=10>Screen</textarea><text><br></text><text>value<br></text><form method=\"post\"><select name=\"Screen\"><option value=\"on\"selected>on</option><option value=\"off\"selected>off</option></select> <input type=\"submit\"value =\"send\"/></form><text><br><br></text><textarea rows=1 cols=10>Key_Event</textarea><text><br></text><text>key_code<br></text><form method=\"post\"><select name=\"Key_Event\"><option value=\"10\"selected>10</option><option value=\"20\"selected>20</option><option value=\"30\"selected>30</option></select> <input type=\"submit\"value =\"send\"/></form><text><br><br></text><textarea rows=1 cols=10>Power</textarea><text><br></text><text>value<br></text><form method=\"post\"><select name=\"Power\"><option value=\"on\"selected>on</option><option value=\"off\"selected>off</option></select> <input type=\"submit\"value =\"send\"/></form><text><br>Screen : off</text><text><br>Key_Event : <text><br>Power : off</text></body></html>";
			InputStream is = new ByteArrayInputStream(context.getBytes());
			long fileLen = context.length();
			
			res = new Response(HTTP_OK, mime, is);
			res.addHeader("Content-Length", "" + fileLen);
		}
		res.addHeader("Accept-Ranges", "bytes"); // Announce that the file
													// server accepts partial												// content requestes
		Log.e("return_res", "return_res");
		return res;
	}

	/**
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
