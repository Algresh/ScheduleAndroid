package com.example.alex.scheduleandroid.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.alex.scheduleandroid.R;

public class SendDialogFragment extends DialogFragment implements View.OnClickListener {

    EditText editText;
    private MyDialogListener mListener;

    public interface MyDialogListener {
        public void onClickSendMessage(String message);
    }

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
        mListener.onClickSendMessage(msg);
        dismiss();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (MyDialogListener) activity;
        } catch(ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
}
