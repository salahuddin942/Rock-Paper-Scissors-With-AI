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

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.catrobat.catroid.R
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.PixelFormat
import android.os.Handler
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.huawei.hms.mlsdk.common.LensEngine
import com.huawei.hms.mlsdk.handkeypoint.MLHandKeypointAnalyzer
import com.huawei.hms.mlsdk.handkeypoint.MLHandKeypointAnalyzerFactory
import com.huawei.hms.mlsdk.handkeypoint.MLHandKeypointAnalyzerSetting
import kotlinx.android.synthetic.main.activity_rock_paper_game_main_ai.frameLayout

class RockPaperGameMainActivityAI : AppCompatActivity() {
    companion object {
        private const val TAG = "ML_RockPaperGameMainActivityAI"
        private const val CAMERA_PERMISSION_REQUEST_CODE = 3
    }
    private var mHandKeyPointTransactor = HandKeyPointTransactor()
    private lateinit var mSurfaceHolderCamera: SurfaceHolder
    private lateinit var mSurfaceHolderOverlay: SurfaceHolder

    private lateinit var mLensEngine: LensEngine

    /**
     *  Calls the hand keypoint API to detect MLHandKeypoints.
     */
    private lateinit var mAnalyzer: MLHandKeypointAnalyzer

    private lateinit var mContext: Context
    private lateinit var mRootLayout: ViewGroup

    var handler: Handler = Handler()
    var runnable: Runnable? = null
    var delay = 3000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rock_paper_game_main_ai)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
        else
            init(this, frameLayout)

    }

    override fun onResume() {

        handler.postDelayed(Runnable {
            handler.postDelayed(runnable!!, delay.toLong())

            Toast.makeText(applicationContext, "Trying to Scan Your Hand !! Please wait 3 second for the result", LENGTH_SHORT).show()
            if(mHandKeyPointTransactor.rr=="5" ||  mHandKeyPointTransactor.rr=="2" ||  mHandKeyPointTransactor.rr=="0"){
                val intent = Intent(this@RockPaperGameMainActivityAI,RockPaperGameWin::class.java)
                intent.putExtra("value",mHandKeyPointTransactor.rr)
                startActivity(intent)
            }
            else{
                Toast.makeText(applicationContext, "Sorry We can detect your Hand! Please Wait 3 seconds", LENGTH_SHORT).show()
            }
        }.also { runnable = it }, delay.toLong())
        super.onResume()
    }
    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(runnable!!)
    }
    fun init(context: Context, rootLayout: ViewGroup) {
        mContext = context
        mRootLayout = rootLayout
        addSurfaceViews()

    }
    private fun addSurfaceViews() {

        val surfaceViewCamera = SurfaceView(mContext).also {
            it.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            mSurfaceHolderCamera = it.holder

        }

        val surfaceViewOverlay = SurfaceView(mContext).also {
            it.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            mSurfaceHolderOverlay = it.holder
            mSurfaceHolderOverlay.setFormat(PixelFormat.TRANSPARENT)
            mHandKeyPointTransactor.setOverlay(mSurfaceHolderOverlay)
        }

        mRootLayout.addView(surfaceViewCamera)
        mRootLayout.addView(surfaceViewOverlay)

        mSurfaceHolderCamera.addCallback(surfaceHolderCallback)
    }
    private val surfaceHolderCallback = object : SurfaceHolder.Callback {

        override fun surfaceCreated(holder: SurfaceHolder) {
            createAnalyzer()

        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            prepareLensEngine(width, height)
            mLensEngine.run(holder)

        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            mLensEngine.release()
        }

    }
    private fun createAnalyzer() {
        val settings = MLHandKeypointAnalyzerSetting.Factory()
            .setSceneType(MLHandKeypointAnalyzerSetting.TYPE_ALL)
            .setMaxHandResults(2)
            .create()

        mAnalyzer = MLHandKeypointAnalyzerFactory.getInstance().getHandKeypointAnalyzer(settings)
        mAnalyzer.setTransactor(mHandKeyPointTransactor)

    }
    private fun prepareLensEngine(width: Int, height: Int) {

        val dimen1: Int
        val dimen2: Int

        if (mContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            dimen1 = width
            dimen2 = height
        } else {
            dimen1 = height
            dimen2 = width
        }

        mLensEngine = LensEngine.Creator(mContext, mAnalyzer)
            .setLensType(LensEngine.BACK_LENS)
            .applyDisplayDimension(dimen1, dimen2)
            .applyFps(5F)
            .enableAutomaticFocus(true)
            .create()
    }

    fun stopAnalyzer() {
        mAnalyzer.stop()
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            init(this, frameLayout)
        }
    }

    override fun onDestroy() {
        stopAnalyzer()
        super.onDestroy()
    }
}