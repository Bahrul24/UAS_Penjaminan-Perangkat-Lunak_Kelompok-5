public class OrderCalculator {

    public static double calculatePrice(String type, int qty, boolean isCustom, int complexity) {

        double price = 0;

        if (type.equals("tshirt")) {
            price = 50000;

            if (qty > 100) {
                price = price * 0.8;
            } else if (qty > 50) {
                price = price * 0.9;
            }

        } else if (type.equals("hoodie")) {

            price = 150000;

            if (qty > 20) {
                price = price * 0.85;
            }
        }

        if (isCustom) {

            if (complexity == 1) {
                price += 10000;

            } else if (complexity == 2) {
                price += 25000;

            } else {

                if (qty < 10) {
                    price += 50000;
                } else {
                    price += 35000;
                }
            }
        }

        return price * qty;
    }
}