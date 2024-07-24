package serialization;

import com.google.gson.Gson;

public class Deserializer {
    private final String myObjectToDeserialize;
    private final Object myTypeOfObjectToDeserialize;

    public Deserializer(String theObjectToDeserialize, Object theTypeOfObjectToDeserialize) {
        myObjectToDeserialize = theObjectToDeserialize;
        myTypeOfObjectToDeserialize = theTypeOfObjectToDeserialize;
    }

    public Object deserialize() {
        var deserializeMethod = new Gson();
        return deserializeMethod.fromJson(myObjectToDeserialize, myTypeOfObjectToDeserialize.getClass());
    }
}
