package org.jgtdsl.dto;

import com.google.common.base.MoreObjects;

public class AutoCompleteObject {

	private String value;
	private String text;

	public AutoCompleteObject(String value, String text) {
		this.value = value;
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String toString() {
		return MoreObjects.toStringHelper(this).add("text", this.text).add(
				"value", this.value).toString();
	}
}
