import ar.edu.itba.cripto.arguments.Parser;
import ar.edu.itba.cripto.arguments.Parser.Arguments;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ArgumentsTest {
    @Before
    public void setUp() {
    }

    @Test
    public void extractTest() {
        //–extract –p imagenmas1.bmp -out mensaje1 –steg LSBI –a 3des –m cbc -pass oculto
        String[] args = {"-extract", "-p", "imagenmas1.bmp", "-out", "mensaje1", "-steg", "LSBI", "-a", "3des", "-m", "cbc", "-pass", "oculto"};
        Optional<Arguments> arguments = new Parser().parse(args);
        assertTrue(arguments.isPresent());
    }

    @Test
    public void extractNoPasswordInvalidTest() {
        //–extract –p imagenmas1.bmp -out mensaje1 –steg LSBI –a 3des –m cbc -pass oculto
        String[] args = {"-extract", "-p", "imagenmas1.bmp", "-out", "mensaje1", "-steg", "LSBI", "-a", "3des", "-m", "cbc"};
        Optional<Arguments> arguments = new Parser().parse(args);
        assertFalse(arguments.isPresent());
    }

    @Test
    public void embedTest() {
        // -embed –in mensaje1.txt –p imagen1.bmp -out imagenmas1.bmp –steg LSBI –a 3des –m cbc -pass oculto
        String[] args = {"-embed", "-in", "mensaje1.txt", "-p", "imagen1.bmp", "-out", "imagenmas1.bmp", "-steg", "LSBI", "-a", "3des", "-m", "cbc", "-pass", "oculto"};
        Optional<Arguments> arguments = new Parser().parse(args);
        assertTrue(arguments.isPresent());
    }

    @Test
    public void embedNoPasswordInvalidTest() {
        // -embed –in mensaje1.txt –p imagen1.bmp -out imagenmas1.bmp –steg LSBI –a 3des –m cbc -pass oculto
        String[] args = {"-embed", "-in", "mensaje1.txt", "-p", "imagen1.bmp", "-out", "imagenmas1.bmp", "-steg", "LSBI", "-a", "3des", "-m", "cbc"};
        Optional<Arguments> arguments = new Parser().parse(args);
        assertFalse(arguments.isPresent());
    }
}
