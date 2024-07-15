import utils.Server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            new Server("localhost", 9879).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}