package org.backend.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventPublisher {
    private static final EventPublisher INSTANCE = new EventPublisher();
    private final Map<Class<? extends Event>, List<EventListener<? extends Event>>> listeners = new HashMap<>();
    private final ExecutorService executorService;

    private EventPublisher() {
        this.executorService = Executors.newFixedThreadPool(5);
    }

    public static EventPublisher getInstance() {
        return INSTANCE;
    }

    public <T extends Event> void subscribe(Class<T> eventType, EventListener<T> listener) {
        List<EventListener<? extends Event>> eventListeners = listeners.computeIfAbsent(eventType, k -> new ArrayList<>());
        eventListeners.add(listener);
    }

    public <T extends Event> void unsubscribe(Class<? extends EventListener> eventType, EventListener<T> listener) {
        List<EventListener<? extends Event>> eventListeners = listeners.get(eventType);
        if (eventListeners != null) {
            eventListeners.remove(listener);
            if (eventListeners.isEmpty()) {
                listeners.remove(eventType);
            }
        }
    }

    public <T extends Event> void publish(T event) {
        if (event == null) {
            throw new IllegalArgumentException("EventAbtract cannot be null");
        }
        List<EventListener<? extends Event>> eventListeners = listeners.get(event.getClass());
        if (eventListeners != null && !eventListeners.isEmpty()) {
//            List<EventListener<? extends EventAbtract>> listenersCopy = new ArrayList<>(eventListeners);
            for (EventListener<? extends Event> listener : eventListeners) {
                EventListener<T> typedListener = (EventListener<T>) listener;
                executorService.submit(() -> {
                    try {
                        typedListener.onEvent(event);
                    } catch (Exception e) {
                        System.err.println("Error processing event: " + e.getMessage());
                    }
                });
            }
        }
    }

    public void shutdown() {
        try {
            executorService.shutdown();
        } catch (Exception e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public boolean hasListeners(Class<? extends Event> eventType) {
        List<EventListener<? extends Event>> eventListeners = listeners.get(eventType);
        return eventListeners != null && !eventListeners.isEmpty();
    }

    public int getListenerCount(Class<? extends Event> eventType) {
        List<EventListener<? extends Event>> eventListeners = listeners.get(eventType);
        return eventListeners != null ? eventListeners.size() : 0;
    }
}
