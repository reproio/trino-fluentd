package fluentd;


import com.google.common.collect.ImmutableList;
import io.trino.spi.Plugin;
import io.trino.spi.eventlistener.EventListenerFactory;

public class FluentdPlugin implements Plugin {

    @Override
    public Iterable<EventListenerFactory> getEventListenerFactories()
    {
        return ImmutableList.of(new FluentdListenerFactory());
    }
}
