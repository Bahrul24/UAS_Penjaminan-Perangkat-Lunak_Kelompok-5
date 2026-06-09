import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;
import java.util.Locale;

public class OrderHandler implements HttpHandler{

    @Override
    public void handle(HttpExchange exchange) throws IOException{
        // Node 1: Decision - HTTP Method POST?
        if ("POST".equals(exchange.getRequestMethod())) {
            String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

            String type = extractValue(requestBody, "type");
            int qty = Integer.parseInt(extractValue(requestBody, "qty"));
            boolean isCustom = Boolean.parseBoolean(extractValue(requestBody, "isCustom"));
            int complexity = Integer.parseInt(extractValue(requestBody, "complexity"));
            String notes = extractValue(requestBody, "notes");

            double price = 0;
            // Node 2: Decision - type == tshirt?
            if (type.equals("tshirt")) {            
                price = 50000;                      
                if (qty > 100) {                    // Node 3: Decision - qty > 100?
                    price = price * 0.8;            // Node 4: Action - discount 20%
                } else if (qty > 50) {              // Node 5: Decision - qty > 50?
                    price = price * 0.9;            // Node 6: Action - discount 10%
                }
            } else if (type.equals("hoodie")) {     // Node 7: Decision - type == hoodie?
                price = 150000;
                if (qty > 20) {                     // Node 8: Decision - qty > 20?
                    price = price * 0.85;           // Node 9: Action - discount 15%
                }
            }

            if (isCustom) {                          // Node 10: Decision - isCustom?
                if (complexity == 1) {              // Node 11: Decision - complexity == 1?
                    price += 10000;                // Node 12: Action
                } else if (complexity == 2) {        // Node 13: Decision - complexity == 2?
                    price += 25000;                 // Node 14: Action
                } else {                           
                    if (qty < 10) {                 // Node 15: Decision - qty < 10?
                        price += 50000;              // Node 16: Action
                    } else {
                        price += 35000;             // Node 17: Action
                    }
                }
            }

            double total = price * qty;             // Node 18: Final computation
            String orderId = UUID.randomUUID().toString();

             // Node 19: Build JSON order record
            String newOrderRecord = String.format(Locale.US, "{\"id\":\"%s\",\"type\":\"%s\",\"qty\":%d,\"total\":%.2f,\"notes\":\"%s\"}",
            orderId, type, qty, total, notes);

            try {
                // Node 20: Read existing database file
                String existingContent = new String(Files.readAllBytes(Paths.get("data/orders.json")), StandardCharsets.UTF_8).trim();
                
                // Ambil isi array yang sudah ada (hapus [ dan ])
                String inner = existingContent.substring(1, existingContent.length() - 1).trim();
                inner = inner.replaceAll("^\\[,?\\s*", "").replaceAll(",?\\s*\\]$", "").trim();
                
                String newContent;
                if (inner.isEmpty()) {
                    newContent = "[\n" + newOrderRecord + "\n]";
                } else {
                    newContent = "[\n" + inner + ",\n" + newOrderRecord + "\n]";
                }
                
                // Node 21: Write updated JSON file
                Files.write(Paths.get("data/orders.json"), newContent.getBytes(StandardCharsets.UTF_8), StandardOpenOption.TRUNCATE_EXISTING);
            } catch (Exception e) {
                // Node 22: Exception handling
                e.printStackTrace();
            }


            // Node 23: Build response
            String responseBody = "{\"status\":\"success\", \"orderId\":\"" + orderId + "\", \"totalPrice\":" + total + "}";
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            // Node 24: Send HTTP 200
            exchange.sendResponseHeaders(200, responseBody.length());

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBody.getBytes());
            }
        } else {
            // Node 25: HTTP 405 Method Not Allowed
            exchange.sendResponseHeaders(405, -1);
        }
        // Node 26: End handle()
    }

    private String extractValue(String json, String key){
        String target = "\"" + key + "\":";
        int start = json.indexOf(target);
        if (start == -1) return "0";
        start += target.length();

        int end = json.indexOf(",", start);
        if (end == -1) {
            end = json.indexOf("}", start);
        }

        return json.substring(start, end).replaceAll("\"", "").trim();
    }
}