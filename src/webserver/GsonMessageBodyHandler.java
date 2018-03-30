package webserver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GsonMessageBodyHandler implements MessageBodyWriter<Object>, MessageBodyReader<Object> {

	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return true;
	}

	@Override
	public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {
		// System.out.println("Gson: readFrom");
		try (InputStreamReader input = new InputStreamReader(entityStream, "UTF-8")) {
			Gson gson = getGson();
			return gson.fromJson(input, genericType);
		}
	}

	@NotNull
	private Gson getGson() {
		return new GsonBuilder().setPrettyPrinting().serializeNulls().create();
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return true;
	}

	@Override
	public long getSize(Object o, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		// Deprecated and ignored in Jersey 2
		return -1;
	}

	@Override
	public void writeTo(Object o, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
			throws IOException, WebApplicationException {
		// System.out.println("Gson: writeTo");
		try (OutputStreamWriter writer = new OutputStreamWriter(entityStream, "UTF-8")) {
			getGson().toJson(o, genericType, writer);
		}
	}
}