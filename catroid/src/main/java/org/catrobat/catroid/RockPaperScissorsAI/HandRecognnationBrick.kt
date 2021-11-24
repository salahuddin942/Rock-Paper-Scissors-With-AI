package org.catrobat.catroid.RockPaperScissorsAI

import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import org.catrobat.catroid.R
import org.catrobat.catroid.content.AdapterViewOnItemSelectedListenerImpl
import org.catrobat.catroid.content.Sprite
import org.catrobat.catroid.content.actions.ScriptSequenceAction
import org.catrobat.catroid.content.bricks.Brick
import org.catrobat.catroid.content.bricks.BrickBaseType

class HandRecognnationBrick (private var spinnerSelectionON: Boolean = true) : BrickBaseType() {

    override fun getView(context: Context): View {
        super.getView(context)
        assignVideoSpinnerProperties()
        return view
    }

    private fun assignVideoSpinnerProperties() {
        view.findViewById<Spinner>(R.id.brick_video_spinner).apply {
            adapter = createArrayAdapter(context)
            onItemSelectedListener = AdapterViewOnItemSelectedListenerImpl { position ->
                spinnerSelectionON = position == 1
            }
            setSelection(if (spinnerSelectionON) 1 else 0)
        }
    }

    private fun createArrayAdapter(context: Context): ArrayAdapter<String?> {
        val spinnerValues = arrayOf(
            context.getString(R.string.video_brick_camera_off),
            context.getString(R.string.video_brick_camera_on)
        )
        val spinnerAdapter =
            ArrayAdapter(context, android.R.layout.simple_spinner_item, spinnerValues)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        return spinnerAdapter
    }

    override fun getViewResource(): Int = R.layout.brick_hand_recognation

    override fun addRequiredResources(requiredResourcesSet: Brick.ResourcesSet) {
        requiredResourcesSet.add(VIDEO)
    }

    override fun addActionToSequence(sprite: Sprite, sequence: ScriptSequenceAction) {
        sequence.addAction(sprite.actionFactory.createHandTrackingAction(spinnerSelectionON))
    }
}
