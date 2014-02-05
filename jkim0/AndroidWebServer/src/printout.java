
	/**
	 * Handles one session, i.e. parses the HTTP request
	 * and returns the response.
	 */
	private class HTTPSession implements Runnable
	{
		public HTTPSession( Socket s )
		{
			mySocket = s;
			Thread t = new Thread( this );
			t.setDaemon( true );
			t.start();
		}

		public void run()
		{
			try
			{
				InputStream is = mySocket.getInputStream();
				if ( is == null) return;

				// Read the first 8192 bytes.
				// The full header should fit in here.
				// Apache's default header limit is 8KB.
				int bufsize = 8192;
				byte[] buf = new byte[bufsize];
				int rlen = is.read(buf, 0, bufsize);
				if (rlen <= 0) return;

				// Create a BufferedReader for parsing the header.
				ByteArrayInputStream hbis = new ByteArrayInputStream(buf, 0, rlen);
				BufferedReader hin = new BufferedReader( new InputStreamReader( hbis ));
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
				if (contentLength != null)
				{
					try { size = Integer.parseInt(contentLength); }
					catch (NumberFormatException ex) {}
				}

				// We are looking for the byte separating header from body.
				// It must be the last byte of the first two sequential new lines.
				int splitbyte = 0;
				boolean sbfound = false;
				while (splitbyte < rlen)
				{
					if (buf[splitbyte] == '\r' && buf[++splitbyte] == '\n' && buf[++splitbyte] == '\r' && buf[++splitbyte] == '\n') {
						sbfound = true;
						break;
					}
					splitbyte++;
				}
				splitbyte++;

				// Write the part of body already read to ByteArrayOutputStream f
				ByteArrayOutputStream f = new ByteArrayOutputStream();
				if (splitbyte < rlen) f.write(buf, splitbyte, rlen-splitbyte);

				// While Firefox sends on the first read all the data fitting
				// our buffer, Chrome and Opera sends only the headers even if
				// there is data for the body. So we do some magic here to find
				// out whether we have already consumed part of body, if we
				// have reached the end of the data to be sent or we should
				// expect the first byte of the body at the next read.
				if (splitbyte < rlen)
					size -= rlen - splitbyte +1;
				else if (!sbfound || size == 0x7FFFFFFFFFFFFFFFl)
					size = 0;

				// Now read all the body and write it to f
				buf = new byte[512];
				while ( rlen >= 0 && size > 0 )
				{
					rlen = is.read(buf, 0, 512);
					size -= rlen;
					if (rlen > 0)
						f.write(buf, 0, rlen);
				}

				// Get the raw body as a byte []
				byte [] fbuf = f.toByteArray();

				// Create a BufferedReader for easily reading it as string.
				ByteArrayInputStream bin = new ByteArrayInputStream(fbuf);
				BufferedReader in = new BufferedReader( new InputStreamReader(bin));

				// If the method is POST, there may be parameters
				// in data section, too, read it:
				if ( method.equalsIgnoreCase( "POST" ))
				{
					String contentType = "";
					String contentTypeHeader = header.getProperty("content-type");
					StringTokenizer st = new StringTokenizer( contentTypeHeader , "; " );
					if ( st.hasMoreTokens()) {
						contentType = st.nextToken();
					}

					if (contentType.equalsIgnoreCase("multipart/form-data"))
					{
						// Handle multipart/form-data
						if ( !st.hasMoreTokens())
							sendError( HTTP_BADREQUEST, "BAD REQUEST: Content type is multipart/form-data but boundary missing. Usage: GET /example/file.html" );
						String boundaryExp = st.nextToken();
						st = new StringTokenizer( boundaryExp , "=" );
						if (st.countTokens() != 2)
							sendError( HTTP_BADREQUEST, "BAD REQUEST: Content type is multipart/form-data but boundary syntax error. Usage: GET /example/file.html" );
						st.nextToken();
						String boundary = st.nextToken();

						decodeMultipartData(boundary, fbuf, in, parms, files);
					}
					else
					{
						// Handle application/x-www-form-urlencoded
						String postLine = "";
						char pbuf[] = new char[512];
						int read = in.read(pbuf);
						while ( read >= 0 && !postLine.endsWith("\r\n") )
						{
							postLine += String.valueOf(pbuf, 0, read);
							read = in.read(pbuf);
						}
						postLine = postLine.trim();
						decodeParms( postLine, parms );
					}
				}

				if ( method.equalsIgnoreCase( "PUT" ))
					files.put("content", saveTmpFile( fbuf, 0, f.size()));

				// Ok, now do the serve()
				Response r = serve( uri, method, header, parms, files );
				if ( r == null )
					sendError( HTTP_INTERNALERROR, "SERVER INTERNAL ERROR: Serve() returned a null response." );
				else
					sendResponse( r.status, r.mimeType, r.header, r.data );

				in.close();
				is.close();
			}
			catch ( IOException ioe )
			{
				try
				{
					sendError( HTTP_INTERNALERROR, "SERVER INTERNAL ERROR: IOException: " + ioe.getMessage());
				}
				catch ( Throwable t ) {}
			}
			catch ( InterruptedException ie )
			{
				// Thrown by sendError, ignore and exit the thread.
			}
		}

		/**
		 * Decodes the sent headers and loads the data into
		 * java Properties' key - value pairs
		**/
		private  void decodeHeader(BufferedReader in, Properties pre, Properties parms, Properties header)
			throws InterruptedException
		{
			try {
				// Read the request line
				String inLine = in.readLine();
				if (inLine == null) return;
				StringTokenizer st = new StringTokenizer( inLine );
				if ( !st.hasMoreTokens())
					sendError( HTTP_BADREQUEST, "BAD REQUEST: Syntax error. Usage: GET /example/file.html" );

				String method = st.nextToken();
				pre.put("method", method);

				if ( !st.hasMoreTokens())
					sendError( HTTP_BADREQUEST, "BAD REQUEST: Missing URI. Usage: GET /example/file.html" );

				String uri = st.nextToken();

				// Decode parameters from the URI
				int qmi = uri.indexOf( '?' );
				if ( qmi >= 0 )
				{
					decodeParms( uri.substring( qmi+1 ), parms );
					uri = decodePercent( uri.substring( 0, qmi ));
				}
				else uri = decodePercent(uri);

				// If there's another token, it's protocol version,
				// followed by HTTP headers. Ignore version but parse headers.
				// NOTE: this now forces header names lowercase since they are
				// case insensitive and vary by client.
				if ( st.hasMoreTokens())
				{
					String line = in.readLine();
					while ( line != null && line.trim().length() > 0 )
					{
						int p = line.indexOf( ':' );
						if ( p >= 0 )
							header.put( line.substring(0,p).trim().toLowerCase(), line.substring(p+1).trim());
						line = in.readLine();
					}
				}

				pre.put("uri", uri);
			}
			catch ( IOException ioe )
			{
				sendError( HTTP_INTERNALERROR, "SERVER INTERNAL ERROR: IOException: " + ioe.getMessage());
			}
		}
