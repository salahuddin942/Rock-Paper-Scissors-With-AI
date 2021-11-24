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

package org.catrobat.catroid.RockPaperScissorsAI;

import android.os.Bundle;
import org.catrobat.catroid.R;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Random;
public class RockPaperGameWin extends AppCompatActivity {

	HandKeyPointTransactor hh=new HandKeyPointTransactor();
	ImageView imagecomputer,imageplayer;
	Drawable drawablepaper,drawablerock, drawablesessor;
	TextView player,computer,win,pnumber,cnumber;

	private String plPick;
	private String cpuPick;
	private static int pScore = 0;
	private static int cScore = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rock_paper_game_win);

		player = (TextView)findViewById(R.id.textView);
		computer = (TextView)findViewById(R.id.textView1);
		win = (TextView)findViewById(R.id.textView2);
		pnumber = (TextView)findViewById(R.id.textView3);
		cnumber = (TextView)findViewById(R.id.textView4);
		imageplayer=(ImageView) findViewById(R.id.imageView1);
		imagecomputer=(ImageView) findViewById(R.id.imageView2);

		drawablepaper=getResources().getDrawable(R.drawable.paper);
		drawablerock=getResources().getDrawable(R.drawable.rock);
		drawablesessor=getResources().getDrawable(R.drawable.scissors);
		cnumber.setText("Computer Score: " + cScore);
		pnumber.setText("Player Score: " + pScore);
		// Button bplayer=(Button)findViewById(R.id.button);
		//  Button bcomputer=(Button)findViewById(R.id.button2);
		player.setText("Player Choice");
		computer.setText("Computer Choice");
		Intent intent = getIntent();

		String fingerdata = intent.getStringExtra("value");

		switch (fingerdata){
			case "0":
				plPick = "rock";
				imageplayer.setImageDrawable(drawablerock);
				break;

			case "2":
				plPick = "scissor";
				imageplayer.setImageDrawable(drawablesessor);
				break;

			case "5":
				plPick = "paper";
				imageplayer.setImageDrawable(drawablepaper);
				break;
		}

		Random rnd = new Random();
		int hand = rnd.nextInt(3)+1;
		switch (hand){
			case 1:
				cpuPick = "rock";
				imagecomputer.setImageDrawable(drawablerock);
				break;

			case 2:
				cpuPick = "scissor";
				imagecomputer.setImageDrawable(drawablesessor);
				break;

			case 3:
				cpuPick = "paper";
				imagecomputer.setImageDrawable(drawablepaper);
				break;
		}
		checkResult(cpuPick, plPick);
	}
	public void checkResult(String cpuHand, String plHand){

		if (cpuHand.equals(plHand)){
			win.setText("Draw");
		}
		else if (plHand.equals("rock") && cpuHand.equals("scissor") || plHand.equals("paper") &&
				cpuHand.equals("rock") || plHand.equals("scissor") && cpuHand.equals("paper")){
			win.setText("YOU WON!!");
			pScore++;
			pnumber.setText("Player Score: " + pScore);
		}
		else{
			win.setText("Computer Won!!");
			cScore++;
			cnumber.setText("Computer Score: " + cScore);
		}

	}
	@Override
	protected void onDestroy() {

		super.onDestroy();
	}

}