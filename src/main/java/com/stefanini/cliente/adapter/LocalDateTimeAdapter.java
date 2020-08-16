package com.stefanini.cliente.adapter;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

public class LocalDateTimeAdapter implements JsonDeserializer<LocalDateTime> {

	/*
	 * public JsonElement serialize(LocalDateTime date, Type typeOfSrc,
	 * JsonSerializationContext context) { return new
	 * JsonPrimitive(date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)); //
	 * "yyyy-mm-dd" }
	 */

	public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
		return LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}
}
