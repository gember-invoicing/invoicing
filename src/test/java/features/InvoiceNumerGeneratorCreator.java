package features;

import java.util.function.Supplier;

public class InvoiceNumerGeneratorCreator {
    public Supplier<Long> invoke() {
        return new Supplier<Long>() {
            @Override
            public Long get() {
                return 201601000016L;
            }
        };
    }
}
