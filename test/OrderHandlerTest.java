import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpPrincipal;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.file.*;

public class OrderHandlerTest {

    @BeforeEach
    void setUp() throws Exception {

        Files.copy(
            Paths.get("seed/orders.json"),
            Paths.get("data/orders.json"),
            StandardCopyOption.REPLACE_EXISTING
        );

        Files.copy(
            Paths.get("seed/products.json"),
            Paths.get("data/products.json"),
            StandardCopyOption.REPLACE_EXISTING
        );

        Files.copy(
            Paths.get("seed/users.json"),
            Paths.get("data/users.json"),
            StandardCopyOption.REPLACE_EXISTING
        );
    }

    @AfterEach
    void tearDown() throws Exception {

        Files.copy(
            Paths.get("seed/orders.json"),
            Paths.get("data/orders.json"),
            StandardCopyOption.REPLACE_EXISTING
        );

        Files.copy(
            Paths.get("seed/products.json"),
            Paths.get("data/products.json"),
            StandardCopyOption.REPLACE_EXISTING
        );

        Files.copy(
            Paths.get("seed/users.json"),
            Paths.get("data/users.json"),
            StandardCopyOption.REPLACE_EXISTING
        );
    }

    static class MockHttpExchange extends HttpExchange {

        private final String method;
        private final ByteArrayInputStream requestBody;
        private final ByteArrayOutputStream responseBody =
                new ByteArrayOutputStream();

        private int responseCode;

        MockHttpExchange(String method, String body) {
            this.method = method;
            this.requestBody =
                    new ByteArrayInputStream(body.getBytes());
        }

        public String getResponseText() {
            return responseBody.toString();
        }

        @Override
        public Headers getRequestHeaders() {
            return new Headers();
        }

        @Override
        public Headers getResponseHeaders() {
            return new Headers();
        }

        @Override
        public URI getRequestURI() {
            return URI.create("/api/order");
        }

        @Override
        public String getRequestMethod() {
            return method;
        }

        @Override
        public HttpContext getHttpContext() {
            return null;
        }

        @Override
        public void close() {}

        @Override
        public InputStream getRequestBody() {
            return requestBody;
        }

        @Override
        public OutputStream getResponseBody() {
            return responseBody;
        }

        @Override
        public void sendResponseHeaders(
                int rCode,
                long responseLength) {

            this.responseCode = rCode;
        }

        @Override
        public InetSocketAddress getRemoteAddress() {
            return null;
        }

        @Override
        public InetSocketAddress getLocalAddress() {
            return null;
        }

        @Override
        public String getProtocol() {
            return "HTTP/1.1";
        }

        @Override
        public Object getAttribute(String name) {
            return null;
        }

        @Override
        public void setAttribute(
                String name,
                Object value) {
        }

        @Override
        public void setStreams(
                InputStream i,
                OutputStream o) {
        }

        @Override
        public HttpPrincipal getPrincipal() {
            return null;
        }

        @Override
        public int getResponseCode() {
            return responseCode;
        }
    }

    @Test
    void testPostTshirtDiscount20() throws Exception {

        String json =
            "{\"type\":\"tshirt\",\"qty\":150," +
            "\"isCustom\":false,\"complexity\":0," +
            "\"notes\":\"test\"}";

        MockHttpExchange exchange =
            new MockHttpExchange("POST", json);

        new OrderHandler().handle(exchange);

        assertEquals(200, exchange.getResponseCode());

        assertTrue(
            exchange.getResponseText()
                    .contains("success")
        );
    }

    @Test
    void testPostHoodieDiscount() throws Exception {

        String json =
            "{\"type\":\"hoodie\",\"qty\":25," +
            "\"isCustom\":false,\"complexity\":0," +
            "\"notes\":\"test\"}";

        MockHttpExchange exchange =
            new MockHttpExchange("POST", json);

        new OrderHandler().handle(exchange);

        assertEquals(200, exchange.getResponseCode());
    }

    @Test
    void testCustomComplexity1() throws Exception {

        String json =
            "{\"type\":\"tshirt\",\"qty\":20," +
            "\"isCustom\":true,\"complexity\":1," +
            "\"notes\":\"test\"}";

        MockHttpExchange exchange =
            new MockHttpExchange("POST", json);

        new OrderHandler().handle(exchange);

        assertEquals(200, exchange.getResponseCode());
    }

    @Test
    void testCustomComplexity2() throws Exception {

        String json =
            "{\"type\":\"tshirt\",\"qty\":20," +
            "\"isCustom\":true,\"complexity\":2," +
            "\"notes\":\"test\"}";

        MockHttpExchange exchange =
            new MockHttpExchange("POST", json);

        new OrderHandler().handle(exchange);

        assertEquals(200, exchange.getResponseCode());
    }

    @Test
    void testCustomComplexity3QtyLessThan10() throws Exception {

        String json =
            "{\"type\":\"tshirt\",\"qty\":5," +
            "\"isCustom\":true,\"complexity\":3," +
            "\"notes\":\"test\"}";

        MockHttpExchange exchange =
            new MockHttpExchange("POST", json);

        new OrderHandler().handle(exchange);

        assertEquals(200, exchange.getResponseCode());
    }

    @Test
    void testMethodNotAllowed() throws Exception {

        MockHttpExchange exchange =
            new MockHttpExchange("GET", "");

        new OrderHandler().handle(exchange);

        assertEquals(
            405,
            exchange.getResponseCode()
        );
    }
}