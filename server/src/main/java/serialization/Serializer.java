package serialization;

import com.google.gson.Gson;

public class Serializer {
    private final Object myObjectToSerialize;

    public Serializer(Object theObjectToSerialize) {
        myObjectToSerialize = theObjectToSerialize;
    }

    public String serialize() {
        var serializeMethod = new Gson();
        return serializeMethod.toJson(myObjectToSerialize);
    }
}
