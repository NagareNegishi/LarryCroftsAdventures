package nz.ac.wgtn.swen225.lc.persistency;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;


@JsonSerialize(using = SerialiseRunnable.class)
@JsonDeserialize(using = DeserialiseRunnable.class)
/**
 * Recreation of Runnable functional interface, that allows for serialisation
 * Only works when interacting with static methods and fields
 * Used below reference heavily in initial implementation. Class never used by App so never refactored for project
 * Reference: https://stackoverflow.com/questions/63760920/serializing-and-deserializing-lambda-with-jackson
 * 
 * @author titheradam	300652933
 */
public interface SerialRunnable extends Runnable, Serializable{
	public void run();
}

class SerialiseRunnable extends JsonSerializer<SerialRunnable>{
	
	
	@Override
	/**
	 * Serialises into bytecode before writing as JSON to JsonGenerator
	 */
    public void serialize(SerialRunnable runnable, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            outputStream.writeObject(runnable);
            gen.writeBinary(byteArrayOutputStream.toByteArray());
        }
    }
}

class DeserialiseRunnable extends JsonDeserializer<SerialRunnable> {
    @Override
    public SerialRunnable deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        byte[] value = p.getBinaryValue();
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(value);
             ObjectInputStream inputStream = new ObjectInputStream(byteArrayInputStream)) {
            return (SerialRunnable) inputStream.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException(e);
        }
    }
}

