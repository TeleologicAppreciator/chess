package serialization;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class Deserializer {
    private String myObjectToDeserialize;
    private Object myTypeOfObjectToDeserialize;

    public Deserializer(String theObjectToDeserialize, Object theTypeOfObjectToDeserialize) {
        myObjectToDeserialize = theObjectToDeserialize;
        myTypeOfObjectToDeserialize = theTypeOfObjectToDeserialize;
    }

    public Object deserialize() {
        var deserializeMethod = new Gson();
        return deserializeMethod.fromJson(myObjectToDeserialize, myTypeOfObjectToDeserialize.getClass());
    }
}
