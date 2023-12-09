package org.example.Task4;
import org.example.Task;
import java.util.ArrayList;
import java.util.Scanner;
public class Account implements Task  {
    public class Transaction {
        private double amount;
        private String transactionType;

        public Transaction(double amount, String transactionType) {
            this.amount = amount;
            this.transactionType = transactionType;
        }
        public double getAmount() {
            return amount;
        }
        public String getTransactionType() {
            return transactionType;
        }
    }

    private double balance;
    private ArrayList<Transaction> transactions;

    public Account(double balance) {
        this.balance = balance;
        this.transactions = new ArrayList<>();
    }
    public Account() {
        this(0);
    }

    public Account(boolean isNew){
        Scanner scanner = new Scanner(System.in);
        Account account = new Account();

        System.out.print("Введите сумму для пополнения счета: ");
        double depositAmount = scanner.nextDouble();
        account.deposit(depositAmount);

        System.out.print("Введите сумму для снятия со счета: ");
        double withdrawalAmount = scanner.nextDouble();
        account.withdraw(withdrawalAmount);

        System.out.print("Введите сумму для дополнительного пополнения счета: ");
        double additionalDeposit = scanner.nextDouble();
        account.deposit(additionalDeposit);

        account.displayTransactions();
        System.out.println("Текущий баланс: " + account.getBalance());
    }

    public void deposit(double amount) {
        balance += amount;
        transactions.add(new Transaction(amount, "Пополнение"));
    }

    public void withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            transactions.add(new Transaction(amount, "Снятие"));
        } else {
            System.out.println("Недостаточно средств!");
        }
    }

    public void displayTransactions() {
        System.out.println("\nИстория операций:");
        for (Transaction transaction : transactions) {
            System.out.println(transaction.getTransactionType() + ": " + transaction.getAmount());
        }
    }

    public double getBalance() {
        return balance;
    }

    public static void main(String[] args) {
        new Account(true);
    }

    @Override
    public void runTask() {
        new Account();
    }
}