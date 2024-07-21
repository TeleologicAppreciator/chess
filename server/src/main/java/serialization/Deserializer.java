package serialization;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class Deserializer {
    private String myObjectToDeserialize;
    private Object myObjectType;

    public Deserializer(String theObjectToDeserialize, Object theObjectType) {
        myObjectToDeserialize = theObjectToDeserialize;
    }

    public Object deserialize() {
        var deserializeMethod = new Gson();
        return deserializeMethod.fromJson(myObjectToDeserialize, (Type) myObjectType);
    }
}
