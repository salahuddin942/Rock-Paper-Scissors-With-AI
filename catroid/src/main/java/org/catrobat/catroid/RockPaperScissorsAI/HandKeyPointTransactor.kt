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

package org.catrobat.catroid.RockPaperScissorsAI

import android.util.Log
import android.view.SurfaceHolder
import androidx.core.util.keyIterator
import androidx.core.util.valueIterator
import com.huawei.hms.mlsdk.common.MLAnalyzer
import com.huawei.hms.mlsdk.handkeypoint.MLHandKeypoints


class HandKeyPointTransactor(surfaceHolder: SurfaceHolder? = null): MLAnalyzer.MLTransactor<MLHandKeypoints> {
    public lateinit var  rr: String

    companion object {
        private const val TAG = "ML_HandKeyPntTransactor"
    }

    /**
     *  SurfaceHolder above camera preview's surfaceHolder. Used to draw paintings on.
     */
    private var mOverlay = surfaceHolder

    fun setOverlay(surfaceHolder: SurfaceHolder) {
        mOverlay = surfaceHolder
    }

    override fun transactResult(result: MLAnalyzer.Result<MLHandKeypoints>?) {

        if (result == null)
            return

        val numberString: String = analyzeHandsAndGetNumber(result)
        rr=numberString


    }
    /**
     *  Creates hands and checks if fingers are up or not. Counts fingers that are up and returns as
     *  String.
     */
    private fun analyzeHandsAndGetNumber(result: MLAnalyzer.Result<MLHandKeypoints>): String {
        val hands = ArrayList<Hand>()
        var number = 10

        for (key in result.analyseList.keyIterator()) {
            hands.add(Hand())

            for (value in result.analyseList.valueIterator()) {
                number = hands.last().createHand(value.handKeypoints).getNumber()
            }
        }
        return number.toString()

    }

    override fun destroy() {
        Log.d(TAG, "destroy()")
    }
}