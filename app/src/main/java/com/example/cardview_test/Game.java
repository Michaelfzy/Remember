package com.example.cardview_test;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.sendcloud.jigsawpuzzle.main.WelcomePage;



public class Game extends Fragment {
    private View tView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        tView = inflater.inflate(R.layout.fragment_game, container, false);
        ImageView Gameimage = tView.findViewById(R.id.imageView_playgame);
        TextView Gametext = tView.findViewById(R.id.textView_playgame);

        Gameimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WelcomePage.class);
                startActivity(intent);
            }
        });
        Gametext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WelcomePage.class);
                startActivity(intent);
            }
        });
        return tView;
    }

}
