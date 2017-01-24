package priceme.api;

/**
 * Handles retrieving the correct api endpoint when given
 * an {@link ExternalApiProvider}
 *
 * @author MarkKewley
 */
public final class ExternalApiUtils {
    private static final String TARGET_API_FORMAT = "https://api.target.com/products/v3/%s?fields=descriptions&id_type=TCIN&key=43cJWpLjH8Z8oR18KdrZDBKAgLLQKJjz";

    private ExternalApiUtils() {}

    public enum ExternalApiProvider {
        TARGET(1);

        private int expectedNumberOfParameters;

        ExternalApiProvider(int expectedNumberOfParameters) {
            this.expectedNumberOfParameters = expectedNumberOfParameters;
        }

        int getExpectedNumberOfParameters() {
            return expectedNumberOfParameters;
        }
    }

    /**
     * Returns the correct api endpoint when given the {@code externalApiProvider}. If the incorrect number
     * of {@code args} are given, an {@code null} will be returned
     *
     * @param externalApiProvider {@link ExternalApiProvider} the endpoint provider requested
     * @param args {@link String[]} the args for the api endpoint
     * @return {@link String} a valid url endpoint, {@code null} otherwise
     */
    public static String getApiEndpoint(ExternalApiProvider externalApiProvider, String... args) {
        switch (externalApiProvider) {
            case TARGET:
                if (args.length == externalApiProvider.getExpectedNumberOfParameters()) {
                    return String.format(TARGET_API_FORMAT, (Object[]) args);
                }
            default:
                return null;
        }
    }
}
