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

