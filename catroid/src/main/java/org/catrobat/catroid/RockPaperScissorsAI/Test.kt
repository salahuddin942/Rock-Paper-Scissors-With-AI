package org.catrobat.catroid.RockPaperScissorsAI


import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction

import org.catrobat.catroid.stage.StageActivity

class Test :  TemporalAction() {
    var active = false


    override fun update(percent: Float) {
        if (active) {
           StageActivity.getActiveCameraManager()?.startPreview()
        } else {
            StageActivity.getActiveCameraManager()?.stopPreview()
        }
    }
}

