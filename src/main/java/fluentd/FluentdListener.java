package fluentd;

import io.airlift.log.Logger;
import io.trino.spi.eventlistener.EventListener;
import io.trino.spi.eventlistener.QueryCompletedEvent;
import io.trino.spi.eventlistener.QueryFailureInfo;
import org.komamitsu.fluency.Fluency;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FluentdListener implements EventListener {

    private static final Logger log = Logger.get(FluentdListener.class);

    private String fluentdTag;

    private Fluency fluency;

    public FluentdListener(Fluency fluency, String fluentdTag) {
        this.fluency = fluency;
        this.fluentdTag = fluentdTag;
    }

    @Override
    public void queryCompleted(QueryCompletedEvent queryCompletedEvent)
    {
        Map<String, Object> event = new HashMap<>();

        // QueryMetadata
        event.put("queryId", queryCompletedEvent.getMetadata().getQueryId());
        event.put("query", queryCompletedEvent.getMetadata().getQuery());
        event.put("uri", queryCompletedEvent.getMetadata().getUri().toString());
        event.put("state", queryCompletedEvent.getMetadata().getQueryState());

        // QueryStatistics
        event.put("cpuTime", queryCompletedEvent.getStatistics().getCpuTime().toMillis());
        event.put("wallTime", queryCompletedEvent.getStatistics().getWallTime().toMillis());
        event.put("queuedTime", queryCompletedEvent.getStatistics().getQueuedTime().toMillis());
        if(queryCompletedEvent.getStatistics().getAnalysisTime().isPresent()) {
            event.put("analysisTime", queryCompletedEvent.getStatistics().getAnalysisTime().get().toMillis());
        }
        event.put("peakUserMemoryBytes", queryCompletedEvent.getStatistics().getPeakUserMemoryBytes());
        event.put("totalBytes", queryCompletedEvent.getStatistics().getTotalBytes());
        event.put("totalRows", queryCompletedEvent.getStatistics().getTotalRows());
        event.put("outputBytes", queryCompletedEvent.getStatistics().getOutputBytes());
        event.put("outputRows", queryCompletedEvent.getStatistics().getOutputRows());
        event.put("writtenBytes", queryCompletedEvent.getStatistics().getWrittenBytes());
        event.put("writtenRows", queryCompletedEvent.getStatistics().getWrittenRows());
        event.put("cumulativeMemory", queryCompletedEvent.getStatistics().getCumulativeMemory());
        event.put("completedSplits", queryCompletedEvent.getStatistics().getCompletedSplits());

        // QueryContext
        event.put("user", queryCompletedEvent.getContext().getUser());
        if(queryCompletedEvent.getContext().getPrincipal().isPresent()) {
            event.put("principal", queryCompletedEvent.getContext().getPrincipal().get());
        }
        if(queryCompletedEvent.getContext().getRemoteClientAddress().isPresent()) {
            event.put("remoteClientAddress", queryCompletedEvent.getContext().getRemoteClientAddress().get());
        }
        if(queryCompletedEvent.getContext().getUserAgent().isPresent()) {
            event.put("userAgent", queryCompletedEvent.getContext().getUserAgent().get());
        }
        if(queryCompletedEvent.getContext().getClientInfo().isPresent()) {
            event.put("clientInfo", queryCompletedEvent.getContext().getClientInfo().get());
        }
        if(queryCompletedEvent.getContext().getSource().isPresent()) {
            event.put("source", queryCompletedEvent.getContext().getSource().get());
        }
        if(queryCompletedEvent.getContext().getCatalog().isPresent()) {
            event.put("catalog", queryCompletedEvent.getContext().getCatalog().get());
        }
        if(queryCompletedEvent.getContext().getSchema().isPresent()) {
            event.put("schema", queryCompletedEvent.getContext().getSchema().get());
        }
        if(queryCompletedEvent.getContext().getResourceGroupId().isPresent()) {
            event.put("resourceGroupId", queryCompletedEvent.getContext().getResourceGroupId().get());
        }

        // QueryFailureInfo
        if(queryCompletedEvent.getFailureInfo().isPresent()) {
            QueryFailureInfo queryFailureInfo = queryCompletedEvent.getFailureInfo().get();
            event.put("errorCode", queryFailureInfo.getErrorCode());
            event.put("failureHost", queryFailureInfo.getFailureHost().orElse(""));
            event.put("failureMessage", queryFailureInfo.getFailureMessage().orElse(""));
            event.put("failureTask", queryFailureInfo.getFailureTask().orElse(""));
            event.put("failureType", queryFailureInfo.getFailureType().orElse(""));
            event.put("failuresJson", queryFailureInfo.getFailuresJson());
        }

        event.put("createTime", queryCompletedEvent.getCreateTime().toEpochMilli());
        event.put("endTime", queryCompletedEvent.getEndTime().toEpochMilli());
        event.put("executionStartTime", queryCompletedEvent.getExecutionStartTime().toEpochMilli());

        try {
            fluency.emit(fluentdTag, event);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

    }
}
