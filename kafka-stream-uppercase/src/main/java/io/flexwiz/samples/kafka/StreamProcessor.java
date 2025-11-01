package io.flexwiz.samples.kafka;

import io.flexwiz.samples.kafka.config.KafkaConfig;
import io.flexwiz.samples.kafka.service.MessageTransformer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class StreamProcessor {

    private static final Logger logger = LoggerFactory.getLogger(StreamProcessor.class);
    private final MessageTransformer transformer;

    public StreamProcessor() {
        this.transformer = new MessageTransformer();
    }

    public void start() {
        Properties props = KafkaConfig.getStreamsConfig();
        String inputTopic = KafkaConfig.getInputTopic();
        String outputTopic = KafkaConfig.getOutputTopic();
        logger.debug("Configuration Kafka Streams: {}", props);

        StreamsBuilder builder = new StreamsBuilder();

        // Construction du pipeline de traitement
        KStream<String, String> inputStream = builder.stream(
            inputTopic,
            Consumed.with(Serdes.String(), Serdes.String())
        );

        KStream<String, String> transformedStream = inputStream
            .peek((key, value) -> logger.info("Message reçu - Key: {}, Value: {}", key, value))
            .mapValues(transformer::transformToUppercase)
            .peek((key, value) -> logger.info("Message transformé - Key: {}, Value: {}", key, value));

        transformedStream.to(
            outputTopic,
            Produced.with(Serdes.String(), Serdes.String())
        );

        final KafkaStreams streams = new KafkaStreams(builder.build(), props);
        final CountDownLatch latch = new CountDownLatch(1);

        // Gestion du shutdown
        Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
            @Override
            public void run() {
                logger.info("Arrêt de l'application Kafka Streams...");
                streams.close();
                latch.countDown();
            }
        });

        try {
            logger.info("Démarrage de l'application Kafka Streams");
            logger.info("Input topic: {}", inputTopic);
            logger.info("Output topic: {}", outputTopic);
            streams.start();
            logger.info("Application démarrée avec succès");
            latch.await();
        } catch (Throwable e) {
            logger.error("Erreur fatale", e);
            System.exit(1);
        }

        System.exit(0);
    }

    public static void main(String[] args) {
        new StreamProcessor().start();
    }
}
