/*
 *  Copyright (c) 2023 Ashish Yadav <mailtoashish693@gmail.com>
 *
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 3 of the License, or (at your option) any later
 *  version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY
 *  WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 *  PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with
 *  this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.uchi.resqsync.utils

import android.content.Context
import android.util.DisplayMetrics
import android.widget.Toast
import androidx.annotation.StringRes

@Suppress("unused")
object UIUtils {
    fun showThemedToast(context: Context?, text: String?, shortLength: Boolean) {
        Toast.makeText(context, text, if (shortLength) Toast.LENGTH_SHORT else Toast.LENGTH_LONG).show()
    }

    fun showThemedToast(context: Context?, text: CharSequence?, shortLength: Boolean) {
        showThemedToast(context, text.toString(), shortLength)
    }

    fun showThemedToast(context: Context?, @StringRes textResource: Int, shortLength: Boolean) {
        Toast.makeText(context, textResource, if (shortLength) Toast.LENGTH_SHORT else Toast.LENGTH_LONG).show()
    }

    fun getDensityAdjustedValue(context: Context, value: Float): Float {
        return context.resources.displayMetrics.density * value
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit.
     * @param context Context to get resources and device specific display metrics.
     * @return A float value to represent px value which is equivalent to the passed dp value.
     */
    fun convertDpToPixel(dp: Float, context: Context): Float {
        return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }
}