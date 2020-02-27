/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import android.util.Base64;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author E-book
 */
public class GsonHelper {
    public static final Gson CUSTOM_GSON = 
        new GsonBuilder()
                // .registerTypeHierarchyAdapter(byte[].class, new ByteArrayToBase64TypeAdapter())
                // .registerTypeAdapter(Date.class, new DateDeserializer())
                .registerTypeAdapter(Integer.class, new IntegerDeserializer())
                .create();

    // Using Android's base64 libraries. This can be replaced with any base64 library.
    private static class ByteArrayToBase64TypeAdapter implements JsonSerializer<byte[]>, JsonDeserializer<byte[]> {
        public byte[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return Base64.decode(json.getAsString(), Base64.NO_WRAP);
        }

        public JsonElement serialize(byte[] src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(Base64.encodeToString(src, Base64.NO_WRAP));
        }
    }

    // Date
    public static class DateDeserializer implements JsonDeserializer<Date> {

        @Override
        public Date deserialize(JsonElement element, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
            String date = element.getAsString();

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            formatter.setTimeZone(TimeZone.getTimeZone("GMT+1")); // UTC
            
            SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            formatter2.setTimeZone(TimeZone.getTimeZone("GMT+1")); // UTC

            try {
                return formatter.parse(date);
            } catch (ParseException e) {
                //System.err.println("Failed to parse Date due to: " +e);
                try {
                    return formatter2.parse(date);
                } catch (ParseException f) {
                    //System.err.println("Failed to parse Date due to: " +e);
                    return null;
                }
            }
        }
    }
    
    // Boolean
    private static class IntegerDeserializer implements JsonDeserializer<Integer>{

        @Override
        public Integer deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
            String booleanValue = je.getAsString();
            return booleanValue.equals("false") ? 0 : (booleanValue.equals("true") ? 1 : Integer.parseInt(booleanValue));
        }
        
    }
}
