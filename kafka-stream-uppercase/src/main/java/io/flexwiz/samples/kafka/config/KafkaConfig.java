
package io.flexwiz.samples.kafka.config;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;

import java.util.Properties;

public class KafkaConfig {
    public static Properties getStreamsConfig() {
        Properties props = new Properties();

        // Configuration de base
        props.put(StreamsConfig.APPLICATION_ID_CONFIG,
            getEnv("APPLICATION_ID", "uppercase-transformer-app"));
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,
            getEnv("KAFKA_BOOTSTRAP_SERVERS", "localhost:9092"));

        // Sérialisation
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG,
            Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,
            Serdes.String().getClass());

        // Performance et traitement
        props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
        props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 1000);

        // Gestion des erreurs
        props.put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG,
            org.apache.kafka.streams.errors.LogAndContinueExceptionHandler.class);

        // Nombre de threads
        props.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 2);

        return props;
    }

    public static String getInputTopic() {
        return getEnv("INPUT_TOPIC", "input-topic");
    }

    public static String getOutputTopic() {
        return getEnv("OUTPUT_TOPIC", "output-topic");
    }

    private static String getEnv(String key, String defaultValue) {
        String value = System.getenv(key);
        return value != null ? value : defaultValue;
    }
}
