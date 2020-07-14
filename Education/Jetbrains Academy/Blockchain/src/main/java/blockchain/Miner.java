package blockchain;

import blockchain.common.BusyWaiting;

public class Miner implements Runnable {
    private final BlockChain chain;
    private final Wallet wallet;
    private final String walletId;
    private final int maxBlocks;

    public Miner(BlockChain chain, Wallet wallet, int maxBlocks) {
        this.chain = chain;
        this.wallet = wallet;
        this.walletId = wallet.getWalletId();
        this.maxBlocks = maxBlocks;
    }

    @Override
    public void run() {

        final BusyWaiting busyWaiting = new BusyWaiting();
        while (continueExecution()) {
            final Block newBlock = generateBlock();

            final byte[] newBlockData = wallet.serializeObject(newBlock);
            final byte[] sign = wallet.signData(newBlockData);
            chain.addBlock(walletId, newBlockData, sign);
            busyWaiting.sleep(10);
        }
    }


    private boolean continueExecution() {
        return chain.getBlocksSize() < maxBlocks;
    }

    Block generateBlock() {
        final int hashZeros = chain.getHashZeros();
        final long blockId = chain.getBlockId();
        final String previousHash = chain.getPreviousHash();
        return new Block
                .Generator(blockId, previousHash, walletId, hashZeros)
                .mineBlock();
    }

}
