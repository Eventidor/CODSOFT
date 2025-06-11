import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CurrencyConverter extends JFrame {

    private JComboBox<String> baseCurrencyCombo;
    private JComboBox<String> targetCurrencyCombo;
    private JTextField amountField;
    private JLabel resultLabel;
    private Map<String, String> currencySymbols;

    public CurrencyConverter() {
        setTitle("Currency Converter");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initCurrencySymbols();
        initUI();
        loadCurrencies();
    }

    private void initCurrencySymbols() {
        currencySymbols = new HashMap<>();
        currencySymbols.put("USD", "$");
        currencySymbols.put("EUR", "€");
        currencySymbols.put("GBP", "£");
        currencySymbols.put("JPY", "¥");
        currencySymbols.put("AUD", "A$");
        currencySymbols.put("CAD", "C$");
        currencySymbols.put("CHF", "CHF");
        currencySymbols.put("CNY", "¥");
        currencySymbols.put("INR", "₹");
        currencySymbols.put("RUB", "₽");
    }

    private void initUI() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel titleLabel = new JLabel("Currency Converter");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBounds(100, 10, 200, 30);
        panel.add(titleLabel);

        JLabel baseLabel = new JLabel("Base Currency:");
        baseLabel.setBounds(30, 60, 120, 25);
        panel.add(baseLabel);

        baseCurrencyCombo = new JComboBox<>();
        baseCurrencyCombo.setBounds(150, 60, 200, 25);
        panel.add(baseCurrencyCombo);

        JLabel targetLabel = new JLabel("Target Currency:");
        targetLabel.setBounds(30, 100, 120, 25);
        panel.add(targetLabel);

        targetCurrencyCombo = new JComboBox<>();
        targetCurrencyCombo.setBounds(150, 100, 200, 25);
        panel.add(targetCurrencyCombo);

        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setBounds(30, 140, 120, 25);
        panel.add(amountLabel);

        amountField = new JTextField();
        amountField.setBounds(150, 140, 200, 25);
        panel.add(amountField);

        JButton convertBtn = new JButton("Convert");
        convertBtn.setBounds(150, 180, 200, 30);
        panel.add(convertBtn);

        resultLabel = new JLabel(" ");
        resultLabel.setBounds(30, 220, 350, 25);
        panel.add(resultLabel);

        convertBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                convertCurrency();
            }
        });

        add(panel);
    }

    private void loadCurrencies() {
        new Thread(() -> {
            try {
                URL url = new URL("https://v6.exchangerate-api.com/v6/c981639dd140e5adcc7767a2/USD");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                Scanner scanner = new Scanner(url.openStream());
                StringBuilder response = new StringBuilder();
                while (scanner.hasNext()) {
                    response.append(scanner.nextLine());
                }
                scanner.close();
                String jsonResponse = response.toString();
                String[] currencies = jsonResponse.split("\"")[3].split(",");
                SwingUtilities.invokeLater(() -> {
                    for (String currency : currencies) {
                        baseCurrencyCombo.addItem(currency.trim());
                        targetCurrencyCombo.addItem(currency.trim());
                    }
                    baseCurrencyCombo.setSelectedItem("USD");
                    targetCurrencyCombo.setSelectedItem("EUR");
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "Failed to load currency list.", "Error", JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }

    private void convertCurrency() {
        String base = (String) baseCurrencyCombo.getSelectedItem();
        String target = (String) targetCurrencyCombo.getSelectedItem();
        String amountText = amountField.getText().trim();
        if (base == null || target == null) {
            showError("Please select both currencies.");
            return;
        }
        if (amountText.isEmpty()) {
            showError("Please enter an amount.");
            return;
        }
        double amount;
        try {
            amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                showError("Enter amount greater than zero.");
                return;
            }
        } catch (NumberFormatException e) {
            showError("Invalid amount.");
            return;
        }
        resultLabel.setText("Converting...");
        new Thread(() -> {
            try {
                URL url = new URL("https://v6.exchangerate-api.com/v6/c981639dd140e5adcc7767a2/pair/" + base + "/" + target + "/" + amount);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                Scanner scanner = new Scanner(url.openStream());
                StringBuilder response = new StringBuilder();
                while (scanner.hasNext()) {
                    response.append(scanner.nextLine());
                }
                scanner.close();
                String jsonResponse = response.toString();
                double result = Double.parseDouble(jsonResponse.split("\"conversion_result\":")[1].split(",")[0]);
                String symbol = currencySymbols.getOrDefault(target, target + " ");
                String display = String.format("Converted: %s %.4f", symbol, result);
                SwingUtilities.invokeLater(() -> resultLabel.setText(display));
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> showError("Error during conversion."));
            }
        }).start();
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
        resultLabel.setText(" ");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CurrencyConverter conv = new CurrencyConverter();
            conv.setVisible(true);
        });
    }
}

