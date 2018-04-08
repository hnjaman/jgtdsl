package org.jgtdsl.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jgtdsl.dto.AutoCompleteObject;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

public class CollectionFilterUsingGuava {

	public static void main(String[] args) {

//		List<AutoCompleteObject> list = new ArrayList<AutoCompleteObject>();
//		list.add(new AutoCompleteObject("1", "Jhon Wick"));
//		list.add(new AutoCompleteObject("2", "Equalizer"));
//
//		Collection<AutoCompleteObject> filter_list = Collections2.filter(list,
//				new Predicate<AutoCompleteObject>() {
//
//					public boolean apply(AutoCompleteObject input) {
//						return input.getText().contains("Jhon");
//					}
//				});
		//System.out.println(filter_list);
		
		
		SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
		
		String fromDate="28-02-2018";
		String toDate="13-03-2018";
		long diffDays =0l;
		try {
		    Date date1 = myFormat.parse("28-02-2018");
		    Date date2 = myFormat.parse("13-03-2018");
		    long diff = date2.getTime() - date1.getTime();
		    diffDays= TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
		} catch (ParseException e) {
		    e.printStackTrace();
		}
		
		System.out.println(diffDays);

	}

}
