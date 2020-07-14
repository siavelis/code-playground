package blockchain;

import java.io.Serializable;

public class Transaction implements Serializable {

    private final String from;
    private final String to;
    private final long amount;

    public Transaction(String from, String to, long amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public long getAmount() {
        return amount;
    }
}
