package lu.kolja.expandedae.mixin.misc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import appeng.client.gui.MathExpressionParser;

@Mixin(value = MathExpressionParser.class, remap = false)
public class MixinMathExpressionParser {

    /**
     * @author Kolja
     * @reason Add support for exponents, factorials, and lowercase-e scientific notation
     */
    @Overwrite
    public static Optional<BigDecimal> parse(String expression, DecimalFormat decimalFormat) {
        // Parse using the Shunting Yard Algorithm

        List<Object> output = new ArrayList<>();
        Stack<Character> operatorStack = new Stack<>();
        boolean wasNumberOrRightBracket = false;

        for (int i = 0; i < expression.length();) {
            if (Character.isWhitespace(expression.charAt(i))) {
                i++;
                continue;
            }

            // First try to parse as scientific notation (e.g., 2e5, 1.5e-2)
            if (!wasNumberOrRightBracket || expression.charAt(i) != '-') {
                int start = i;
                int numLen = 0;
                boolean hasE = false;
                boolean isValidScientific = false;

                // Look ahead to check for scientific notation format
                while (i + numLen < expression.length()) {
                    char c = expression.charAt(i + numLen); // Don't convert to uppercase

                    // First part: digits and decimal point
                    if (!hasE && (Character.isDigit(c) || c == '.')) {
                        numLen++;
                        continue;
                    }

                    // Found 'e' character - only accept lowercase
                    if (!hasE && (c == 'e')) {
                        hasE = true;
                        numLen++;
                        continue;
                    }

                    // After 'e': optional sign and digits
                    if (hasE && numLen == i - start + 1) { // Right after e
                        if (c == '+' || c == '-') {
                            numLen++;
                            continue;
                        }
                    }

                    if (hasE && Character.isDigit(c)) {
                        numLen++;
                        isValidScientific = true; // Need at least one digit after e
                        continue;
                    }

                    break;
                }

                // If we found valid scientific notation
                if (hasE && isValidScientific) {
                    try {
                        String numStr = expression.substring(start, start + numLen);
                        BigDecimal decimal = new BigDecimal(numStr);
                        output.add(decimal);
                        i = start + numLen;
                        wasNumberOrRightBracket = true;
                        continue;
                    } catch (NumberFormatException e) {
                        // If parsing fails, fall back to regular number parsing
                    }
                }

                // Try regular number parsing with DecimalFormat
                var position = new ParsePosition(i);
                Number parsedNumber = decimalFormat.parse(expression, position);
                if (position.getErrorIndex() == -1) { // no error
                    if (!(parsedNumber instanceof BigDecimal decimal)) {
                        // NaN or infinity
                        return Optional.empty();
                    }
                    output.add(decimal);
                    i = position.getIndex();
                    wasNumberOrRightBracket = true;
                    continue;
                }
            }

            char currentOperator = expression.charAt(i);

            // Check for factorial operator
            if (currentOperator == '!' && wasNumberOrRightBracket) {
                // Process factorial immediately since it's a postfix operator
                output.add('!');
                i++;
                continue;
            }

            if (currentOperator == '-' && !wasNumberOrRightBracket) {
                currentOperator = 'u'; // unitary minus
            }

            wasNumberOrRightBracket = false;

            switch (currentOperator) {
                case '(', 'u' -> operatorStack.push(currentOperator);
                case ')' -> {
                    while (true) {
                        if (operatorStack.isEmpty()) {
                            return Optional.empty(); // mismatched parenthesis
                        }
                        char operator = operatorStack.pop();
                        if (operator == '(') {
                            break;
                        } else {
                            output.add(operator);
                        }
                    }
                    wasNumberOrRightBracket = true;
                }
                case '+', '-', '*', '/', '^' -> {
                    while (!operatorStack.isEmpty()) {
                        char operator = operatorStack.peek();
                        if (operator != '(' && precedenceCheck(operator, currentOperator)) {
                            operatorStack.pop();
                            output.add(operator);
                        } else {
                            break;
                        }
                    }
                    operatorStack.push(currentOperator);
                }
                default -> {
                    return Optional.empty();
                }
            }
            i++;
        }

        while (!operatorStack.isEmpty()) {
            output.add(operatorStack.pop());
        }

        Stack<BigDecimal> number = new Stack<>();

        for (Object object : output) {
            if (object instanceof BigDecimal bigDecimal) {
                number.push(bigDecimal);
            } else {
                char currentOperator = (char) object;
                if (currentOperator == '!') {
                    // Factorial is a unary operator, so we only need one operand
                    if (number.isEmpty()) {
                        return Optional.empty();
                    }
                    BigDecimal operand = number.pop();

                    // Check if the number is a non-negative integer
                    if (operand.compareTo(BigDecimal.ZERO) < 0 || operand.scale() > 0) {
                        return Optional.empty(); // Factorial only defined for non-negative integers
                    }

                    try {
                        int n = operand.intValueExact();
                        if (n > 20) {
                            // Factorial grows very quickly, limit to reasonable size to prevent overflow
                            return Optional.empty();
                        }

                        BigDecimal result = BigDecimal.ONE;
                        for (int i = 2; i <= n; i++) {
                            result = result.multiply(new BigDecimal(i));
                        }
                        number.push(result);
                    } catch (ArithmeticException e) {
                        // Number too large for int
                        return Optional.empty();
                    }
                } else if (currentOperator != 'u') {
                    if (number.size() < 2) {
                        return Optional.empty();
                    } else {
                        BigDecimal right = number.pop();
                        BigDecimal left = number.pop();
                        switch (currentOperator) {
                            case '+' -> number.push(right.add(left));
                            case '*' -> number.push(right.multiply(left));
                            case '-' -> number.push(left.subtract(right));
                            case '/' -> {
                                if (right.compareTo(BigDecimal.ZERO) == 0) {
                                    return Optional.empty(); // division by zeroes
                                } else {
                                    number.push(left.divide(right, 8, RoundingMode.FLOOR));
                                }
                            }
                            case '^' -> {
                                if (right.scale() > 0) {
                                    // For non-integer exponents, a different approach would be needed
                                    // This implementation only handles integer exponents
                                    return Optional.empty();
                                }
                                try {
                                    int exponent = right.intValueExact();
                                    if (exponent < 0 && left.compareTo(BigDecimal.ZERO) == 0) {
                                        return Optional.empty(); // 0^-n is undefined
                                    }
                                    // Handle negative exponents
                                    if (exponent < 0) {
                                        BigDecimal result = BigDecimal.ONE.divide(left.pow(-exponent), 8, RoundingMode.FLOOR);
                                        number.push(result);
                                    } else {
                                        number.push(left.pow(exponent));
                                    }
                                } catch (ArithmeticException e) {
                                    // If exponent is too large for int
                                    return Optional.empty();
                                }
                            }
                            case '(', ')' -> {
                                return Optional.empty(); // should not have any remaining parenthesis in the stack
                            }
                            default -> {
                                throw new IllegalStateException("Unreachable character : " + currentOperator);
                            }
                        }
                    }
                } else {
                    if (number.isEmpty()) {
                        return Optional.empty();
                    } else {
                        number.push(number.pop().negate());
                    }
                }
            }
        }

        if (number.size() != 1) {
            return Optional.empty();
        } else {
            return Optional.of(number.pop().stripTrailingZeros());
        }
    }

    /**
     * @author Kolja
     * @reason Add support for exponents and factorials
     */
    @Overwrite
    private static int getPrecedence(char operator) {
        return switch (operator) {
            case '!' -> -1; // Highest precedence (factorial)
            case 'u' -> 0;  // Next highest precedence (unary minus)
            case '^' -> 1;  // Exponent operator
            case '/', '*' -> 2;
            case '+', '-' -> 3;
            default -> throw new IllegalArgumentException("Invalid Operator : " + operator);
        };
    }

    /**
     * @author Kolja
     * @reason Add support for exponents and factorials
     */
    @Overwrite
    private static boolean precedenceCheck(char first, char second) {
        // For right-associative operators like ^, we need a special case
        if (first == '^' && second == '^') {
            return false;  // Exponentiation is right-associative
        }
        return getPrecedence(first) < getPrecedence(second);
    }
}