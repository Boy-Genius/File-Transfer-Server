package bgu.spl.net.impl.tftp;

import bgu.spl.net.api.MessageEncoderDecoder;

public class TftpEncoderDecoder implements MessageEncoderDecoder<byte[]> {

  private byte[] bytes = new byte[1 << 10]; //start with 1k
  private int len = 0;
  private short opCode;

  @Override
  public byte[] decodeNextByte(byte nextByte) {
    if (len < 2) { // Reading the opcode:
      bytes[len] = nextByte;
      len++;
      if (len == 2) { // Save the opcode as short:
        opCode =
          (short) (((short) bytes[0] & 0xFF) << 8 | (short) (bytes[1] & 0xFF));
      }
    } else { // finnished reading opcode
      if (
        opCode == 1 || //Read request
        opCode == 2 || //Write request
        opCode == 5 || //Error
        opCode == 7 || //Login request
        opCode == 8 || //Delete file request
        opCode == 9 //Brodcast file added/deleted
      ) {
        if (nextByte == 0) {
          len = 0;
          return bytes;
        }
        bytes[len] = nextByte;
        len++;
      }
      if (opCode == 4) {
        bytes[len] = nextByte;
        len++;
        if (len == 4) {
          len = 0;
          return bytes;
        }
      }
      if (opCode == 3) {
        bytes[len] = nextByte;
        len++;

        if (len >= 6) {
          short packetSize = (short) (
            ((short) bytes[2]) << 8 | (short) (bytes[3])
          );
          if (len == 6 + packetSize) {
            len = 0;
            return bytes;
          }
        }
      }
      if (opCode == 10 || opCode == 6) {
        len = 0;
        return bytes;
      }
    }
    return null;
  }

  @Override
  public byte[] encode(byte[] message) {
    return message;
  }
}
