package uta.cse3310.Events;

public class EventHolder<T> {
    public String type;
    public T eventData;

    public EventHolder(String type, T data) {
        this.type = type;
        this.eventData = data;
    }
}
