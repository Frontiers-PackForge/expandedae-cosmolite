package lu.kolja.expandedae.helper.misc;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class NumberUtil {
    private static final String[] UNITS = {"", "K", "M", "G", "T", "P", "E", "Y", "Z", "R", "Q"};

    private static final Style NUMBER = Style.EMPTY.withColor(0x886EFF);
    private static final Style UNIT = Style.EMPTY.withColor(0xEE82EE);
    private static final DecimalFormat DF = new DecimalFormat("#.##");

    public static String formatBigInt(BigInteger number) {
        if (number.compareTo(BigInteger.ZERO) < 0) {
            return "-" + formatBigInt(number.negate());
        }

        int unitIndex = 0;
        BigDecimal temp = new BigDecimal(number, 0);
        while (temp.compareTo(BigDecimal.TEN.pow(3)) >= 0 && unitIndex <= UNITS.length) {
            temp = temp.divide(BigDecimal.TEN.pow(3), 3, RoundingMode.HALF_DOWN);
            unitIndex++;
        }

        String formattedNumber = DF.format(temp.doubleValue());

//        if (formattedNumber.endsWith(".00")) {
//            formattedNumber = formattedNumber.substring(0, formattedNumber.length() - 3);
//        }

        if (unitIndex >= UNITS.length) {
            return String.format("%.2e", number.doubleValue());
        }

        return formattedNumber + UNITS[unitIndex];
    }

    public static String formatDouble(double number) {
        if (number < 1000) return DF.format(number);
        int unit = Math.min((int) (Math.log10(number) / 3), UNITS.length - 1);
        return DF.format(number / Math.pow(1000, unit)) + UNITS[unit];
    }

    public static Component numberText(BigInteger number) {
        String text = formatBigInt(number);
        if (text.matches(".*[a-zA-Z]$")) {
            return Component.literal(text.substring(0, text.length() - 1)).withStyle(NUMBER)
                .append(Component.literal(text.substring(text.length() - 1)).withStyle(UNIT));
        } else if (text.contains("e+")) {
            String[] split = text.split("e\\+");
            return Component.literal(split[0]).withStyle(NUMBER)
                .append(Component.literal("e+").withStyle(UNIT))
                .append(Component.literal(split[1]).withStyle(NUMBER));
        }
        return Component.literal(text).withStyle(NUMBER);
    }
}