package org.foomla.androidapp.utils;

import android.text.Editable;
import android.text.Html.TagHandler;

import org.xml.sax.XMLReader;

public class ExtendedTagHandler implements TagHandler {
	boolean first = true;

	@Override
	public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {

		if (tag.equals("li")) {
			char lastChar = 0;
			if (output.length() > 0)
				lastChar = output.charAt(output.length() - 1);
			if (first) {
				if (lastChar == '\n')
					output.append("\u2022  ");
				else
					output.append("\n\u2022  ");
				first = false;
			} else {
				first = true;
			}
		}
	}

}
