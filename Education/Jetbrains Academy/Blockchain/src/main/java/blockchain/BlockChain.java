package blockchain;

import blockchain.common.Serialization;
import blockchain.common.SerializerException;

import java.security.*;
import java.util.*;

public class BlockChain {
    private static final List<BlockChainData> blocks = new ArrayList<>();
    private static final Map<String, Wallet> walletMap = new HashMap<>();
    private static List<Transaction> transactions = new ArrayList<>();
    private static long maxBlockId = 1L;
    private static String previousHash = "0";
    private static int hashZeros;

    private final long initialBalance;
    private final long minerReward;

    public BlockChain(long initialBalance, long minerReward) {
        this.initialBalance = initialBalance;
        this.minerReward = minerReward;
    }

    public int getBlocksSize() {
        return blocks.size();
    }

    public int getHashZeros() {
        return hashZeros;
    }

    public long getBlockId() {
        return maxBlockId;
    }

    public boolean addBlock(String walletId, byte[] data, byte[] sign) {
        boolean added;

        Wallet wallet = getWalletById(walletId);
        if (wallet != null) {
            added = addBlock(wallet, data, sign);
        } else {
            added = false;
        }

        return added;
    }

    private boolean addBlock(Wallet wallet, byte[] data, byte[] sign) {
        boolean added;
        PublicKey publicKey = wallet.getPublicKey();

        if (verify(publicKey, data, sign)) {
            try {
                Block block = Serialization.readObject(data);
                added = addBlock(wallet, block);
            } catch (SerializerException e) {
                e.printStackTrace();
                added = false;
            }
        } else {
            added = false;
        }

        return added;
    }

    private boolean verify(PublicKey publicKey, byte[] data, byte[] sign) {
        boolean valid = false;

        try {
            Signature sig = Signature.getInstance("SHA1withRSA");
            sig.initVerify(publicKey);
            sig.update(data);
            valid = sig.verify(sign);
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
        }
        return valid;
    }

    private boolean addBlock(Wallet wallet, Block block) {
        boolean accept;
        synchronized (walletMap) {
            accept = new Block
                    .Validator(getBlockId(), previousHash, hashZeros)
                    .isValid(block);

            if (accept) {
                final List<Transaction> currentTransactionList = transactions;
                transactions = new ArrayList<>();
                int oldHashZeros = hashZeros;
                updateHashZeros(block.getMilliSeconds());

                final BlockChainData newBlock =
                        new BlockChainData(block, currentTransactionList, oldHashZeros, hashZeros, minerReward);

                blocks.add(newBlock);
                maxBlockId++;
                previousHash = block.getHash();

                updateWalletBalance(wallet, minerReward);
            }
        }

        return accept;
    }

    private void updateHashZeros(long millis) {
        if (millis > 300) {
            if (hashZeros > 0) {
                hashZeros--;
            }
        } else if (millis < 100) {
            hashZeros++;
        }
    }

    public boolean addWallet(String id, PublicKey publicKey) {
        final boolean result;
        synchronized (walletMap) {
            if (walletMap.containsKey(id)) {
                result = false;
            } else {
                walletMap.put(id, new Wallet(id, publicKey, initialBalance));
                result = true;
            }
        }
        return result;
    }

    public long getWalletBalance(String walletId) {
        final long balance;
        final Wallet wallet = getWalletById(walletId);
        if (wallet != null) {
            balance = wallet.getBalance();
        } else {
            balance = 0;
        }
        return balance;
    }

    public long getSum() {
        long balance = 0;
        for (Wallet wallet : walletMap.values()) {
            balance += wallet.getBalance();
        }

        return balance;
    }

    public boolean addTransaction(String walletId, byte[] data, byte[] signature) {
        boolean accept;

        final Wallet wallet = getWalletById(walletId);
        if (wallet != null) {
            accept = addTransaction(wallet, data, signature);
        } else {
            accept = false;
        }

        return accept;
    }

    private Wallet getWalletById(String walletId) {
        synchronized (walletMap) {
            return walletMap.getOrDefault(walletId, null);
        }
    }

    private boolean addTransaction(Wallet wallet, byte[] data, byte[] signature) {
        boolean accept;

        PublicKey publicKey = wallet.getPublicKey();
        if (verify(publicKey, data, signature)) {
            try {
                Transaction transaction = Serialization.readObject(data);
                accept = addTransaction(wallet, transaction);
            } catch (SerializerException e) {
                e.printStackTrace();
                accept = false;
            }
        } else {
            accept = false;
        }

        return accept;
    }

    private boolean addTransaction(Wallet wallet, Transaction transaction) {

        boolean accept = transactionIsValid(wallet, transaction);

        if (accept) {
            final Wallet targetWallet = getWalletById(transaction.getTo());
            if (targetWallet != null) {
                synchronized (walletMap) {
                    transactions.add(transaction);

                    updateWalletBalance(wallet, -transaction.getAmount());
                    updateWalletBalance(targetWallet, transaction.getAmount());
                }
            } else {
                accept = false;
            }
        }

        return accept;
    }

    private void updateWalletBalance(Wallet wallet, long amount) {
        wallet.setBalance(
                wallet.getBalance() + amount
        );
    }

    private boolean transactionIsValid(Wallet wallet, Transaction transaction) {

        // make sure that:
        // - the requester transfers hers/his money
        // - the target is defined
        // - the amount is positive

        boolean valid;
        if (!wallet.getId().equals(transaction.getFrom())) {
            valid = false;
        } else if (transaction.getTo() == null) {
            valid = false;
        } else {
            final long amount = transaction.getAmount();
            valid = amount > 0 && (wallet.getBalance() - amount) >= 0;
        }
        return valid;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public List<BlockChainData> getBlocks() {
        return Collections.unmodifiableList(blocks);
    }

    public static class BlockChainData {
        private final Block block;
        private final List<Transaction> transactions;
        private final int oldHashZeros;
        private final int hashZeros;
        private final long minerReward;

        public BlockChainData(
                Block block,
                List<Transaction> transactions,
                int oldHashZeros,
                int hashZeros,
                long minerReward) {
            this.block = block;
            this.transactions = Collections.unmodifiableList(transactions);
            this.oldHashZeros = oldHashZeros;
            this.hashZeros = hashZeros;
            this.minerReward = minerReward;
        }

        public void print() {
            System.out.println("Block: ");
            System.out.println("Created by " + block.getMinerId());
            System.out.println(String.format("%s gets %d VC", block.getMinerId(), minerReward));
            System.out.println("Id: " + block.getId());
            System.out.println("Timestamp: " + block.getTimestamp());
            System.out.println("Magic number: " + block.getMagicNumber());
            System.out.println("Hash of the previous block:\n" + block.getPreviousHash());
            System.out.println("Hash of the block:\n" + block.getHash());
            if (transactions.isEmpty()) {
                System.out.println("Block data: no transactions");
            } else {
                System.out.println("Block data:");
                for (Transaction transaction : transactions) {
                    System.out.println(String.format("%s gets %d VC", transaction.getTo(), transaction.getAmount()));
                }
            }
            System.out.println("Block was generating for " + block.getMilliSeconds() / 1000 + " seconds.");
            if (oldHashZeros < hashZeros) {
                System.out.println("N was increased to " + hashZeros);

            } else if (oldHashZeros > hashZeros) {
                System.out.println("N was decreased to " + hashZeros);
            } else {
                System.out.println("N stays the same");
            }
            System.out.println("");
        }
    }

    static class Wallet {
        private final String id;
        private final PublicKey publicKey;
        private long balance;

        public Wallet(String id, PublicKey publicKey, long balance) {
            this.id = id;
            this.publicKey = publicKey;
            this.balance = balance;
        }

        public String getId() {
            return id;
        }

        public PublicKey getPublicKey() {
            return publicKey;
        }

        public long getBalance() {
            return balance;
        }

        public void setBalance(long balance) {
            this.balance = balance;
        }
    }
}
