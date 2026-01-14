import com.sun.net.httpserver.HttpServer;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.HashMap;

public class CalculatorServer {

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(9090), 0);

        server.createContext("/calc", exchange -> {

            // 🔥 CORS FIX
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

            URI uri = exchange.getRequestURI();
            HashMap<String, String> params = queryToMap(uri.getQuery());

            String op = params.get("op");
            BigDecimal n1 = new BigDecimal(params.getOrDefault("num1", "0"));
            BigDecimal n2 = new BigDecimal(params.getOrDefault("num2", "0"));

            String result;

            try {
                switch (op) {
                    case "add" -> result = n1.add(n2).toString();
                    case "sub" -> result = n1.subtract(n2).toString();
                    case "mul" -> result = n1.multiply(n2).toString();
                    case "div" -> {
                        if (n2.compareTo(BigDecimal.ZERO) == 0)
                            result = "Cannot divide by zero";
                        else
                            result = n1.divide(n2).toString();
                    }
                    case "sqrt" -> result = String.valueOf(Math.sqrt(n1.doubleValue()));
                    case "pow" -> result = String.valueOf(Math.pow(n1.doubleValue(), n2.doubleValue()));
                    default -> result = "Invalid operation";
                }
            } catch (Exception e) {
                result = "Error in calculation";
            }

            exchange.sendResponseHeaders(200, result.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(result.getBytes());
            os.close();
        });

        server.start();
        System.out.println("Server started at http://localhost:9090");
    }

    static HashMap<String, String> queryToMap(String query) {
        HashMap<String, String> map = new HashMap<>();
        if (query == null)
            return map;

        for (String param : query.split("&")) {
            String[] pair = param.split("=");
            map.put(pair[0], pair.length > 1 ? pair[1] : "");
        }
        return map;
    }
}
