package utilities.gl;

import java.awt.image.*;
import java.io.*;
import java.util.*;

import utilities.log.*;

/**
 * Windows bitmap file loader.
 * @author Abdul Bezrati
 * @author Pepijn Van Eeckhoudt
 */
public class BitmapLoader {
	
	public static Log log = LogFactory.getLog(BitmapLoader.class);
	
    public static BufferedImage loadBitmap(String file) throws IOException {
        BufferedImage image;
        InputStream input = null;
        try {
            input = ResourceRetriever.getResourceAsStream(file);

            int bitmapFileHeaderLength = 14;
            int bitmapInfoHeaderLength = 40;

            byte bitmapFileHeader[] = new byte[bitmapFileHeaderLength];
            byte bitmapInfoHeader[] = new byte[bitmapInfoHeaderLength];

            input.read(bitmapFileHeader, 0, bitmapFileHeaderLength);
            input.read(bitmapInfoHeader, 0, bitmapInfoHeaderLength);

            @SuppressWarnings("unused")
			int nSize = bytesToInt(bitmapFileHeader, 2);
            int nWidth = bytesToInt(bitmapInfoHeader, 4);
            int nHeight = bytesToInt(bitmapInfoHeader, 8);
            @SuppressWarnings("unused")
            int nBiSize = bytesToInt(bitmapInfoHeader, 0);
            @SuppressWarnings("unused")
            int nPlanes = bytesToShort(bitmapInfoHeader, 12);
            int nBitCount = bytesToShort(bitmapInfoHeader, 14);
            int nSizeImage = bytesToInt(bitmapInfoHeader, 20);
            @SuppressWarnings("unused")
            int nCompression = bytesToInt(bitmapInfoHeader, 16);
            int nColoursUsed = bytesToInt(bitmapInfoHeader, 32);
            @SuppressWarnings("unused")
            int nXPixelsMeter = bytesToInt(bitmapInfoHeader, 24);
            @SuppressWarnings("unused")
            int nYPixelsMeter = bytesToInt(bitmapInfoHeader, 28);
            @SuppressWarnings("unused")
            int nImportantColours = bytesToInt(bitmapInfoHeader, 36);

            if (nBitCount == 24) {
                image = read24BitBitmap(nSizeImage, nHeight, nWidth, input);
            } else if (nBitCount == 8) {
                image = read8BitBitmap(nColoursUsed, nBitCount, nSizeImage, nWidth, nHeight, input);
            } else {
            	log.error("loadBitmap() Not a 24-bit or 8-bit Windows Bitmap, aborting...", new UnknownFormatConversionException(null));                
                image = null;
            }
        } finally {
            try {
                if (input != null)
                    input.close();
            } catch (IOException e) {
            	log.error("loadBitmap() Fehler beim Lesen der Texture", e);
            }
        }
        return image;
    }

    private static BufferedImage read8BitBitmap(int nColoursUsed, int nBitCount, int nSizeImage, int nWidth, int nHeight, InputStream input) throws IOException {
        int nNumColors = (nColoursUsed > 0) ? nColoursUsed : (1 & 0xff) << nBitCount;

        if (nSizeImage == 0) {
            nSizeImage = ((((nWidth * nBitCount) + 31) & ~31) >> 3);
            nSizeImage *= nHeight;
        }

        int npalette[] = new int[nNumColors];
        byte bpalette[] = new byte[nNumColors * 4];
        readBuffer(input, bpalette);
        int nindex8 = 0;

        for (int n = 0; n < nNumColors; n++) {
        	
            npalette[n] = (255 & 0xff) << 24 |
                    (bpalette[nindex8 + 2] & 0xff) << 16 |
                    (bpalette[nindex8 + 1] & 0xff) << 8 |
                    (bpalette[nindex8 + 0] & 0xff);

            nindex8 += 4;
        }

        int npad8 = (nSizeImage / nHeight) - nWidth;
        int[][] bankData = new int[nWidth][nHeight];
        byte bdata[] = new byte[(nWidth + npad8) * nHeight];

        readBuffer(input, bdata);
        nindex8 = 0;

        for (int j8 = nHeight - 1; j8 >= 0; j8--) {
            for (int i8 = 0; i8 < nWidth; i8++) {
                bankData[j8][i8] = npalette[((int) bdata[nindex8] & 0xff)];
                nindex8++;
            }
            nindex8 += npad8;
        }
        return null;
    }

    private static BufferedImage read24BitBitmap(int nSizeImage, int nHeight, int nWidth, InputStream input) throws IOException {
        int npad = (nSizeImage / nHeight) - nWidth * 3;
        if (npad == 4 || npad < 0)
            npad = 0;
        int nindex = 0;
        BufferedImage bufferedImage = new BufferedImage(nWidth, nHeight, BufferedImage.TYPE_4BYTE_ABGR);
        DataBufferByte dataBufferByte = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer());
        byte[][] bankData = dataBufferByte.getBankData();
        byte brgb[] = new byte[(nWidth + npad) * 3 * nHeight];

        readBuffer(input, brgb);

        for (int j = nHeight - 1; j >= 0; j--) {
            for (int i = 0; i < nWidth; i++) {
                int base = (j * nWidth + i) * 4;
                bankData[0][base] = (byte) 255;
                bankData[0][base + 1] = brgb[nindex];
                bankData[0][base + 2] = brgb[nindex + 1];
                bankData[0][base + 3] = brgb[nindex + 2];
                nindex += 3;
            }
            nindex += npad;
        }

        return bufferedImage;
    }

    private static int bytesToInt(byte[] bytes, int index) {
        return (bytes[index + 3] & 0xff) << 24 |
                (bytes[index + 2] & 0xff) << 16 |
                (bytes[index + 1] & 0xff) << 8 |
                bytes[index + 0] & 0xff;
    }

    private static short bytesToShort(byte[] bytes, int index) {
        return (short) (((bytes[index + 1] & 0xff) << 8) |
                (bytes[index + 0] & 0xff));
    }

    private static void readBuffer(InputStream in, byte[] buffer) throws IOException {
        int bytesRead = 0;
        int bytesToRead = buffer.length;
        while (bytesToRead > 0) {
            int read = in.read(buffer, bytesRead, bytesToRead);
            bytesRead += read;
            bytesToRead -= read;
        }
    }
}
