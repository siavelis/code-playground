package blockchain;

import blockchain.common.BusyWaiting;

import java.util.List;
import java.util.Random;

public class Shopper implements Runnable {
    private static boolean sendTransactions = true;

    private final BlockChain chain;
    private final Wallet wallet;
    private final String walletId;
    private final List<String> walletIds;
    private final Random random;

    public Shopper(BlockChain chain, Wallet wallet, List<String> walletIds) {
        this.chain = chain;
        this.wallet = wallet;
        this.walletId = wallet.getWalletId();
        this.walletIds = walletIds;
        random = new Random();
    }

    synchronized public static void stop() {
        sendTransactions = false;
    }

    @Override
    public void run() {
        final BusyWaiting busyWaiting = new BusyWaiting();
        while (sendTransactions) {
            busyWaiting.sleep(50);
            spendMoney();
        }
    }

    public void spendMoney() {
        long amount = chain.getWalletBalance(walletId);
        long limit = amount / 2;
        if (limit > 0) {
            long amountToMove = random.nextInt((int) limit) + 1;
            final String to = walletIds.get(random.nextInt(walletIds.size()));
            sendTransaction(to, amountToMove);
        }
    }

    private void sendTransaction(String to, long amount) {
        byte[] transactionData = wallet.serializeObject(new Transaction(walletId, to, amount));
        byte[] signature = wallet.signData(transactionData);
        chain.addTransaction(walletId, transactionData, signature);
    }
}
