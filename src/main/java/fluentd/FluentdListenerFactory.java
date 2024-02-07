package fluentd;

import io.trino.spi.eventlistener.EventListener;
import io.trino.spi.eventlistener.EventListenerFactory;
import org.komamitsu.fluency.Fluency;
import org.komamitsu.fluency.fluentd.FluencyBuilderForFluentd;

import java.io.IOException;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public class FluentdListenerFactory implements EventListenerFactory {

    public String getName() {
        return "presto-fluentd";
    }

    public EventListener create(Map<String, String> map) {
        String fluentdHost = requireNonNull(map.get("event-listener.fluentd-host"), "event-listener.fluentd-host is null");
        String fluentdPort = requireNonNull(map.get("event-listener.fluentd-port"), "event-listener.fluentd-port is null");
        String fluentdTag = requireNonNull(map.get("event-listener.fluentd-tag"), "event-listener.fluentd-tag is null");
        try (Fluency fluency = new FluencyBuilderForFluentd()
                .build(fluentdHost, Integer.parseInt(fluentdPort))){
            return new FluentdListener(fluency, fluentdTag);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
