package base.input;

import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.HashMap;

public class Input {

    private final Node node;

    private final HashMap<KeyCode, Boolean> mapKeysState;

    private final HashMap<KeyCode, Boolean> mapKeysStateLast;

    private final HashMap<MouseButton, Boolean> mapButtons;

    private final HashMap<MouseButton, Boolean> mapButtonLast;

    private double mouseX;

    private double mouseY;

    private int scroll;

    public Input(Node node) {
        this.node = node;

        mouseX = 0.0;
        mouseY = 0.0;
        scroll = 0;

        mapKeysState = new HashMap<>();
        for ( KeyCode keyCode : KeyCode.values() ) {
            mapKeysState.put(keyCode, Boolean.FALSE);
        }
        mapKeysStateLast = new HashMap<>();
        for ( KeyCode keyCode : KeyCode.values() ) {
            mapKeysStateLast.put(keyCode, Boolean.FALSE);
        }

        mapButtons = new HashMap<>();
        for ( MouseButton btn : MouseButton.values() ) {
            mapButtons.put(btn, Boolean.FALSE);
        }
        mapButtonLast = new HashMap<>();
        for ( MouseButton btn : MouseButton.values() ) {
            mapButtonLast.put(btn, Boolean.FALSE);
        }

        setNodeClickEvents();
        setNodeKeyEvents();
        node.setOnScroll(event -> scroll += (int) event.getDeltaY() / 40);
    }

    private void setNodeKeyEvents() {
        node.setOnKeyPressed(event -> {
            mapKeysState.replace(event.getCode(), Boolean.TRUE);
        });
        node.setOnKeyReleased(event -> {
            mapKeysState.replace(event.getCode(), Boolean.FALSE);
        });
    }

    private void setNodeClickEvents() {
        /*node.setOnMousePressed(event -> {
            mapButtons.replace(event.getButton(), Boolean.TRUE);
        });
        node.setOnMouseReleased(event -> {
            mapButtons.replace(event.getButton(), Boolean.FALSE);
        });
        node.setOnMouseMoved(event -> {
            mouseX = event.getX();
            mouseY = event.getY();
        });*/
        node.addEventHandler(MouseEvent.ANY, (event -> {
            if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
                mapButtons.replace(event.getButton(), Boolean.TRUE);
            }
            if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
                mapButtons.replace(event.getButton(), Boolean.FALSE);
            }
            mouseX = event.getX();
            mouseY = event.getY();
        }));
    }

    public void update() {
        mapKeysStateLast.clear();
        mapKeysStateLast.putAll(mapKeysState);

        mapButtonLast.clear();
        mapButtonLast.putAll(mapButtons);
    }

    public boolean isKey(KeyCode keyCode) {
        return mapKeysState.get(keyCode);
    }

    public boolean isKeyLast(KeyCode keyCode) {
        return mapKeysStateLast.get(keyCode);
    }

    // Now works well!!

    public boolean isKeyUp(KeyCode keyCode) {
        boolean isPressed = mapKeysState.get(keyCode);
        boolean wasPressed = mapKeysStateLast.get(keyCode);
        return !isPressed && wasPressed;
    }

    public boolean isKeyDown(KeyCode keyCode) {
        boolean isPressed = mapKeysState.get(keyCode);
        boolean wasPressed = mapKeysStateLast.get(keyCode);
        return isPressed && !wasPressed;
    }

    public boolean isKeyHeld(KeyCode keyCode) {
        boolean isPressed = mapKeysState.get(keyCode);
        boolean wasPressed = mapKeysStateLast.get(keyCode);
        return isPressed && wasPressed;
    }

    public boolean isButton(MouseButton button) {
        return mapButtons.get(button);
    }

    public boolean isButtonLast(MouseButton button) {
        return mapButtonLast.get(button);
    }

    public boolean isButtonUp(MouseButton button) {
        return !mapButtons.get(button) && mapButtonLast.get(button);
    }

    public boolean isButtonDown(MouseButton button) {
        return mapButtons.get(button) && !mapButtonLast.get(button);
    }

    public boolean isButtonHeld(MouseButton button) {
        return mapButtons.get(button) && mapButtonLast.get(button);
    }

    public Node getNode() {
        return node;
    }

    public double getMouseX() {
        return mouseX;
    }

    public double getMouseY() {
        return mouseY;
    }

    public int getScroll() {
        return scroll;
    }

}
