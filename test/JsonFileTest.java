import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.nio.file.*;

public class JsonFileTest {

    @Test
    void UT_07_ReadJson() throws Exception {

        String content = Files.readString(Paths.get("data/orders.json"));

        assertNotNull(content);
        assertFalse(content.isEmpty());
    }

    @Test
    void UT_08_WriteJson() throws Exception {

        String file = "data/test_orders.json";

        Files.writeString(
            Paths.get(file),
            "[{\"test\":true}]"
        );

        String result = Files.readString(Paths.get(file));

        assertEquals("[{\"test\":true}]", result);

        Files.delete(Paths.get(file));
    }
}