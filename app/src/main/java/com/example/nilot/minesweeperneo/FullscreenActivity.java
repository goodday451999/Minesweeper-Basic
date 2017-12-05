package com.example.nilot.minesweeperneo;

import android.annotation.SuppressLint;
//import android.content.Intent;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static android.R.id.list;
import static android.R.id.summary;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */

public class FullscreenActivity extends AppCompatActivity {
    final int rowNo = 9;
    final int colNo = 4;
    final String[] singleBoard = new String[rowNo * colNo];
    final Context context = this;
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.gridview);


        // Set up the user interaction to manually show or hide the system UI.
        /*mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });*/

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        //findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
        createBombBoard();
        gridview();

    }
    public void createBombBoard() {
        int[][] board = new int[rowNo][colNo];

        Random rand = new Random();
        for (int i = 0; i <rowNo; i++) {
            for (int j = 0; j < colNo; j++) {
                board[i][j] = rand.nextInt(200) / 100;
                singleBoard[(i * colNo) + j] ="" +  board[i][j];
            }
        }

        for (int i = 0; i < rowNo; i++) {
            for (int j = 0; j < colNo; j++) {
                if(board[i][j] == 0) {
                    int pos1 = (i - 1 < 0 || j - 1 < 0) ? 0 : board[i - 1][j - 1];
                    int pos2 = (i - 1 < 0) ? 0 : board[i - 1][j];
                    int pos3 = (i - 1 < 0 || j + 1 >= colNo) ? 0 : board[i - 1][j + 1];
                    int pos4 = (j - 1 < 0) ? 0 : board[i][j - 1];
                    int pos5 = (j + 1 >= colNo) ? 0 : board[i][j + 1];
                    int pos6 = (i + 1 >= rowNo || j - 1 < 0) ? 0 : board[i + 1][j - 1];
                    int pos7 = (i + 1 >= rowNo) ? 0 : board[i + 1][j];
                    int pos8 = (i + 1 >= rowNo || j + 1 >= colNo) ? 0 : board[i + 1][j + 1];
                    int sum = pos1 + pos2 + pos3 + pos4 + pos5 + pos6 + pos7 + pos8;
                    singleBoard[(i * colNo) + j] = "" + sum;
                }
            }
        }
    }
    protected String[] resetBoard(){
        String[] array = new String[rowNo * colNo];
        for (int i = 0; i < (rowNo * colNo); i++) {
            array[i] = ".";
        }
        createBombBoard();
        return array;
    }
    protected void gridview() {
        GridView gridview = (GridView) findViewById(R.id.gridview);
        /*String[] array = new String[36];
        for (int i = 0; i < 36; i++)
            array[i] = ".";
            */
        String[] arr = resetBoard();
        //initializing an ArrayList from arra
        final List<String> list = new ArrayList<String>(Arrays.asList(arr));
        final MinesweeperArrAdapter <String> gridAdapter = new MinesweeperArrAdapter<String>(this, android.R.layout.simple_list_item_1, list,this);

        //data bind GridView with ArrayAdapter
        gridview.setAdapter(gridAdapter);
        setClickListener(list,gridAdapter);
    }

    protected void setClickListener(final List<String> list,final MinesweeperArrAdapter<String> gridAdapter){

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,View v, int position, long id){
                if(!singleBoard[position].equals("1")) {
                    list.set(position, singleBoard[position]);
                }
                else{

                    list.clear();
                    list.addAll(Arrays.asList(singleBoard));
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    // set title
                    alertDialogBuilder.setTitle("Neo's Minesweeper");

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("Game Over!")
                            .setCancelable(false)
                            .setPositiveButton("Replay",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // if this button is clicked, close
                                    // current activity
                                    list.clear();
                                    list.addAll(Arrays.asList(resetBoard()));
                                    gridAdapter.notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("Quit",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    //dialog.cancel();
                                    FullscreenActivity.this.finish();
                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                }

                gridAdapter.notifyDataSetChanged();
            }
        });
    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
