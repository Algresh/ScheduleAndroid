package com.example.alex.scheduleandroid.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.alex.scheduleandroid.Constants;
import com.example.alex.scheduleandroid.R;

public class SendDialogFragment extends DialogFragment implements View.OnClickListener {

    EditText editText;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.send_dialog_fragment, null);
        Button button = (Button) view.findViewById(R.id.buttonSendMessage);
        editText = (EditText) view.findViewById(R.id.editTextMessage);
        button.setOnClickListener(this);
        builder.setView(view);

        return builder.create();
    }


    @Override
    public void onClick(View v) {
        String msg = editText.getText().toString();
        Log.d(Constants.MY_TAG, msg);
        dismiss();

    }
}
