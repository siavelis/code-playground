package blockchain;

import blockchain.common.StringUtil;

import java.io.Serializable;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Block implements Serializable {

    private final long id;
    private final long timestamp;
    private final String previousHash;
    private final String minerId;
    private int magicNumber;
    private String hash;
    private long milliSeconds;

    public Block(long id, long timestamp, String previousHash, String minerId) {
        this.id = id;
        this.timestamp = timestamp;
        this.previousHash = previousHash;
        this.minerId = minerId;
    }

    public long getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String getHash() {
        return hash;
    }

    public int getMagicNumber() {
        return magicNumber;
    }

    public long getMilliSeconds() {
        return milliSeconds;
    }

    public String getMinerId() {
        return minerId;
    }

    private void setMagicNumber(int magicNumber) {
        this.magicNumber = magicNumber;
    }

    private void setHash(String hash) {
        this.hash = hash;
    }

    private void setMilliSeconds(long milliSeconds) {
        this.milliSeconds = milliSeconds;
    }

    static class Utilities {
        static String getStringToHash(Block block) {
            return String.format("{\"id\": %d, \"timestamp\": %d, \"previousHash\": \"%s\", \"magic\": %d}",
                    block.id, block.timestamp, block.previousHash, block.magicNumber);
        }
    }

    public static class Validator {
        private final long id;
        private final String previousHash;
        private final String hashStart;

        public Validator(long id, String previousHash, int hashZeros) {
            this.id = id;
            this.previousHash = previousHash;
            hashStart = "0".repeat(hashZeros);
        }

        public boolean isValid(Block block) {
            boolean valid = true;
            if (blockIdIsInvalid(block)) {
                valid = false;
            } else if (previousHashIsInvalid(block)) {
                valid = false;
            } else if (hashDoesNotStartWithEnoughZeros(block)) {
                valid = false;
            }

            return valid;
        }

        private boolean blockIdIsInvalid(Block block) {
            return block.id != id;
        }

        private boolean previousHashIsInvalid(Block block) {
            return !block.getPreviousHash().equals(previousHash);
        }

        private boolean hashDoesNotStartWithEnoughZeros(Block block) {
            final boolean result;
            final String hash = block.getHash();
            if (hash == null || !hash.startsWith(hashStart)) {
                result = true;
            } else {
                final String actualHash = StringUtil.applySha256(Utilities.getStringToHash(block));
                result = !actualHash.equals(hash);
            }
            return result;
        }
    }

    public static class Generator {
        private static final int magicNumberEnd = Integer.MAX_VALUE;

        private final long id;
        private final String previousHash;
        private final String minerId;
        private final String hashStart;
        private final Random random;

        public Generator(long id, String previousHash, String minerId, int hashZeros) {
            this.id = id;
            this.previousHash = previousHash;
            this.minerId = minerId;
            hashStart = "0".repeat(hashZeros);
            random = new Random();
        }

        public Block mineBlock() {
            final long timestamp = new Date().getTime();
            final Block block = new Block(id, timestamp, previousHash, minerId);

            String hash;
            do {
                block.setMagicNumber(getNextMagicNumber());
                hash = StringUtil.applySha256(Utilities.getStringToHash(block));
            } while (!hashStartsWithEnoughZeros(hash));

            long seconds = getGenerationMilliSeconds(timestamp);
            block.setHash(hash);
            block.setMilliSeconds(seconds);
            return block;
        }

        private int getNextMagicNumber() {
            return random.nextInt(magicNumberEnd);
        }

        private boolean hashStartsWithEnoughZeros(final String hash) {
            return hash.startsWith(hashStart);
        }

        private long getGenerationMilliSeconds(long startTimestamp) {
            return System.currentTimeMillis() - startTimestamp;
        }
    }


}
