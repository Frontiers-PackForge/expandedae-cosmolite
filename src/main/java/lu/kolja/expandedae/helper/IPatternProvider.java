package lu.kolja.expandedae.helper;

import lu.kolja.expandedae.enums.BlockingMode;

public interface IPatternProvider {
    void expandedae$modifyPatterns(boolean rightClick);

    BlockingMode expandedae$getBlockingMode();

    void setBlockingMode(BlockingMode blockingMode);
}
