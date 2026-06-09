import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class OrderCalculatorTest {

@Test
void testTshirtDiscount20Percent() {
    assertEquals(
        6000000.0,
        OrderCalculator.calculatePrice(
            "tshirt",
            150,
            false,
            0
        )
    );
}

@Test
void testTshirtDiscount10Percent() {
    assertEquals(
        3375000.0,
        OrderCalculator.calculatePrice(
            "tshirt",
            75,
            false,
            0
        )
    );
}

@Test
void testHoodieDiscount15Percent() {
    assertEquals(
        3187500.0,
        OrderCalculator.calculatePrice(
            "hoodie",
            25,
            false,
            0
        )
    );
}

@Test
void testCustomComplexity1() {
    assertEquals(
        1200000.0,
        OrderCalculator.calculatePrice(
            "tshirt",
            20,
            true,
            1
        )
    );
}

@Test
void testCustomComplexity2() {
    assertEquals(
        1500000.0,
        OrderCalculator.calculatePrice(
            "tshirt",
            20,
            true,
            2
        )
    );
}

@Test
void testCustomComplexity3QtyLessThan10() {
    assertEquals(
        500000.0,
        OrderCalculator.calculatePrice(
            "tshirt",
            5,
            true,
            3
        )
    );
}

// TEST BARU 1
@Test
void testHoodieWithoutDiscount() {
    assertEquals(
        1500000.0,
        OrderCalculator.calculatePrice(
            "hoodie",
            10,
            false,
            0
        )
    );
}

// TEST BARU 2
@Test
void testCustomComplexity3QtyMoreThan10() {
    assertEquals(
        1700000.0,
        OrderCalculator.calculatePrice(
            "tshirt",
            20,
            true,
            3
        )
    );
}


}
