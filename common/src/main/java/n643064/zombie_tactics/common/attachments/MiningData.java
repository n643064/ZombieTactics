package n643064.zombie_tactics.common.attachments;

/*
    This class contains a mining block's position
    and a condition of mine for each of zombie
 */
public class MiningData<T> {
    public boolean doMining;
    public T bp;

    public MiningData() {
        doMining = false;
        // `bp` is Nullable but, if `doMining` is true, `bp` must not be null
        bp = null;
    }
}
