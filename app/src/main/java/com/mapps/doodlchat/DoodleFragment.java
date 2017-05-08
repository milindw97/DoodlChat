package com.mapps.doodlchat;


import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.util.UUID;

import static android.support.v4.content.res.ResourcesCompat.getDrawable;

public class DoodleFragment extends Fragment implements View.OnClickListener {

    private static DrawingView drawView;
    private ImageButton currPaint;
    private Button eraseBtn, saveBtn;

    public DoodleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_doodle, container, false);

        drawView = (DrawingView) view.findViewById(R.id.drawing);
        LinearLayout paintLayout = (LinearLayout) view.findViewById(R.id.paint_colors);
        currPaint = (ImageButton) paintLayout.getChildAt(0);
        currPaint.setImageDrawable(getDrawable(getResources(), R.drawable.paint_pressed, null));

        eraseBtn = (Button)view.findViewById(R.id.erase_btn);
        eraseBtn.setOnClickListener(this);
        saveBtn = (Button)view.findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(this);

        final ImageButton b1 = (ImageButton) view.findViewById(R.id.button1);
        b1.setOnClickListener(this);
        final ImageButton b2 = (ImageButton) view.findViewById(R.id.button2);
        b2.setOnClickListener(this);
        final ImageButton b3 = (ImageButton) view.findViewById(R.id.button3);
        b3.setOnClickListener(this);
        final ImageButton b4 = (ImageButton) view.findViewById(R.id.button4);
        b4.setOnClickListener(this);
        final ImageButton b5 = (ImageButton) view.findViewById(R.id.button5);
        b5.setOnClickListener(this);
        final ImageButton b6 = (ImageButton) view.findViewById(R.id.button6);
        b6.setOnClickListener(this);
        final ImageButton b7 = (ImageButton) view.findViewById(R.id.button7);
        b7.setOnClickListener(this);
        final ImageButton b8 = (ImageButton) view.findViewById(R.id.button8);
        b8.setOnClickListener(this);
        final ImageButton b9 = (ImageButton) view.findViewById(R.id.button9);
        b9.setOnClickListener(this);
        final ImageButton b10 = (ImageButton) view.findViewById(R.id.button10);
        b10.setOnClickListener(this);
        final ImageButton b11 = (ImageButton) view.findViewById(R.id.button11);
        b11.setOnClickListener(this);
        final ImageButton b12 = (ImageButton) view.findViewById(R.id.button12);
        b12.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.erase_btn){
            drawView.startNew();
        }
        else if(view.getId() == R.id.save_btn){
            drawView.setDrawingCacheEnabled(true);
            String imgSaved = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), drawView.getDrawingCache(),
                    UUID.randomUUID().toString()+".png", "drawing");
            if(imgSaved!=null){
                Toast savedToast = Toast.makeText(getActivity().getApplicationContext(),
                        "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
                savedToast.show();
            }
            else{
                Toast unsavedToast = Toast.makeText(getActivity().getApplicationContext(),
                        "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
                unsavedToast.show();
            }
            drawView.destroyDrawingCache();
        }
        else
        {
            if (view != currPaint) {
                ImageButton imgView = (ImageButton)view;
                String color = view.getTag().toString();
                drawView.setColor(color);
                imgView.setImageDrawable(getDrawable(getResources(),R.drawable.paint_pressed,null));
                currPaint.setImageDrawable(getDrawable(getResources(),R.drawable.paint,null));
                currPaint = (ImageButton) view;
            }
        }
    }
}
