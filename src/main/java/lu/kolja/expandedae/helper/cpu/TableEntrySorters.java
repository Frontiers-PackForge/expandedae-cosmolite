package lu.kolja.expandedae.helper.cpu;

import appeng.api.config.SortDir;
import appeng.api.config.SortOrder;
import appeng.menu.me.crafting.CraftingPlanSummaryEntry;
import appeng.menu.me.crafting.CraftingStatusEntry;

import java.util.Comparator;

public class TableEntrySorters {
    public static class Status {
        public static final Comparator<CraftingStatusEntry> DEFAULT = Comparator
                .comparing((CraftingStatusEntry e) -> e.getActiveAmount() + e.getPendingAmount())
                .thenComparing(CraftingStatusEntry::getStoredAmount)
                .reversed();

        public static final Comparator<CraftingStatusEntry> DEFAULT_REV = DEFAULT.reversed();

        public static final Comparator<CraftingStatusEntry> NAME_ASC = Comparator.comparing(
                is -> is.getWhat().getDisplayName().getString(),
                String::compareToIgnoreCase);

        public static final Comparator<CraftingStatusEntry> NAME_DESC = NAME_ASC.reversed();

        public static final Comparator<CraftingStatusEntry> MOD_ASC = Comparator.comparing(
                        (CraftingStatusEntry entry) -> entry.getWhat().getModId(),
                        String::compareToIgnoreCase)
                .thenComparing(NAME_ASC);

        public static final Comparator<CraftingStatusEntry> MOD_DESC = MOD_ASC.reversed();

        public static Comparator<CraftingStatusEntry> getComparator (SortOrder order, SortDir dir){
            return switch (order) {
                case NAME -> dir == SortDir.ASCENDING ? NAME_ASC : NAME_DESC;
                case MOD -> dir == SortDir.ASCENDING ? MOD_ASC : MOD_DESC;
                case AMOUNT -> dir == SortDir.ASCENDING ? DEFAULT : DEFAULT_REV;
            };
        }
    }

    public static class Plan {
        public static final Comparator<CraftingPlanSummaryEntry> DEFAULT = Comparator
                .comparing(CraftingPlanSummaryEntry::getMissingAmount)
                .thenComparing(CraftingPlanSummaryEntry::getStoredAmount)
                .thenComparing(CraftingPlanSummaryEntry::getCraftAmount)
                .reversed();

        public static final Comparator<CraftingPlanSummaryEntry> DEFAULT_REV = DEFAULT.reversed();

        public static final Comparator<CraftingPlanSummaryEntry> NAME_ASC = Comparator.comparing(
                is -> is.getWhat().getDisplayName().getString(),
                String::compareToIgnoreCase);

        public static final Comparator<CraftingPlanSummaryEntry> NAME_DESC = NAME_ASC.reversed();

        public static final Comparator<CraftingPlanSummaryEntry> MOD_ASC = Comparator.comparing(
                        (CraftingPlanSummaryEntry entry) -> entry.getWhat().getModId(),
                        String::compareToIgnoreCase)
                .thenComparing(NAME_ASC);

        public static final Comparator<CraftingPlanSummaryEntry> MOD_DESC = MOD_ASC.reversed();

        public static Comparator<CraftingPlanSummaryEntry> getComparator(SortOrder order, SortDir dir) {
            return switch (order) {
                case NAME -> dir == SortDir.ASCENDING ? NAME_ASC : NAME_DESC;
                case MOD -> dir == SortDir.ASCENDING ? MOD_ASC : MOD_DESC;
                case AMOUNT -> dir == SortDir.ASCENDING ? DEFAULT : DEFAULT_REV;
            };
        }
    }
}