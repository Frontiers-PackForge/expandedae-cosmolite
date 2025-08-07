package lu.kolja.expandedae.helper.misc;

import java.util.Objects;
import org.jetbrains.annotations.Contract;

/**
 * Custom Tuple implementation with added features
 * @param <A>
 * @param <B>
 */
public class Tuple<A, B> {
    public A a;
    public B b;

    public Tuple(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public A getA() {
        return this.a;
    }

    public void setA(A a) {
        this.a = a;
    }

    public B getB() {
        return this.b;
    }

    public void setB(B b) {
        this.b = b;
    }

    /**
     * Static instantiation method
     */
    @Contract(value = "_, _ -> new", pure = true)
    public static <A, B> Tuple<A, B> of(A a, B b) {
        return new Tuple<>(a, b);
    }

    @SafeVarargs
    public static <A, B> Tuple<A, B>[] arrayOf(Tuple<A, B>... tuples) {
        return tuples;
    }

    /**
     * Checks if the given object is contained in this tuple
     */
    public boolean contains(Object o) {
        return equals(o, a) || equals(o, b);
    }

    public boolean isLeft(Object o) {
        return equals(o, a);
    }
    public boolean isRight(Object o) {
        return equals(o, b);
    }

    /**
     * Swaps the values of this tuple
     * @return {@code Tuple<B, A>}
     */
    public Tuple<B, A> swap() {
        return of(b, a);
    }

    private boolean equals(Object a, Object b) {
        return Objects.equals(a, b);
    }
}