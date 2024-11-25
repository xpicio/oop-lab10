package it.unibo.mvc;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 */
public final class DrawNumberApp implements DrawNumberViewObserver {
    private static final String CONFIG_FILE_NAME = "config.yml";
    private static final String LOG_FILE_PATH = System.getProperty("user.home") + System.getProperty("file.separator")
            + "oop-exercise-102-log.txt";

    private final DrawNumber model;
    private final List<DrawNumberView> views;

    /**
     * @param configuration
     * @param views
     *                      the views to attach
     */
    public DrawNumberApp(final Configuration configuration, final DrawNumberView... views) {
        /*
         * Side-effect proof
         */
        this.views = Arrays.asList(Arrays.copyOf(views, views.length));
        for (final DrawNumberView view : views) {
            view.setObserver(this);
            view.start();
        }
        this.model = new DrawNumberImpl(configuration.getMin(), configuration.getMax(), configuration.getAttempts());
    }

    @Override
    public void newAttempt(final int n) {
        try {
            final DrawResult result = model.attempt(n);
            for (final DrawNumberView view : views) {
                view.result(result);
            }
        } catch (IllegalArgumentException e) {
            for (final DrawNumberView view : views) {
                view.numberIncorrect();
            }
        }
    }

    @Override
    public void resetGame() {
        this.model.reset();
    }

    @Override
    @SuppressFBWarnings("DM_EXIT")
    public void quit() {
        /*
         * A bit harsh. A good application should configure the graphics to exit by
         * natural termination when closing is hit. To do things more cleanly, attention
         * should be paid to alive threads, as the application would continue to persist
         * until the last thread terminates.
         */
        System.exit(0);
    }

    /**
     * @param args
     *             ignored
     * @throws FileNotFoundException
     */
    @SuppressWarnings({ "PMD.SystemPrintln", "PMD.AvoidCatchingGenericException" })
    public static void main(final String... args) throws FileNotFoundException {
        final Configuration.Builder configurationBuilder = new Configuration.Builder();

        try {
            final Yaml yaml = new Yaml();
            final InputStream inputStream = DrawNumberApp.class
                    .getClassLoader()
                    .getResourceAsStream(CONFIG_FILE_NAME);
            final Map<String, Integer> configurationFromFile = yaml.load(inputStream);
            // setup configuration from file
            configurationBuilder.setMin(configurationFromFile.get("minimum"))
                    .setMax(configurationFromFile.get("maximum"))
                    .setAttempts(configurationFromFile.get("attempts"));
        } catch (Exception exception) {
            // Yaml.load doesn't document which kind of exception will be throws, so let's
            // use a generic Exception
            System.out.println(
                    "WARNING: Something went wrong parsing YAML file, the default configuration will be used.");
            System.out.println(exception.getMessage());
        }

        new DrawNumberApp(configurationBuilder.build(),
                new DrawNumberViewImpl(),
                new PrintStreamView(System.out),
                new PrintStreamView(LOG_FILE_PATH));
    }

}
