/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.test.hwui;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewAnimationUtils;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;

public class RevealActivity extends Activity implements OnClickListener {

    private static final int DURATION = 800;

    private boolean mShouldBlock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        ProgressBar spinner = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
        layout.addView(spinner, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        View revealView = new MyView(this);
        layout.addView(revealView,
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        setContentView(layout);

        revealView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Animator animator = ViewAnimationUtils.createCircularReveal(view,
                view.getWidth() / 2, view.getHeight() / 2,
                0, Math.max(view.getWidth(), view.getHeight()));
        animator.setDuration(DURATION);
        animator.setAllowRunningAsynchronously(true);
        animator.start();

        mShouldBlock = !mShouldBlock;
        if (mShouldBlock) {
            view.post(sBlockThread);
        }
    }

    private final static Runnable sBlockThread = new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(DURATION);
            } catch (InterruptedException e) {
            }
        }
    };

    static class MyView extends View {

        public MyView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawColor(Color.RED);
        }
    }
}
