package by.sorface.ssoserver.utils.json;

import java.util.function.Supplier;

public class LazyObjectSerializable {

    private final Supplier<String> stringSupplier;

    public LazyObjectSerializable(final Supplier<String> stringSupplier) {
        this.stringSupplier = stringSupplier;
    }

    @Override
    public String toString() {
        return stringSupplier.get();
    }

}
