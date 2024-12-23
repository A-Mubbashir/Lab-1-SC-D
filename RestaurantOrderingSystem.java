package sc_d_lab7.restaurantorderingsystem;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class Order {
    private int id;
    private String status;

    public Order(int id) {
        this.id = id;
        this.status = "Received";
    }

    public int getId() {
        return id;
    }

    public synchronized String getStatus() {
        return status;
    }

    public synchronized void setStatus(String status) {
        this.status = status;
    }
}

class RestaurantSystem {
    private final BlockingQueue<Order> orderQueue = new LinkedBlockingQueue<>();
    private int orderId = 0;

    public void placeOrder() {
        Order order = new Order(++orderId);
        try {
            orderQueue.put(order);
            System.out.println("Order " + order.getId() + " placed.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void prepareOrder() {
        try {
            Order order = orderQueue.take();
            order.setStatus("Preparing");
            System.out.println("Order " + order.getId() + " is being prepared.");
            Thread.sleep(2000); // Simulate preparation time
            order.setStatus("Prepared");
            System.out.println("Order " + order.getId() + " prepared.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void deliverOrder() {
        try {
            Thread.sleep(3000); // Simulate delivery time
            System.out.println("Order delivered.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class RestaurantOrderingSystem {
    public static void main(String[] args) {
        RestaurantSystem system = new RestaurantSystem();

        // Place orders in a separate thread
        Thread customerThread = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                system.placeOrder();
                try {
                    Thread.sleep(500); // Simulate time between orders
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // Prepare orders in a separate thread
        Thread kitchenThread = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                system.prepareOrder();
            }
        });

        customerThread.start();
        kitchenThread.start();

        try {
            customerThread.join();
            kitchenThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("All orders processed.");
    }
}

