package store;

import store.controller.Controller;
import store.controller.ControllerImpl;

public class Application {
    public static void main(String[] args) {
       Controller controller = ControllerImpl.getInstance();
       controller.run();
    }
}
