package com.stefansator.mensaplan;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;

import androidx.fragment.app.Fragment;

public class AccountFragment extends Fragment {
    private MaterialButton logoutButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        logoutButton = (MaterialButton) view.findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(logoutButtonClickListener);
        return view;
    }

    //Actions
    // Action which gets triggered when logout button is pressed
    private View.OnClickListener logoutButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            loggingOut();
        }
    };

    // Private Functions
    // Logs out the user and ends current Session
    private void loggingOut() {
        UserSession.endSession();
        // Switch back to Start Screen of App
        Intent intent = new Intent(getActivity().getApplicationContext(), StartActivity.class);
        startActivity(intent);
    }
}
