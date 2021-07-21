package com.codepath.aurora.helpandoapp.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.codepath.aurora.helpandoapp.R;
import com.codepath.aurora.helpandoapp.databinding.DialogContactBinding;
import com.codepath.aurora.helpandoapp.models.Contact;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

public class ContactDialog extends DialogFragment {
    private ContactDialogListener _listener;
    private DialogContactBinding _binding;
    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_contact, null);
        _binding = DialogContactBinding.bind(view);

        // If we already add a Contact Info Card -> Populate the Dialog with that information
        Contact contact = (Contact) Parcels.unwrap(getArguments().getParcelable("Contact"));
        if(contact!=null){
            _binding.etName.setText(contact.getName());
            _binding.etTelephone.setText(contact.getTelephone());
            _binding.etExtraInfo.setText(contact.getExtraInfo());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setTitle(getActivity().getResources().getString(R.string.dialog_contact_title))
                .setNegativeButton(getActivity().getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // We don't have to do anything
                    }
                })
                .setPositiveButton(getActivity().getResources().getString(R.string.dialog_use_contact_info), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Contact contact = new Contact();
                        contact.setName(_binding.etName.getText().toString());
                        contact.setTelephone(_binding.etTelephone.getText().toString());
                        contact.setExtraInfo(_binding.etExtraInfo.getText().toString());
                        _listener.saveInformation(contact);
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            _listener = (ContactDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString()
                    + " must implement NoticeDialogListener");
        }
    }

    public interface ContactDialogListener{
        void saveInformation(Contact contact);
    }

}

