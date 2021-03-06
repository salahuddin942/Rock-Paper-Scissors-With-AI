/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2021 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.catrobat.catroid.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;

import org.catrobat.catroid.CatroidApplication;
import org.catrobat.catroid.R;
import org.catrobat.catroid.formulaeditor.Formula;

import static org.catrobat.catroid.utils.NumberFormats.toMetricUnitRepresentation;
import static org.catrobat.catroid.utils.NumberFormats.trimTrailingCharacters;

public final class ShowTextUtils {
	public static final int DEFAULT_TEXT_SIZE = 45;
	public static final float DEFAULT_X_OFFSET = 3.0f;

	public static final int ALIGNMENT_STYLE_LEFT = 0;
	public static final int ALIGNMENT_STYLE_CENTERED = 1;
	public static final int ALIGNMENT_STYLE_RIGHT = 2;

	private ShowTextUtils() {
	}

	public static int[] calculateColorRGBs(String color) {
		int[] rgb = new int[3];
		int colorValue = Integer.parseInt(color.substring(1), 16);
		rgb[0] = (colorValue & 0xFF0000) >> 16;
		rgb[1] = (colorValue & 0xFF00) >> 8;
		rgb[2] = (colorValue & 0xFF);
		return rgb;
	}

	public static float sanitizeTextSize(float textSize) {
		if (textSize > DEFAULT_TEXT_SIZE * 100.0f) {
			return DEFAULT_TEXT_SIZE * 25.0f;
		}
		if (textSize > 0 && textSize < DEFAULT_TEXT_SIZE * 0.05f) {
			return DEFAULT_TEXT_SIZE * 0.25f;
		}
		return textSize;
	}

	public static boolean isValidColorString(String color) {
		return (color != null && color.length() == 7 && color.matches("#[A-F0-9a-f]+"));
	}

	public static int calculateAlignmentValuesForText(Paint paint, int bitmapWidth, int alignment) {
		switch (alignment) {
			case ALIGNMENT_STYLE_LEFT:
				paint.setTextAlign(Paint.Align.LEFT);
				return 0;
			case ALIGNMENT_STYLE_RIGHT:
				paint.setTextAlign(Paint.Align.RIGHT);
				return bitmapWidth;
			default:
				paint.setTextAlign(Paint.Align.CENTER);
				return bitmapWidth / 2;
		}
	}

	private static String convertToEnglishDigits(String value) {
		return value
				// Eastern-Arabic ??
				.replace("??", "1").replace("??", "2").replace("??", "3").replace("??", "4").replace("??", "5")
				.replace("??", "6").replace("??", "7").replace("??", "8").replace("??", "9").replace("??", "0")
				// Farsi
				.replace("??", "1").replace("??", "2").replace("??", "3").replace("??", "4").replace("??", "5")
				.replace("??", "6").replace("??", "7").replace("??", "8").replace("??", "9").replace("??", "0")
				// Hindi
				.replace("???", "1").replace("???", "2").replace("???", "3").replace("???", "4").replace("???", "5")
				.replace("???", "6").replace("???", "7").replace("???", "8").replace("???", "9").replace("???", "0")
				// Assamese and Bengali
				.replace("???", "1").replace("???", "2").replace("???", "3").replace("???", "4").replace("???", "5")
				.replace("???", "6").replace("???", "7").replace("???", "8").replace("???", "9").replace("???", "0")
				// Tamil
				.replace("???", "1").replace("???", "0").replace("???", "2").replace("???", "3").replace("???", "4")
				.replace("???", "5").replace("???", "6").replace("???", "7").replace("???", "8").replace("???", "9")
				// Gujarati
				.replace("???", "1").replace("???", "2").replace("???", "3").replace("???", "4").replace("???", "5")
				.replace("???", "6").replace("???", "7").replace("???", "8").replace("???", "9").replace("???", "0");
	}

	public static boolean isNumberAndInteger(String variableValue) {
		double variableValueIsNumber = 0;

		if (variableValue.matches("-?\\d+(\\.\\d+)?")) {
			variableValueIsNumber = Double.parseDouble(convertToEnglishDigits(variableValue));
		} else {
			return false;
		}

		return ((int) variableValueIsNumber) - variableValueIsNumber == 0;
	}

	public static String getStringAsInteger(String variableValue) {
		return Integer.toString((int) Double.parseDouble(convertToEnglishDigits(variableValue)));
	}

	public static String convertColorToString(int color) {
		return String.format("#%02X%02X%02X", Color.red(color), Color.green(color), Color.blue(color));
	}

	public static String convertStringToMetricRepresentation(String value) {
		String result = value;
		try {
			result = toMetricUnitRepresentation(Integer.parseInt(value));
		} catch (NumberFormatException ignored) {
		}
		return result;
	}

	public static String convertObjectToString(Object object) {
		if (object instanceof Boolean) {
			return new AndroidStringProvider(CatroidApplication.getAppContext())
					.getTrueOrFalse((Boolean) object);
		} else {
			return convertStringToMetricRepresentation(trimTrailingCharacters(object.toString()));
		}
	}

	public static class AndroidStringProvider implements Formula.StringProvider {
		private final String trueString;
		private final String falseString;

		public AndroidStringProvider(Context context) {
			this.trueString = context.getString(R.string.formula_editor_true);
			this.falseString = context.getString(R.string.formula_editor_false);
		}

		@Override
		public String getTrueOrFalse(Boolean value) {
			if (value) {
				return trueString;
			} else {
				return falseString;
			}
		}
	}
}
