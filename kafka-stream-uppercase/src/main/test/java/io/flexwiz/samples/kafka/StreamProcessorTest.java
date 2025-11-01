package io.flexwiz.samples.kafka;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StreamProcessorTest {

    private TopologyTestDriver testDriver;
    private TestInputTopic<String, String> inputTopic;
    private TestOutputTopic<String, String> outputTopic;

    @BeforeEach
    void setup() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "test");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy:1234");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsBuilder builder = new StreamsBuilder();
        builder.stream("input-topic", Consumed.with(Serdes.String(), Serdes.String()))
               .mapValues(value -> value != null ? value.toUpperCase() : null)
               .to("output-topic", Produced.with(Serdes.String(), Serdes.String()));

        testDriver = new TopologyTestDriver(builder.build(), props);

        inputTopic = testDriver.createInputTopic(
            "input-topic",
            Serdes.String().serializer(),
            Serdes.String().serializer()
        );

        outputTopic = testDriver.createOutputTopic(
            "output-topic",
            Serdes.String().deserializer(),
            Serdes.String().deserializer()
        );
    }

    @AfterEach
    void tearDown() {
        testDriver.close();
    }

    @Test
    void testUppercaseTransformation() {
        inputTopic.pipeInput("key1", "hello world");

        KeyValue<String, String> output = outputTopic.readKeyValue();

        assertEquals("key1", output.key);
        assertEquals("HELLO WORLD", output.value);
    }

    @Test
    void testMultipleMessages() {
        inputTopic.pipeInput("key1", "message1");
        inputTopic.pipeInput("key2", "message2");

        assertEquals("MESSAGE1", outputTopic.readValue());
        assertEquals("MESSAGE2", outputTopic.readValue());
    }
}
