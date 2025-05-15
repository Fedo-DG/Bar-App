import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class BarMangiaMarconi {
    public static void main(String[] args) {
        new RoleSelectionFrame();
    }
}

// Finestra di selezione del ruolo
class RoleSelectionFrame extends JFrame {
    public RoleSelectionFrame() {
        setTitle("Seleziona il ruolo");
        setSize(300, 150);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        String[] roles = {"Cliente", "Barista"};
        JComboBox<String> roleSelection = new JComboBox<>(roles);
        JButton proceedButton = new JButton("Procedi");

        add(new JLabel("Scegli il ruolo:"));
        add(roleSelection);
        add(proceedButton);

        proceedButton.addActionListener(e -> {
            String selectedRole = (String) roleSelection.getSelectedItem();
            if (selectedRole.equals("Cliente")) {
                new CustomerFrame();
            } else {
                new BaristaFrame();
            }
            dispose();
        });

        setVisible(true);
    }
}

// Finestra Cliente
class CustomerFrame extends JFrame {
    private Map<String, Integer> cart = new HashMap<>();
    private JLabel cartLabel;

    public CustomerFrame() {
        setTitle("Menu Cliente");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 2));

        String[] items = {"Caffè", "Tè", "Dolci", "Cocktail"};
        double[] prices = {2.70, 2.30, 4.50, 7.50};

        cartLabel = new JLabel("Carrello: ");
        add(cartLabel);

        for (int i = 0; i < items.length; i++) {
            String item = items[i];
            JButton addButton = new JButton("+ " + item);
            JButton removeButton = new JButton("- " + item);

            add(addButton);
            add(removeButton);

            addButton.addActionListener(e -> updateCart(item, 1));
            removeButton.addActionListener(e -> updateCart(item, -1));
        }

        JButton payButton = new JButton("Paga");
        payButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Pagamento effettuato!"));
        add(payButton);

        setVisible(true);
    }

    private void updateCart(String item, int quantity) {
        cart.put(item, cart.getOrDefault(item, 0) + quantity);
        if (cart.get(item) <= 0) cart.remove(item);
        cartLabel.setText("Carrello: " + cart.toString());
    }
}

// Finestra Barista
class BaristaFrame extends JFrame {
    private Map<String, Integer> inventory = new HashMap<>();
    private JTextArea inventoryArea;

    public BaristaFrame() {
        setTitle("Gestione Barista");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        inventoryArea = new JTextArea();
        updateInventoryDisplay();

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2));

        String[] items = {"Caffè", "Tè", "Dolci", "Cocktail"};
        for (String item : items) {
            JButton addButton = new JButton("+ " + item);
            addButton.addActionListener(e -> updateInventory(item, 1));
            panel.add(addButton);
        }

        JButton printButton = new JButton("Stampa inventario");
        printButton.addActionListener(e -> JOptionPane.showMessageDialog(this, inventoryArea.getText()));

        add(new JScrollPane(inventoryArea), BorderLayout.CENTER);
        add(panel, BorderLayout.NORTH);
        add(printButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void updateInventory(String item, int quantity) {
        inventory.put(item, inventory.getOrDefault(item, 0) + quantity);
        updateInventoryDisplay();
    }

    private void updateInventoryDisplay() {
        StringBuilder display = new StringBuilder("Inventario:\n");
        inventory.forEach((item, qty) -> display.append(item).append(": ").append(qty).append("\n"));
        inventoryArea.setText(display.toString());
    }
}
