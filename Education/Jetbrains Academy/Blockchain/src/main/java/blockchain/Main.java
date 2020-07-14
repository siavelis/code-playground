package blockchain;

import blockchain.common.BusyWaiting;
import blockchain.common.SecureKeys;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    private static final int BLOCKS_TO_PRODUCE = 15;
    private static ExecutorService executor;

    public static void main(String[] args) {
        final long initialBalance = 100;
        final long minerReward = 100;
        final BlockChain chain = new BlockChain(initialBalance, minerReward);

        start(chain);

        final BusyWaiting busyWaiting = new BusyWaiting();
        while (chain.getBlocksSize() < BLOCKS_TO_PRODUCE) {
            busyWaiting.sleep(50);
        }

        stop();

        final List<BlockChain.BlockChainData> blocks = chain.getBlocks();
        for (int i = 0; i < BLOCKS_TO_PRODUCE; i++) {
            final BlockChain.BlockChainData block = blocks.get(i);
            block.print();
        }
    }

    private static void start(BlockChain chain) {
        int minersNum = Runtime.getRuntime().availableProcessors();
        executor = Executors.newFixedThreadPool(minersNum);

        final List<String> minerIds = IntStream
                .rangeClosed(1, minersNum)
                .boxed()
                .map(i -> "miner" + i)
                .collect(Collectors.toList());

        for (String minerId : minerIds) {

            SecureKeys keys;
            try {
                keys = new SecureKeys(1024);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("Error during keys initialization. Root cause: " + e.getMessage(), e);
            }

            final Wallet wallet = new Wallet(minerId, keys);
            final boolean walletAdded = chain.addWallet(wallet.getWalletId(), keys.getPublicKey());
            if (!walletAdded) {
                throw new RuntimeException("Error during wallet initialization.");
            }

            executor.submit(new Miner(chain, wallet, BLOCKS_TO_PRODUCE));

            List<String> walletIds = minerIds
                    .stream()
                    .filter(t -> !t.equals(minerId))
                    .collect(Collectors.toList());
            executor.submit(new Shopper(chain, wallet, walletIds));
        }
    }

    private static void stop() {
        Shopper.stop();
        executor.shutdown();
        try {
            executor.awaitTermination(300, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
