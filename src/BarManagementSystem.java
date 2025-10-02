import java.awt.*;
import java.util.*;
import javax.swing.*;

class CartItem {
    String nome;
    double prezzo;
    int quantita;
    
    public CartItem(String nome, double prezzo, int quantita) {
        this.nome = nome;
        this.prezzo = prezzo;
        this.quantita = quantita;
    }
    
    public double getTotale() {
        return prezzo * quantita;
    }
}

// Helper class for product information
class ProductInfo {
    String nome;
    double prezzo;
    int quantita;
    
    public ProductInfo(String nome, double prezzo, int quantita) {
        this.nome = nome;
        this.prezzo = prezzo;
        this.quantita = quantita;
    }
}

public class BarManagementSystem {
    private static final String DATA_FILE = "inventario_bar.txt";
    private static Map<String, CartItem> carrello = new HashMap<>();
    
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
        clientiFrame.setSize(800, 600);
        clientiFrame.setLocationRelativeTo(null);
        clientiFrame.setLayout(new BorderLayout());

        // Header
        JLabel label = new JLabel("Benvenuto nell'Area Clienti!", SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 18));
        label.setForeground(new Color(101, 67, 33));
        label.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        clientiFrame.add(label, BorderLayout.NORTH);

        // Panel principale con layout diviso
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(sfondo);

        // Panel sinistro - Prodotti
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(sfondo);
        leftPanel.setBorder(BorderFactory.createTitledBorder("Prodotti Disponibili"));
        leftPanel.setPreferredSize(new Dimension(500, 400));

        // Dropdown categorie
        JPanel categoryPanel = new JPanel();
        categoryPanel.setBackground(sfondo);
        String[] categorie = {"Tutti i prodotti", "🍩 Colazione", "🥤 Bevande", "🥪 Salati", "🍌 Snack"};
        JComboBox<String> categoryCombo = new JComboBox<>(categorie);
        categoryCombo.setBackground(Color.WHITE);
        categoryPanel.add(new JLabel("Categoria: "));
        categoryPanel.add(categoryCombo);
        leftPanel.add(categoryPanel, BorderLayout.NORTH);

        // Lista prodotti
        DefaultListModel<String> productModel = new DefaultListModel<>();
        JList<String> productList = new JList<>(productModel);
        productList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productList.setFont(new Font("SansSerif", Font.PLAIN, 12));
        JScrollPane productScroll = new JScrollPane(productList);
        leftPanel.add(productScroll, BorderLayout.CENTER);

        // Panel per aggiungere al carrello
        JPanel addPanel = new JPanel();
        addPanel.setBackground(sfondo);
        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        JButton addToCartButton = new JButton("Aggiungi al Carrello");
        stileBottone(addToCartButton, bottoneColore, testoColore);
        addPanel.add(new JLabel("Quantità:"));
        addPanel.add(quantitySpinner);
        addPanel.add(addToCartButton);
        leftPanel.add(addPanel, BorderLayout.SOUTH);

        // Panel destro - Carrello
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(sfondo);
        rightPanel.setBorder(BorderFactory.createTitledBorder("Carrello"));
        rightPanel.setPreferredSize(new Dimension(280, 400));

        // Lista carrello
        DefaultListModel<String> cartModel = new DefaultListModel<>();
        JList<String> cartList = new JList<>(cartModel);
        cartList.setFont(new Font("SansSerif", Font.PLAIN, 11));
        JScrollPane cartScroll = new JScrollPane(cartList);
        rightPanel.add(cartScroll, BorderLayout.CENTER);

        // Panel totale e azioni carrello
        JPanel cartActionsPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        cartActionsPanel.setBackground(sfondo);
        
        JLabel totalLabel = new JLabel("Totale: €0.00");
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        totalLabel.setForeground(new Color(101, 67, 33));
        
        JButton removeFromCartButton = new JButton("Rimuovi Selezionato");
        JButton clearCartButton = new JButton("Svuota Carrello");
        JButton orderButton = new JButton("Invia Ordine");
        
        stileBottone(removeFromCartButton, new Color(178, 34, 34), Color.WHITE);
        stileBottone(clearCartButton, new Color(178, 34, 34), Color.WHITE);
        stileBottone(orderButton, new Color(34, 139, 34), Color.WHITE);
        
        cartActionsPanel.add(totalLabel);
        cartActionsPanel.add(removeFromCartButton);
        cartActionsPanel.add(clearCartButton);
        cartActionsPanel.add(orderButton);
        rightPanel.add(cartActionsPanel, BorderLayout.SOUTH);

        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.EAST);
        clientiFrame.add(mainPanel, BorderLayout.CENTER);

        // Carica inizialmente tutti i prodotti
        caricaProdotti(productModel, "Tutti i prodotti");
        aggiornaCarrello(cartModel, totalLabel);

        // Event listeners
        categoryCombo.addActionListener(e -> {
            String selected = (String) categoryCombo.getSelectedItem();
            caricaProdotti(productModel, selected);
        });

        addToCartButton.addActionListener(e -> {
            String selected = productList.getSelectedValue();
            if (selected != null) {
                String nomeProdotto = estraiNomeProdotto(selected);
                int quantita = (Integer) quantitySpinner.getValue();
                aggiungiAlCarrello(nomeProdotto, quantita);
                aggiornaCarrello(cartModel, totalLabel);
            } else {
                JOptionPane.showMessageDialog(clientiFrame, "Seleziona un prodotto!");
            }
        });

        removeFromCartButton.addActionListener(e -> {
            String selected = cartList.getSelectedValue();
            if (selected != null) {
                String nomeProdotto = estraiNomeProdottoCarrello(selected);
                carrello.remove(nomeProdotto);
                aggiornaCarrello(cartModel, totalLabel);
            } else {
                JOptionPane.showMessageDialog(clientiFrame, "Seleziona un item dal carrello!");
            }
        });

        clearCartButton.addActionListener(e -> {
            carrello.clear();
            aggiornaCarrello(cartModel, totalLabel);
            JOptionPane.showMessageDialog(clientiFrame, "Carrello svuotato!");
        });

        orderButton.addActionListener(e -> {
            if (carrello.isEmpty()) {
                JOptionPane.showMessageDialog(clientiFrame, "Il carrello è vuoto!");
            } else {
                inviaOrdine(clientiFrame);
                aggiornaCarrello(cartModel, totalLabel);
            }
        });

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
        JButton eliminaProdotto = new JButton("Elimina prodotto");

        stileBottone(mostraInventario, bottoneColore, testoColore);
        stileBottone(aggiungiProdotto, bottoneColore, testoColore);
        stileBottone(rifornisciProdotto, bottoneColore, testoColore);
        stileBottone(rimuoviProdotto, bottoneColore, testoColore);
        stileBottone(eliminaProdotto, bottoneColore, testoColore);
        
        panel.add(mostraInventario);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(aggiungiProdotto);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(rifornisciProdotto);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(rimuoviProdotto);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(eliminaProdotto);
        
        mostraInventario.addActionListener(e -> mostraInventarioCompleto());
        aggiungiProdotto.addActionListener(e -> aggiungiNuovoProdotto(personaleFrame));
        rifornisciProdotto.addActionListener(e -> rifornisciProdotto(personaleFrame));
        rimuoviProdotto.addActionListener(e -> rimuoviDalMagazzino(personaleFrame));
        eliminaProdotto.addActionListener(e -> eliminaDalMagazzino(personaleFrame));
        
        

        personaleFrame.add(panel, BorderLayout.CENTER);
        personaleFrame.setVisible(true);
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

    private static void mostraInventarioCompleto() {
        JFrame inventarioFrame = new JFrame("📦 Inventario Completo - Sistema Bar");
        inventarioFrame.setSize(900, 650);
        inventarioFrame.setLocationRelativeTo(null);
        inventarioFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        Color sfondo = new Color(255, 248, 220);
        Color headerColor = new Color(101, 67, 33);
        Color cardColor = Color.WHITE;
        Color accentColor = new Color(139, 69, 19);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(sfondo);
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(headerColor);
        headerPanel.setPreferredSize(new Dimension(900, 80));
        headerPanel.setLayout(new BorderLayout());
        
        JLabel titleLabel = new JLabel("📦 INVENTARIO BAR", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("Gestione completa prodotti e scorte", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
        subtitleLabel.setForeground(new Color(255, 248, 220));
        
        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setBackground(headerColor);
        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);
        
        headerPanel.add(titlePanel, BorderLayout.CENTER);
        
        // Panel per le statistiche
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        statsPanel.setBackground(sfondo);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        // Raccogli TUTTI i prodotti dal file
        String inventarioRaw = DataManagementSystem.fileToString();
        ArrayList<ProductInfo> allProducts = new ArrayList<>();
        int totaleProdotti = 0;
        int prodottiDisponibili = 0;
        int prodottiInEsaurimento = 0;
        double valoreInventario = 0.0;
        
        if (!inventarioRaw.trim().isEmpty()) {
            String[] blocks = inventarioRaw.split("\\}");
            
            for (String block : blocks) {
                if (block.trim().isEmpty()) continue;
                
                try {
                    String[] lines = block.split("\n");
                    String nome = "";
                    double prezzo = 0;
                    int quantita = 0;
                    
                    for (String line : lines) {
                        line = line.trim();
                        if (line.startsWith("Item=")) {
                            nome = line.substring(5, line.length() - 1).trim();
                        } else if (line.startsWith("Price=")) {
                            prezzo = Double.parseDouble(line.substring(6, line.length() - 1).trim());
                        } else if (line.startsWith("Quantity=")) {
                            quantita = Integer.parseInt(line.substring(9, line.length() - 1).trim());
                        }
                    }
                    
                    if (!nome.isEmpty()) {
                        allProducts.add(new ProductInfo(nome, prezzo, quantita));
                        totaleProdotti++;
                        if (quantita > 0) {
                            prodottiDisponibili++;
                            valoreInventario += quantita * prezzo;
                        }
                        if (quantita > 0 && quantita < 10) {
                            prodottiInEsaurimento++;
                        }
                    }
                } catch (Exception e) {
                    // Ignora errori di parsing
                }
            }
        }
        
        statsPanel.add(createStatCard("📊 Totale Prodotti", String.valueOf(totaleProdotti), accentColor));
        statsPanel.add(createStatCard("✅ Disponibili", String.valueOf(prodottiDisponibili), new Color(34, 139, 34)));
        statsPanel.add(createStatCard("⚠️ In Esaurimento", String.valueOf(prodottiInEsaurimento), new Color(255, 140, 0)));
        statsPanel.add(createStatCard("💰 Valore Tot.", "€" + String.format("%.2f", valoreInventario), new Color(30, 144, 255)));
        
        JPanel inventarioPanel = new JPanel(new BorderLayout());
        inventarioPanel.setBackground(sfondo);
        inventarioPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
        tablePanel.setBackground(cardColor);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JPanel tableHeader = new JPanel(new GridLayout(1, 4));
        tableHeader.setBackground(new Color(245, 245, 245));
        tableHeader.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel[] headers = {
            new JLabel("🏷️ PRODOTTO", SwingConstants.LEFT),
            new JLabel("💰 PREZZO", SwingConstants.CENTER),
            new JLabel("📦 QUANTITÀ", SwingConstants.CENTER),
            new JLabel("📊 STATO", SwingConstants.CENTER)
        };
        
        for (JLabel header : headers) {
            header.setFont(new Font("SansSerif", Font.BOLD, 12));
            header.setForeground(headerColor);
            tableHeader.add(header);
        }
        
        tablePanel.add(tableHeader);
        
        // Group products by category
        Map<String, ArrayList<ProductInfo>> categorizedProducts = new HashMap<>();
        categorizedProducts.put("🍩 Colazione", new ArrayList<>());
        categorizedProducts.put("🥤 Bevande", new ArrayList<>());
        categorizedProducts.put("🥪 Salati", new ArrayList<>());
        categorizedProducts.put("🍌 Snack", new ArrayList<>());
        categorizedProducts.put("📦 Altri Prodotti", new ArrayList<>());
        
        for (ProductInfo product : allProducts) {
            boolean categorized = false;
            
            for (String categoria : new String[]{"🍩 Colazione", "🥤 Bevande", "🥪 Salati", "🍌 Snack"}) {
                String[] prodottiCategoria = getProdottiPerCategoria(categoria);
                for (String nomePredefinito : prodottiCategoria) {
                    if (product.nome.equalsIgnoreCase(nomePredefinito)) {
                        categorizedProducts.get(categoria).add(product);
                        categorized = true;
                        break;
                    }
                }
                if (categorized) break;
            }
            
            if (!categorized) {
                categorizedProducts.get("📦 Altri Prodotti").add(product);
            }
        }
        
        // Display products by category
        for (String categoria : new String[]{"🍩 Colazione", "🥤 Bevande", "🥪 Salati", "🍌 Snack", "📦 Altri Prodotti"}) {
            ArrayList<ProductInfo> prodottiCategoria = categorizedProducts.get(categoria);
            
            if (!prodottiCategoria.isEmpty()) {
                JPanel categorySeparator = new JPanel();
                categorySeparator.setBackground(accentColor);
                categorySeparator.setPreferredSize(new Dimension(0, 2));
                categorySeparator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
                tablePanel.add(Box.createRigidArea(new Dimension(0, 10)));
                tablePanel.add(categorySeparator);
                
                JLabel categoryLabel = new JLabel(categoria);
                categoryLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
                categoryLabel.setForeground(accentColor);
                categoryLabel.setBorder(BorderFactory.createEmptyBorder(8, 5, 5, 5));
                tablePanel.add(categoryLabel);
                
                for (ProductInfo product : prodottiCategoria) {
                    JPanel productRow = createProductRow(product.nome, 
                                                         String.format("%.2f", product.prezzo), 
                                                         String.valueOf(product.quantita));
                    tablePanel.add(productRow);
                }
            }
        }
        
        JScrollPane scrollPane = new JScrollPane(tablePanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        
        inventarioPanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        actionPanel.setBackground(sfondo);
        
        JButton refreshButton = new JButton("🔄 Aggiorna");
        JButton exportButton = new JButton("📋 Esporta");
        JButton closeButton = new JButton("❌ Chiudi");
        
        JButton[] buttons = {refreshButton, exportButton, closeButton};
        Color[] buttonColors = {
            new Color(34, 139, 34),
            new Color(30, 144, 255),
            new Color(178, 34, 34)
        };
        
        for (int i = 0; i < buttons.length; i++) {
            styleActionButton(buttons[i], buttonColors[i]);
        }
        
        refreshButton.addActionListener(e -> {
            inventarioFrame.dispose();
            mostraInventarioCompleto();
        });
        
        exportButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(inventarioFrame, 
                "Funzione di esportazione non ancora implementata.\nI dati sono salvati in: " + DATA_FILE);
        });
        
        closeButton.addActionListener(e -> inventarioFrame.dispose());
        
        actionPanel.add(refreshButton);
        actionPanel.add(exportButton);
        actionPanel.add(closeButton);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(statsPanel, BorderLayout.PAGE_START);
        mainPanel.add(inventarioPanel, BorderLayout.CENTER);
        mainPanel.add(actionPanel, BorderLayout.SOUTH);
        
        inventarioFrame.add(mainPanel);
        inventarioFrame.setVisible(true);
    }

    private static JPanel createStatCard(String title, String value, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
        titleLabel.setForeground(new Color(100, 100, 100));
        
        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        valueLabel.setForeground(accentColor);
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
    }

    private static JPanel createProductRow(String nome, String prezzo, String quantita) {
        JPanel row = new JPanel(new GridLayout(1, 4));
        row.setBackground(Color.WHITE);
        row.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        
        if (Math.random() > 0.5) {
            row.setBackground(new Color(248, 248, 248));
        }
        
        JLabel nomeLabel = new JLabel(nome);
        nomeLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        
        JLabel prezzoLabel = new JLabel("€" + prezzo, SwingConstants.CENTER);
        prezzoLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        prezzoLabel.setForeground(new Color(30, 144, 255));
        
        JLabel quantitaLabel = new JLabel(quantita, SwingConstants.CENTER);
        quantitaLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        
        JLabel statoLabel = new JLabel();
        statoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statoLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
        
        int qty = Integer.parseInt(quantita);
        if (qty == 0) {
            statoLabel.setText("❌ ESAURITO");
            statoLabel.setForeground(new Color(178, 34, 34));
        } else if (qty < 10) {
            statoLabel.setText("⚠️ SCARSO");
            statoLabel.setForeground(new Color(255, 140, 0));
        } else if (qty < 25) {
            statoLabel.setText("🟡 MEDIO");
            statoLabel.setForeground(new Color(255, 193, 7));
        } else {
            statoLabel.setText("✅ OK");
            statoLabel.setForeground(new Color(34, 139, 34));
        }
        
        row.add(nomeLabel);
        row.add(prezzoLabel);
        row.add(quantitaLabel);
        row.add(statoLabel);
        
        return row;
    }

    private static void styleActionButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(120, 35));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
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
                
                // Check if item already exists
                String existingItem = DataManagementSystem.findItem(nome);
                if (!existingItem.equals("Item not found or error occurred")) {
                    JOptionPane.showMessageDialog(parent, 
                        "Un prodotto con questo nome esiste già!\nUsa 'Rifornisci Prodotto' per aggiungere quantità.");
                    return;
                }
                
                DataManagementSystem.makeItem(nome, prezzo, quantita);
                JOptionPane.showMessageDialog(parent, 
                    "Prodotto aggiunto con successo!\n\n" +
                    "NOTA: I nuovi prodotti non fanno parte delle categorie predefinite.\n" +
                    "Saranno visibili solo in 'Tutti i prodotti' nell'Area Clienti.");
                
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
    
    private static void eliminaDalMagazzino(JFrame parent) {
        String prodotto = JOptionPane.showInputDialog(parent, "Nome del prodotto da eliminare:");
        if (prodotto != null && !prodotto.trim().isEmpty()) {
            int conferma = JOptionPane.showConfirmDialog(
                parent,
                "Sei sicuro di voler eliminare il prodotto \"" + prodotto.trim() + "\" dal magazzino?",
                "Conferma eliminazione",
                JOptionPane.YES_NO_OPTION
            );

            if (conferma == JOptionPane.YES_OPTION) {
                DataManagementSystem.deleteItem(prodotto.trim());
                JOptionPane.showMessageDialog(parent, "Prodotto eliminato con successo!");
            }
        }
    }

    private static void caricaProdotti(DefaultListModel<String> model, String categoria) {
        model.clear();
        String[] prodotti;
        
        if (categoria.equals("Tutti i prodotti")) {
            prodotti = getTuttiIProdotti();
        } else {
            prodotti = getProdottiPerCategoria(categoria);
        }
        
        for (String prodotto : prodotti) {
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
                
                if (Integer.parseInt(quantita) > 0) {
                    String displayText = prodotto + " - €" + prezzo + " (Disp: " + quantita + ")";
                    model.addElement(displayText);
                }
            }
        }
    }

    private static String[] getTuttiIProdotti() {
        String[] colazione = getProdottiPerCategoria("🍩 Colazione");
        String[] bevande = getProdottiPerCategoria("🥤 Bevande");
        String[] salati = getProdottiPerCategoria("🥪 Salati");
        String[] snack = getProdottiPerCategoria("🍌 Snack");
        
        ArrayList<String> tutti = new ArrayList<>();
        tutti.addAll(Arrays.asList(colazione));
        tutti.addAll(Arrays.asList(bevande));
        tutti.addAll(Arrays.asList(salati));
        tutti.addAll(Arrays.asList(snack));
        
        return tutti.toArray(new String[0]);
    }

    private static String estraiNomeProdotto(String displayText) {
        return displayText.split(" - €")[0];
    }

    private static String estraiNomeProdottoCarrello(String displayText) {
        return displayText.split(" x")[1].split(" - €")[0].trim();
    }

    private static void aggiungiAlCarrello(String nomeProdotto, int quantita) {
        String itemInfo = DataManagementSystem.findItem(nomeProdotto);
        if (!itemInfo.equals("Item not found or error occurred")) {
            String[] lines = itemInfo.split("\n");
            double prezzo = 0;
            int disponibili = 0;
            
            for (String line : lines) {
                if (line.trim().startsWith("Price=")) {
                    prezzo = Double.parseDouble(line.trim().substring(6, line.trim().length() - 1));
                } else if (line.trim().startsWith("Quantity=")) {
                    disponibili = Integer.parseInt(line.trim().substring(9, line.trim().length() - 1));
                }
            }
            
            if (disponibili >= quantita) {
                if (carrello.containsKey(nomeProdotto)) {
                    CartItem existing = carrello.get(nomeProdotto);
                    if (disponibili >= existing.quantita + quantita) {
                        existing.quantita += quantita;
                    } else {
                        JOptionPane.showMessageDialog(null, "Quantità non disponibile!");
                        return;
                    }
                } else {
                    carrello.put(nomeProdotto, new CartItem(nomeProdotto, prezzo, quantita));
                }
            } else {
                JOptionPane.showMessageDialog(null, "Quantità non disponibile!");
            }
        }
    }

    private static void aggiornaCarrello(DefaultListModel<String> model, JLabel totalLabel) {
        model.clear();
        double totale = 0;
        
        for (CartItem item : carrello.values()) {
            model.addElement(item.quantita + " x " + item.nome + " - €" + 
                            String.format("%.2f", item.prezzo) + " = €" + 
                            String.format("%.2f", item.getTotale()));
            totale += item.getTotale();
        }
        
        totalLabel.setText("Totale: €" + String.format("%.2f", totale));
    }

    private static void inviaOrdine(JFrame parent) {
        Map<String, Integer> ordineMap = new HashMap<>();
        for (CartItem item : carrello.values()) {
            ordineMap.put(item.nome, item.quantita);
        }
        
        boolean disponibile = true;
        StringBuilder messaggioErrore = new StringBuilder();
        
        for (Map.Entry<String, Integer> entry : ordineMap.entrySet()) {
            String itemInfo = DataManagementSystem.findItem(entry.getKey());
            if (!itemInfo.equals("Item not found or error occurred")) {
                String[] lines = itemInfo.split("\n");
                for (String line : lines) {
                    if (line.trim().startsWith("Quantity=")) {
                        int disponibili = Integer.parseInt(line.trim().substring(9, line.trim().length() - 1));
                        if (disponibili < entry.getValue()) {
                            disponibile = false;
                            messaggioErrore.append("- ").append(entry.getKey())
                                         .append(": richiesti ").append(entry.getValue())
                                         .append(", disponibili ").append(disponibili).append("\n");
                        }
                        break;
                    }
                }
            }
        }
        
        if (!disponibile) {
            JOptionPane.showMessageDialog(parent, 
                "Alcuni prodotti non sono più disponibili nella quantità richiesta:\n\n" + 
                messaggioErrore.toString());
            return;
        }
        
        double totale = carrello.values().stream().mapToDouble(CartItem::getTotale).sum();
        
        StringBuilder riepilogo = new StringBuilder("Riepilogo Ordine:\n\n");
        for (CartItem item : carrello.values()) {
            riepilogo.append(item.quantita).append(" x ").append(item.nome)
                    .append(" = €").append(String.format("%.2f", item.getTotale())).append("\n");
        }
        riepilogo.append("\nTotale: €").append(String.format("%.2f", totale));
        riepilogo.append("\n\nConfermare l'ordine?");
        
        int result = JOptionPane.showConfirmDialog(parent, riepilogo.toString(), 
                                                  "Conferma Ordine", JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            DataManagementSystem.removeMultipleItems(ordineMap);
            
            JOptionPane.showMessageDialog(parent, 
                "Ordine inviato con successo!\nTotale: €" + String.format("%.2f", totale) + 
                "\n\nGrazie per il tuo acquisto!");
            
            carrello.clear();
        }
    }
}
