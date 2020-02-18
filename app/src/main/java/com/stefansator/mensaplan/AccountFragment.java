package com.stefansator.mensaplan;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;

import androidx.fragment.app.Fragment;

/**
 * Fragment for implementing the Logout Process within the app.
 * @author stefansator
 * @version 1.0
 */
public class AccountFragment extends Fragment {
    /**
     * Button Component for logging out the user
     */
    private MaterialButton logoutButton;

    /**
     * This function is called, when the AccountFragment is created. It instantiates the User
     * Interface and sets the Click Listener for the Logout Button.
     * @param inflater inflater for inflating views in the fragment
     * @param container parent view to which the fragment should be attached to
     * @param savedInstanceState If non-null, fragment is being reconstructed from previous state
     * @return View This returns the created Layout as a View Object
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        logoutButton = (MaterialButton) view.findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(logoutButtonClickListener);
        return view;
    }

    /**
     * Action Listener which gets triggered when Logout Button is clicked
     */
    private View.OnClickListener logoutButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            loggingOut();
        }
    };

    /**
     * Logs out the user and ends current session
     */
    private void loggingOut() {
        UserSession.endSession();
        // Switch back to Start Screen of App
        Intent intent = new Intent(getActivity().getApplicationContext(), StartActivity.class);
        startActivity(intent);
    }
}
