package lu.kolja.expandedae.helper.patternprovider;

import lu.kolja.expandedae.enums.BlockingMode;

public interface IPatternProvider {
    void expandedae$modifyPatterns(boolean rightClick);

    BlockingMode expandedae$getBlockingMode();

    void setBlockingMode(BlockingMode blockingMode);
}
