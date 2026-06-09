public class TestOrderCalculator {

    public static void main(String[] args) {

        double result =
            OrderCalculator.calculatePrice(
                "tshirt",
                150,
                false,
                0
            );

        if (result == 6000000.0) {
            System.out.println("PASS");
        } else {
            System.out.println("FAIL");
        }
    }
}