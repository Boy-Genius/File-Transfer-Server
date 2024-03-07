package bgu.spl.net.impl.tftp;

import bgu.spl.net.impl.echo.EchoProtocol;
import bgu.spl.net.impl.echo.LineMessageEncoderDecoder;
import bgu.spl.net.srv.Connections;
import bgu.spl.net.srv.Server;

public class TftpServer {

  public static void main(String[] args) {
    // you can use any server you like baby...

    Server
      .threadPerClient(
        7777, //port
        () -> new TftpProtocol(), //protocol factory
        () -> new TftpEncoderDecoder(), //message encoder decoder factory
        new ConnectionsTftp()
      )
      .serve();
  }
}
