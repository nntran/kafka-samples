package io.flexwiz.samples.kafka.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageTransformer {

    private static final Logger logger = LoggerFactory.getLogger(MessageTransformer.class);

    public String transformToUppercase(String value) {
        if (value == null || value.isEmpty()) {
            logger.warn("Message vide ou null reçu");
            return value;
        }

        try {
            String transformed = value.toUpperCase();
            logger.debug("Message transformé: '{}' -> '{}'", value, transformed);
            return transformed;
        } catch (Exception e) {
            logger.error("Erreur lors de la transformation du message: {}", value, e);
            throw new RuntimeException("Erreur de transformation", e);
        }
    }
}
