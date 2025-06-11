import javax.swing.*;
import java.awt.*;

public class ATMInterface extends JFrame {

    private BankAccount userAccount;
    private JLabel messageLabel;
    private JLabel balanceLabel;

    public ATMInterface() {
        super("ATM Interface Simulator");
        userAccount = new BankAccount(1000.00);

        setupInterface();

        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void setupInterface() {
        JPanel mainPanel = new JPanel(new BorderLayout(16, 16));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel headerLabel = new JLabel("Welcome to the ATM Interface Simulator");
        headerLabel.setFont(headerLabel.getFont().deriveFont(Font.BOLD, 22f));
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JButton withdrawBtn = new JButton("Withdraw");
        JButton depositBtn = new JButton("Deposit");
        JButton checkBalanceBtn = new JButton("Check Balance");

        Dimension btnSize = new Dimension(120, 45);
        withdrawBtn.setPreferredSize(btnSize);
        depositBtn.setPreferredSize(btnSize);
        checkBalanceBtn.setPreferredSize(btnSize);

        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.gridx = 0; gbc.gridy = 0;
        centerPanel.add(withdrawBtn, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        centerPanel.add(depositBtn, gbc);

        gbc.gridx = 2; gbc.gridy = 0;
        centerPanel.add(checkBalanceBtn, gbc);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel messagePanel = new JPanel(new BorderLayout(8, 8));
        messageLabel = new JLabel("Please select an operation.");
        messageLabel.setFont(messageLabel.getFont().deriveFont(Font.PLAIN, 14f));
        messageLabel.setForeground(new Color(51, 51, 51));
        messagePanel.add(messageLabel, BorderLayout.CENTER);

        balanceLabel = new JLabel("Current Balance: $ " + String.format("%.2f", userAccount.getBalance()));
        balanceLabel.setFont(balanceLabel.getFont().deriveFont(Font.BOLD, 16f));
        balanceLabel.setForeground(new Color(0, 102, 51));
        balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messagePanel.add(balanceLabel, BorderLayout.SOUTH);

        mainPanel.add(messagePanel, BorderLayout.SOUTH);

        withdrawBtn.addActionListener(e -> doWithdraw());
        depositBtn.addActionListener(e -> doDeposit());
        checkBalanceBtn.addActionListener(e -> showBalance());

        getContentPane().add(mainPanel);
    }

    private void doWithdraw() {
        String input = JOptionPane.showInputDialog(this,
                "Enter amount to withdraw:",
                "Withdraw",
                JOptionPane.PLAIN_MESSAGE);
        if (input == null) {
            messageLabel.setText("Withdrawal cancelled.");
            return;
        }
        input = input.trim();
        if (input.isEmpty()) {
            showError("Withdrawal amount cannot be empty.");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(input);
        } catch (NumberFormatException ex) {
            showError("Please enter a valid number.");
            return;
        }
        if (amount <= 0) {
            showError("Amount must be greater than zero.");
            return;
        }
        if (userAccount.withdraw(amount)) {
            messageLabel.setText("Please collect your cash: $ " + String.format("%.2f", amount));
            refreshBalance();
        } else {
            showError("Insufficient funds.");
        }
    }

    private void doDeposit() {
        String input = JOptionPane.showInputDialog(this,
                "Enter amount to deposit:",
                "Deposit",
                JOptionPane.PLAIN_MESSAGE);
        if (input == null) {
            messageLabel.setText("Deposit cancelled.");
            return;
        }
        input = input.trim();
        if (input.isEmpty()) {
            showError("Deposit amount cannot be empty.");
            return;
        }
        double amount;
        try {
            amount = Double.parseDouble(input);
        } catch (NumberFormatException ex) {
            showError("Please enter a valid number.");
            return;
        }
        if (amount <= 0) {
            showError("Amount must be greater than zero.");
            return;
        }
        userAccount.deposit(amount);
        messageLabel.setText("Deposit successful: $ " + String.format("%.2f", amount));
        refreshBalance();
    }

    private void showBalance() {
        messageLabel.setText("Your balance is: $ " + String.format("%.2f", userAccount.getBalance()));
        refreshBalance();
    }

    private void refreshBalance() {
        balanceLabel.setText("Current Balance: $ " + String.format("%.2f", userAccount.getBalance()));
    }

    private void showError(String text) {
        JOptionPane.showMessageDialog(this,
                text,
                "Error",
                JOptionPane.ERROR_MESSAGE);
        messageLabel.setText(text);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ATMInterface atmFrame = new ATMInterface();
            atmFrame.setVisible(true);
        });
    }

    public static class BankAccount {
        private double balance;

        public BankAccount(double startingBalance) {
            if (startingBalance < 0)
                throw new IllegalArgumentException("Negative starting balance not allowed.");
            balance = startingBalance;
        }

        public boolean withdraw(double amount) {
            if (amount <= 0)
                return false;
            if (amount > balance)
                return false;
            balance -= amount;
            return true;
        }

        public void deposit(double amount) {
            if (amount <= 0)
                throw new IllegalArgumentException("Deposit must be positive.");
            balance += amount;
        }

        public double getBalance() {
            return balance;
        }
    }
}

