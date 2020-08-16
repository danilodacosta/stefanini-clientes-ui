package com.stefanini.cliente.converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter("localDateFacesConverter")
public class LocalDateConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String stringValue) {

		if (null == stringValue || stringValue.isEmpty()) {
			return null;
		}

		LocalDate localDate;

		try {
			localDate = LocalDate.parse(stringValue, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

		} catch (DateTimeParseException e) {

			throw new ConverterException("O ano deve conter 4 dígitos. Exemplo: 15/08/2020.");
		}

		return localDate;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object localDateValue) {

		String convert = "";
		
		
		if (null == localDateValue) {

			return "";
		}

		try {

			convert = ((LocalDate) localDateValue).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")); 

		}

		catch (Exception e) {
			
		}

		return convert;
	}
}