package org.jgtdsl.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jgtdsl.dto.AutoCompleteObject;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

public class CollectionFilterUsingGuava {

	public static void main(String[] args) {

		List<AutoCompleteObject> list = new ArrayList<AutoCompleteObject>();
		list.add(new AutoCompleteObject("1", "Jhon Wick"));
		list.add(new AutoCompleteObject("2", "Equalizer"));

		Collection<AutoCompleteObject> filter_list = Collections2.filter(list,
				new Predicate<AutoCompleteObject>() {

					public boolean apply(AutoCompleteObject input) {
						return input.getText().contains("Jhon");
					}
				});
		//System.out.println(filter_list);

	}

}
