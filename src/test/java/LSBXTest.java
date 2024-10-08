import ar.edu.itba.cripto.steganography.LSB;
import ar.edu.itba.cripto.steganography.LSB1;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.*;

class LSBXTest {

    // Helper method to create a blank carrier image
    private BufferedImage createCarrier(int width, int height) {
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    @Test
    void hide() {

        BufferedImage carrier = createCarrier(10, 10);
        int secret = 129;
        byte[] data = ByteBuffer.allocate(4).putInt(secret).array();

        LSB lsb = new LSB1();

        lsb.hide(carrier, data);

        byte[] retreived = lsb.extract(carrier);


        assertEquals(data, retreived);






    }


}
