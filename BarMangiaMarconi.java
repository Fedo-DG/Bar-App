import javax.swing.*;
import java.awt.*;

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

        setVisible(true);
    }
}

// Finestra Cliente
class CustomerFrame extends JFrame {
    public CustomerFrame() {
        setTitle("Menu Cliente");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 2));

        JLabel cartLabel = new JLabel("Carrello: ");
        add(cartLabel);

        String[] items = {"Caffè", "Tè", "Dolci", "Cocktail"};

        for (String item : items) {
            JButton addButton = new JButton("+ " + item);
            JButton removeButton = new JButton("- " + item);

            add(addButton);
            add(removeButton);
        }

        JButton payButton = new JButton("Paga");
        add(payButton);

        setVisible(true);
    }
}

// Finestra Barista
class BaristaFrame extends JFrame {
    public BaristaFrame() {
        setTitle("Gestione Barista");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JTextArea inventoryArea = new JTextArea("Inventario:\n");
        add(new JScrollPane(inventoryArea), BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2));

        String[] items = {"Caffè", "Tè", "Dolci", "Cocktail"};
        for (String item : items) {
            JButton addButton = new JButton("+ " + item);
            panel.add(addButton);
        }

        JButton printButton = new JButton("Stampa inventario");
        add(printButton, BorderLayout.SOUTH);
        add(panel, BorderLayout.NORTH);

        setVisible(true);
    }
}
