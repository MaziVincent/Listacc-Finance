using System;
using Microsoft.AspNetCore.Http;
using Newtonsoft.Json;

namespace Learn.API.Helpers
{
    public static class Extensions
    {
        public static void AddApplicationError(this HttpResponse response, string message)
        {
            response.Headers.Add("Application-Error",message);
            response.Headers.Add("Access-Control-Expose-Headers", "Application-Error");
            response.Headers.Add("Access-Control-Allow-Origin", "*");
        }

        public  class TrimmingConverter : JsonConverter
        {

            
            public override bool CanConvert(Type objectType)
            {
                return objectType == typeof(string);
            }

            public override object ReadJson(JsonReader reader, Type objectType, object existingValue, JsonSerializer serializer)
            {
                if (reader.TokenType == JsonToken.String)
                if (reader.Value != null)
                    return (reader.Value as string).Trim();

                return reader.Value;
            }

            public override void WriteJson(JsonWriter writer, object value, JsonSerializer serializer)
            {
                var text = (string)value;
                if (text == null)
                writer.WriteNull(); 
                else
                writer.WriteValue(text.Trim());
            }
    }
    }
   
}