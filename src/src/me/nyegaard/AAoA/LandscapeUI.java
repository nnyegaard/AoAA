package me.nyegaard.AAoA;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LandscapeUI extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.landscape, viewGroup, false);
    }
}