package edu.cmu.cs214.hw6;

import java.io.IOException;

import fi.iki.elonen.NanoHTTPD;

/**
 * Hello world!
 *
 */
public class App extends NanoHTTPD {
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
    }

    public App() throws IOException {
        super(8080);

        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("\nRunning!\n");
    }

    @Override
    public Response serve(IHTTPSession session) {
        Response response = newFixedLengthResponse("");
        return response;
    }
}
