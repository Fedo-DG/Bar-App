import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class BarManagementSystem {
    private static final String DATA_FILE = "inventario_bar.txt";
    
    public static void main(String[] args) {
        // Inizializza il sistema di gestione dati
        DataManagementSystem.setFilePath(DATA_FILE);
        
        // Crea alcuni prodotti di default se il file è vuoto
        initializeDefaultProducts();
        
        // Avvia l'interfaccia grafica
        SwingUtilities.invokeLater(() -> createMainInterface());
    }
    
    private static void initializeDefaultProducts() {
        String fileContent = DataManagementSystem.fileToString();
        if (fileContent.trim().isEmpty()) {
            // Prodotti colazione
            DataManagementSystem.makeItem("Cornetto semplice", 1.00, 50);
            DataManagementSystem.makeItem("Cornetto integrale", 1.20, 30);
            DataManagementSystem.makeItem("Muffin al cioccolato", 1.30, 25);
            DataManagementSystem.makeItem("Pane e marmellata", 1.00, 40);
            DataManagementSystem.makeItem("Yogurt con cereali", 1.50, 20);
            
            // Bevande
            DataManagementSystem.makeItem("Acqua naturale", 0.80, 100);
            DataManagementSystem.makeItem("Succo di frutta", 1.20, 60);
            DataManagementSystem.makeItem("Tè freddo", 1.30, 40);
            DataManagementSystem.makeItem("Latte e cacao", 1.20, 30);
            DataManagementSystem.makeItem("Caffè d'orzo", 0.90, 50);
            
            // Salati
            DataManagementSystem.makeItem("Panino prosciutto", 2.00, 30);
            DataManagementSystem.makeItem("Panino formaggio", 2.00, 25);
            DataManagementSystem.makeItem("Tramezzino tonno", 2.20, 20);
            DataManagementSystem.makeItem("Focaccia semplice", 1.50, 35);
            DataManagementSystem.makeItem("Toast", 2.50, 25);
            
            // Snack
            DataManagementSystem.makeItem("Frutta fresca", 1.00, 50);
            DataManagementSystem.makeItem("Macedonia", 1.50, 15);
            DataManagementSystem.makeItem("Barretta cereali", 1.00, 40);
            DataManagementSystem.makeItem("Crackers", 0.80, 60);
            DataManagementSystem.makeItem("Popcorn", 1.00, 30);
        }
    }
    
    private static void createMainInterface() {
        JFrame frame = new JFrame("Sistema Gestione Bar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 350);
        frame.setLocationRelativeTo(null);

        Color sfondo = new Color(255, 248, 220);       // crema
        Color bottoneColore = new Color(139, 69, 19);  // marrone scuro
        Color testoColore = Color.WHITE;

        JPanel panel = new JPanel();
        panel.setBackground(sfondo);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel titolo = new JLabel("Sistema Gestione Bar");
        titolo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titolo.setFont(new Font("SansSerif", Font.BOLD, 24));
        titolo.setForeground(new Color(101, 67, 33));
        panel.add(titolo);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        JButton clientiButton = new JButton("Area Clienti");
        stileBottone(clientiButton, bottoneColore, testoColore);
        clientiButton.addActionListener(e -> apriAreaClienti(sfondo, bottoneColore, testoColore));
        panel.add(clientiButton);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton personaleButton = new JButton("Area Personale");
        stileBottone(personaleButton, bottoneColore, testoColore);
        personaleButton.addActionListener(e -> apriAreaPersonale(sfondo, bottoneColore, testoColore));
        panel.add(personaleButton);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton inventarioButton = new JButton("Visualizza Inventario");
        stileBottone(inventarioButton, bottoneColore, testoColore);
        inventarioButton.addActionListener(e -> mostraInventarioCompleto());
        panel.add(inventarioButton);

        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }

    private static void stileBottone(JButton button, Color bg, Color fg) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(220, 40));
        button.setMaximumSize(new Dimension(220, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private static void apriAreaClienti(Color sfondo, Color bottoneColore, Color testoColore) {
        JFrame clientiFrame = new JFrame("Area Clienti");
        clientiFrame.setSize(500, 400);
        clientiFrame.setLocationRelativeTo(null);
        clientiFrame.setLayout(new BorderLayout());

        JLabel label = new JLabel("Benvenuto nell'Area Clienti!", SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 18));
        label.setForeground(new Color(101, 67, 33));
        label.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        clientiFrame.add(label, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        panel.setBackground(sfondo);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel menuLabel = new JLabel("Seleziona una categoria:");
        menuLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        menuLabel.setForeground(new Color(101, 67, 33));
        panel.add(menuLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        String[] categorie = {"Seleziona categoria", "🍩 Colazione", "🥤 Bevande", "🥪 Salati", "🍌 Snack"};
        JComboBox<String> menuDropdown = new JComboBox<>(categorie);
        menuDropdown.setMaximumSize(new Dimension(250, 30));
        menuDropdown.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuDropdown.setBackground(Color.WHITE);
        menuDropdown.setFont(new Font("SansSerif", Font.PLAIN, 14));
        menuDropdown.setForeground(new Color(101, 67, 33));
        panel.add(menuDropdown);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton ordinaButton = new JButton("Effettua Ordine");
        stileBottone(ordinaButton, bottoneColore, testoColore);
        ordinaButton.addActionListener(e -> apriInterfacciaOrdine(clientiFrame, sfondo, bottoneColore, testoColore));
        panel.add(ordinaButton);

        menuDropdown.addActionListener(e -> {
            String selected = (String) menuDropdown.getSelectedItem();
            if (!selected.equals("Seleziona categoria")) {
                mostraCategoria(selected, clientiFrame);
            }
        });

        clientiFrame.add(panel, BorderLayout.CENTER);
        clientiFrame.setVisible(true);
    }

    private static void apriAreaPersonale(Color sfondo, Color bottoneColore, Color testoColore) {
        JFrame personaleFrame = new JFrame("Area Personale");
        personaleFrame.setSize(450, 400);
        personaleFrame.setLocationRelativeTo(null);
        personaleFrame.setLayout(new BorderLayout());

        JLabel label = new JLabel("Area Gestione Personale", SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 18));
        label.setForeground(new Color(101, 67, 33));
        label.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        personaleFrame.add(label, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        panel.setBackground(sfondo);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton mostraInventario = new JButton("Mostra Inventario");
        JButton aggiungiProdotto = new JButton("Aggiungi Prodotto");
        JButton rifornisciProdotto = new JButton("Rifornisci Prodotto");
        JButton rimuoviProdotto = new JButton("Rimuovi dal Magazzino");

        stileBottone(mostraInventario, bottoneColore, testoColore);
        stileBottone(aggiungiProdotto, bottoneColore, testoColore);
        stileBottone(rifornisciProdotto, bottoneColore, testoColore);
        stileBottone(rimuoviProdotto, bottoneColore, testoColore);

        panel.add(mostraInventario);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(aggiungiProdotto);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(rifornisciProdotto);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(rimuoviProdotto);

        mostraInventario.addActionListener(e -> mostraInventarioCompleto());

        aggiungiProdotto.addActionListener(e -> aggiungiNuovoProdotto(personaleFrame));

        rifornisciProdotto.addActionListener(e -> rifornisciProdotto(personaleFrame));

        rimuoviProdotto.addActionListener(e -> rimuoviDalMagazzino(personaleFrame));

        personaleFrame.add(panel, BorderLayout.CENTER);
        personaleFrame.setVisible(true);
    }

    private static void mostraCategoria(String categoria, JFrame parent) {
        String[] prodottiCategoria = getProdottiPerCategoria(categoria);
        
        StringBuilder sb = new StringBuilder("Prodotti disponibili in " + categoria + ":\n\n");
        
        for (String prodotto : prodottiCategoria) {
            String itemInfo = DataManagementSystem.findItem(prodotto);
            if (!itemInfo.equals("Item not found or error occurred")) {
                String[] lines = itemInfo.split("\n");
                String prezzo = "";
                String quantita = "";
                
                for (String line : lines) {
                    if (line.trim().startsWith("Price=")) {
                        prezzo = line.trim().substring(6, line.trim().length() - 1);
                    } else if (line.trim().startsWith("Quantity=")) {
                        quantita = line.trim().substring(9, line.trim().length() - 1);
                    }
                }
                
                sb.append("• ").append(prodotto).append(" - €").append(prezzo)
                  .append(" (Disponibili: ").append(quantita).append(")\n");
            }
        }
        
        JOptionPane.showMessageDialog(parent, sb.toString());
    }
    
    private static String[] getProdottiPerCategoria(String categoria) {
        switch (categoria) {
            case "🍩 Colazione":
                return new String[]{"Cornetto semplice", "Cornetto integrale", "Muffin al cioccolato", 
                                  "Pane e marmellata", "Yogurt con cereali"};
            case "🥤 Bevande":
                return new String[]{"Acqua naturale", "Succo di frutta", "Tè freddo", 
                                  "Latte e cacao", "Caffè d'orzo"};
            case "🥪 Salati":
                return new String[]{"Panino prosciutto", "Panino formaggio", "Tramezzino tonno", 
                                  "Focaccia semplice", "Toast"};
            case "🍌 Snack":
                return new String[]{"Frutta fresca", "Macedonia", "Barretta cereali", 
                                  "Crackers", "Popcorn"};
            default:
                return new String[]{};
        }
    }

    private static void apriInterfacciaOrdine(JFrame parent, Color sfondo, Color bottoneColore, Color testoColore) {
        JFrame ordineFrame = new JFrame("Effettua Ordine");
        ordineFrame.setSize(400, 300);
        ordineFrame.setLocationRelativeTo(parent);
        
        JPanel panel = new JPanel();
        panel.setBackground(sfondo);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel istruzioni = new JLabel("Inserisci il nome del prodotto e la quantità:");
        istruzioni.setAlignmentX(Component.CENTER_ALIGNMENT);
        istruzioni.setFont(new Font("SansSerif", Font.PLAIN, 14));
        panel.add(istruzioni);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JTextField prodottoField = new JTextField();
        prodottoField.setMaximumSize(new Dimension(250, 25));
        prodottoField.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(new JLabel("Nome prodotto:"));
        panel.add(prodottoField);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JTextField quantitaField = new JTextField();
        quantitaField.setMaximumSize(new Dimension(250, 25));
        quantitaField.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(new JLabel("Quantità:"));
        panel.add(quantitaField);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JButton confermaButton = new JButton("Conferma Ordine");
        stileBottone(confermaButton, bottoneColore, testoColore);
        
        confermaButton.addActionListener(e -> {
            String prodotto = prodottoField.getText().trim();
            String quantitaStr = quantitaField.getText().trim();
            
            if (prodotto.isEmpty() || quantitaStr.isEmpty()) {
                JOptionPane.showMessageDialog(ordineFrame, "Compila tutti i campi!");
                return;
            }
            
            try {
                int quantita = Integer.parseInt(quantitaStr);
                if (quantita <= 0) {
                    JOptionPane.showMessageDialog(ordineFrame, "La quantità deve essere positiva!");
                    return;
                }
                
                // Verifica se il prodotto esiste e ha quantità sufficiente
                String itemInfo = DataManagementSystem.findItem(prodotto);
                if (itemInfo.equals("Item not found or error occurred")) {
                    JOptionPane.showMessageDialog(ordineFrame, "Prodotto non trovato!");
                    return;
                }
                
                // Rimuovi la quantità ordinata dall'inventario
                DataManagementSystem.removeItem(prodotto, quantita);
                JOptionPane.showMessageDialog(ordineFrame, 
                    "Ordine confermato!\n" + quantita + "x " + prodotto);
                ordineFrame.dispose();
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(ordineFrame, "Inserisci un numero valido per la quantità!");
            }
        });
        
        panel.add(confermaButton);
        ordineFrame.add(panel);
        ordineFrame.setVisible(true);
    }

    private static void mostraInventarioCompleto() {
        String inventario = DataManagementSystem.fileToString();
        if (inventario.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "L'inventario è vuoto.");
        } else {
            JTextArea textArea = new JTextArea(inventario);
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 400));
            JOptionPane.showMessageDialog(null, scrollPane, "Inventario Completo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private static void aggiungiNuovoProdotto(JFrame parent) {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        
        JTextField nomeField = new JTextField();
        JTextField prezzoField = new JTextField();
        JTextField quantitaField = new JTextField();
        
        panel.add(new JLabel("Nome prodotto:"));
        panel.add(nomeField);
        panel.add(new JLabel("Prezzo (€):"));
        panel.add(prezzoField);
        panel.add(new JLabel("Quantità:"));
        panel.add(quantitaField);
        
        int result = JOptionPane.showConfirmDialog(parent, panel, "Aggiungi Nuovo Prodotto", 
                                                  JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                String nome = nomeField.getText().trim();
                double prezzo = Double.parseDouble(prezzoField.getText().trim());
                int quantita = Integer.parseInt(quantitaField.getText().trim());
                
                if (nome.isEmpty() || prezzo < 0 || quantita < 0) {
                    JOptionPane.showMessageDialog(parent, "Inserisci dati validi!");
                    return;
                }
                
                DataManagementSystem.makeItem(nome, prezzo, quantita);
                JOptionPane.showMessageDialog(parent, "Prodotto aggiunto con successo!");
                
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(parent, "Inserisci valori numerici validi!");
            }
        }
    }

    private static void rifornisciProdotto(JFrame parent) {
        String prodotto = JOptionPane.showInputDialog(parent, "Nome del prodotto da rifornire:");
        if (prodotto != null && !prodotto.trim().isEmpty()) {
            String quantitaStr = JOptionPane.showInputDialog(parent, "Quantità da aggiungere:");
            if (quantitaStr != null && !quantitaStr.trim().isEmpty()) {
                try {
                    int quantita = Integer.parseInt(quantitaStr.trim());
                    if (quantita > 0) {
                        DataManagementSystem.addItem(prodotto.trim(), quantita);
                        JOptionPane.showMessageDialog(parent, "Rifornimento completato!");
                    } else {
                        JOptionPane.showMessageDialog(parent, "La quantità deve essere positiva!");
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(parent, "Inserisci un numero valido!");
                }
            }
        }
    }

    private static void rimuoviDalMagazzino(JFrame parent) {
        String prodotto = JOptionPane.showInputDialog(parent, "Nome del prodotto da rimuovere:");
        if (prodotto != null && !prodotto.trim().isEmpty()) {
            String quantitaStr = JOptionPane.showInputDialog(parent, "Quantità da rimuovere:");
            if (quantitaStr != null && !quantitaStr.trim().isEmpty()) {
                try {
                    int quantita = Integer.parseInt(quantitaStr.trim());
                    if (quantita > 0) {
                        DataManagementSystem.removeItem(prodotto.trim(), quantita);
                        JOptionPane.showMessageDialog(parent, "Rimozione completata!");
                    } else {
                        JOptionPane.showMessageDialog(parent, "La quantità deve essere positiva!");
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(parent, "Inserisci un numero valido!");
                }
            }
        }
    }
}

// Classe DataManagementSystem integrata
class DataManagementSystem {
    private static String filePath = "";

    public static String findItem(String toFind) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("Item=") && line.substring(5, line.length() - 1).equalsIgnoreCase(toFind)) {
                    List<String> allLines = readAllLines();
                    for (int i = 0; i < allLines.size(); i++) {
                        String curr = allLines.get(i).trim();
                        if (curr.equals("Item=" + toFind + ";")) {
                            int start = i;
                            while (start >= 0 && !allLines.get(start).trim().equals("{")) {
                                start--;
                            }
                            int end = i;
                            while (end < allLines.size() && !allLines.get(end).trim().equals("}")) {
                                end++;
                            }
                            if (start >= 0 && end < allLines.size()) {
                                StringJoiner result = new StringJoiner("\n");
                                for (int j = start; j <= end; j++) {
                                    result.add(allLines.get(j));
                                }
                                return result.toString();
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Item not found or error occurred";
    }

    public static void addItem(String itemName, int quantityToAdd) {
        try {
            List<String> lines = readAllLines();
            boolean found = false;

            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).trim().equals("{") && i + 4 < lines.size()) {
                    String nameLine = lines.get(i + 1).trim();
                    String name = extractValue(nameLine, "Item=");
                    if (name.equalsIgnoreCase(itemName)) {
                        String qtyLine = lines.get(i + 3).trim();
                        int currentQty = Integer.parseInt(extractValue(qtyLine, "Quantity="));
                        int newQty = currentQty + quantityToAdd;
                        lines.set(i + 3, "    Quantity=" + newQty + ";");
                        found = true;
                        break;
                    }
                }
            }

            if (!found) {
                System.out.println("Item not found. Use makeItem() to add new items.");
                return;
            }

            writeAllLines(lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void makeItem(String itemName, double price, int quantity) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write("{");
            writer.newLine();
            writer.write("    Item=" + itemName + ";");
            writer.newLine();
            writer.write("    Price=" + price + ";");
            writer.newLine();
            writer.write("    Quantity=" + quantity + ";");
            writer.newLine();
            writer.write("}");
            writer.newLine();
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeItem(String itemName, int quantityToRemove) {
        try {
            List<String> lines = readAllLines();
            boolean found = false;

            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).trim().equals("{")) {
                    int itemLineIndex = -1;
                    int quantityLineIndex = -1;

                    for (int j = i + 1; j < lines.size() && !lines.get(j).trim().equals("}"); j++) {
                        String currentLine = lines.get(j).trim();

                        if (currentLine.startsWith("Item=")) {
                            String itemValue = extractValue(currentLine, "Item=");
                            if (itemValue.equalsIgnoreCase(itemName)) {
                                itemLineIndex = j;
                            }
                        } else if (currentLine.startsWith("Quantity=")) {
                            quantityLineIndex = j;
                        }
                    }

                    if (itemLineIndex != -1 && quantityLineIndex != -1) {
                        int currentQty = Integer.parseInt(extractValue(lines.get(quantityLineIndex).trim(), "Quantity="));
                        int newQty = currentQty - quantityToRemove;

                        if (newQty < 0) {
                            System.out.println("Quantity of item '" + itemName + "' would go negative. Operation aborted.");
                            return;
                        }

                        lines.set(quantityLineIndex, "    Quantity=" + newQty + ";");
                        found = true;
                        break;
                    }
                }
            }

            if (!found) {
                System.out.println("Item '" + itemName + "' not found.");
                return;
            }

            writeAllLines(lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeMultipleItems(Map<String, Integer> itemsToRemove) {
        try {
            List<String> lines = readAllLines();
            Map<String, Integer> foundItems = new HashMap<>();

            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).trim().equals("{")) {
                    int quantityLineIndex = -1;
                    String currentItemName = null;

                    for (int j = i + 1; j < lines.size() && !lines.get(j).trim().equals("}"); j++) {
                        String currentLine = lines.get(j).trim();

                        if (currentLine.startsWith("Item=")) {
                            currentItemName = extractValue(currentLine, "Item=");
                        } else if (currentLine.startsWith("Quantity=")) {
                            quantityLineIndex = j;
                        }
                    }

                    if (currentItemName != null && itemsToRemove.containsKey(currentItemName) && quantityLineIndex != -1) {
                        int currentQty = Integer.parseInt(extractValue(lines.get(quantityLineIndex).trim(), "Quantity="));
                        int qtyToRemove = itemsToRemove.get(currentItemName);
                        int newQty = currentQty - qtyToRemove;

                        if (newQty < 0) {
                            System.out.println("Quantity of item '" + currentItemName + "' would go negative. Operation aborted.");
                            return;
                        }

                        lines.set(quantityLineIndex, "    Quantity=" + newQty + ";");
                        foundItems.put(currentItemName, newQty);
                    }
                }
            }

            for (String item : itemsToRemove.keySet()) {
                if (!foundItems.containsKey(item)) {
                    System.out.println("Item '" + item + "' not found.");
                }
            }

            writeAllLines(lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> readAllLines() throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    private static void writeAllLines(List<String> lines) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    private static String extractValue(String line, String key) {
        return line.substring(key.length(), line.length() - 1).trim();
    }

    public static String fileToString() {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error reading file.";
        }
        return sb.toString();
    }
    
    public static void setFilePath(String fp) {
        filePath = fp;
    }
}
