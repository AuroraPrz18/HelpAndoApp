package com.codepath.aurora.helpandoapp.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.codepath.aurora.helpandoapp.R;
import com.codepath.aurora.helpandoapp.databinding.DialogContactInPostBinding;
import com.codepath.aurora.helpandoapp.models.Contact;
import com.codepath.aurora.helpandoapp.models.User;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

public class ContactDialogInPost extends DialogFragment {
    private DialogContactInPostBinding _binding;

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_contact_in_post, null);
        _binding = DialogContactInPostBinding.bind(view);

        Bundle dataSent = getArguments();
        if(dataSent.containsKey("Contact")){
            // Populate the Dialog with that information
            Contact contact = (Contact) Parcels.unwrap(dataSent.getParcelable("Contact"));
            if (contact != null) {
                _binding.tvNameContact.setText(Html.fromHtml("<b>" + getResources().getString(R.string.etNameContact_hint) + "</b> "
                        + contact.getName()));
                _binding.tvPhoneContact.setText(Html.fromHtml("<b>" + getResources().getString(R.string.etTelContact_hint) + "</b> "
                        + contact.getTelephone()));
                _binding.tvExtraInfoContact.setText(Html.fromHtml("<b>" + getResources().getString(R.string.etExtraInfoContact_hint) + "</b> "
                        + contact.getExtraInfo()));
            }
        }else if(dataSent.containsKey("Volunteer")){
            // Populate the Dialog with that information of the volunteer
            ParseUser volunteer = (ParseUser) Parcels.unwrap(dataSent.getParcelable("Volunteer"));
            if (volunteer != null) {
                _binding.tvNameContact.setText(Html.fromHtml("<b>" + getResources().getString(R.string.etName_hint) + "</b> "
                        + volunteer.getString(User.KEY_NAME)));
                String telephone = getResources().getString(R.string.not_provided);
                if(volunteer.getString(User.KEY_PHONE)!=null){
                    telephone = volunteer.getString(User.KEY_PHONE);
                }
                _binding.tvPhoneContact.setText(Html.fromHtml("<b>" + getResources().getString(R.string.etTelContact_hint) + "</b> "
                        + telephone));
                String email = getResources().getString(R.string.not_provided);
                if(volunteer.getString(User.KEY_EMAIL)!=null){
                    email = volunteer.getString(User.KEY_EMAIL);
                }
                _binding.tvExtraInfoContact.setText(Html.fromHtml("<b>" + getResources().getString(R.string.email) + "</b> "
                        + email));
            }
        }


        // Creates a builder for an alert dialog that uses the default alert dialog theme.
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setTitle(getActivity().getResources().getString(R.string.dialog_contact_in_post_title))
                .setNeutralButton(getActivity().getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                });
        return builder.create();
    }
}

