package com.codepath.aurora.helpandoapp.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.aurora.helpandoapp.GlideApp;
import com.codepath.aurora.helpandoapp.R;
import com.codepath.aurora.helpandoapp.databinding.ItemOrganizationBinding;
import com.codepath.aurora.helpandoapp.models.Organization;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OrganizationAdapter extends RecyclerView.Adapter<OrganizationAdapter.ViewHolder> {

    Context _context;
    List<Organization> _orgs;

    public OrganizationAdapter(Context _context, List<Organization> _orgs) {
        this._context = _context;
        this._orgs = _orgs;
    }

    /**
     * Method called whenever the RecyclerView needs to create a new holder
     */
    @NonNull
    @NotNull
    @Override
    public OrganizationAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        // Create and inflate a new view for an item
        View view = LayoutInflater.from(_context).inflate(R.layout.item_organization, parent, false);
        return new OrganizationAdapter.ViewHolder(view);
    }

    /**
     * Method that associates a ViewHolder with data.
     */
    @Override
    public void onBindViewHolder(@NonNull @NotNull OrganizationAdapter.ViewHolder holder, int position) {
        Organization org = _orgs.get(position);
        holder.bind(org);
    }

    /**
     * Method to get the size of the data
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return _orgs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemOrganizationBinding _binding;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            _binding = ItemOrganizationBinding.bind(itemView);

        }

        /**
         * If the Mission has been cut to display it in the item, it will show the complete Mission
         * If the Mission has more then 200 characters it will only show the first 200.
         */
        private void showOrHideMission(Organization org) {
            String missionObj = org.getMission();
            String missionTV = _binding.tvMission.getText().toString();
            String end = "";
            //  if(missionTV.length()>3){
            //     end = missionTV.substring(missionTV.length() - 3, missionObj.length());
            //  }
            //  if (end.equals("...")) { // If the Mission has been cut before
            _binding.tvMission.setText(missionObj); // Shows the complete Mission
            //  } else {
            //      if (missionObj.length() > 200) { // If the Mission has not been cut before and it has more than 200 characters
            //          missionObj = missionObj.substring(0, 200) + "..."; // Cuts the Mission to don't show it complete
            //      }
            //     _binding.tvMission.setText(missionObj); // Displays it in the TextView
            // }
        }

        /**
         * Method to custom the data inside each item view and populate all their views
         *
         * @param org
         */
        public void bind(Organization org) {
            _binding.tvMission.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!org.getMission().isEmpty()) {
                        showOrHideMission(org);
                    } else {
                        _binding.tvMission.setText("");
                    }
                }
            });
            _binding.tvName.setText(org.getName());
            if (!org.getMission().isEmpty()) {
                showOrHideMission(org);
            } else {
                _binding.tvMission.setText("");
            }
            String address = String.format("<b>%s</b><br>%s<br>%s", _context.getResources().getString(R.string.address), org.getAddressLine1(), org.getAddressLine2());
            _binding.tvAddress.setText(Html.fromHtml(address));
            _binding.tvLocation.setText(org.getCity() + ", " + org.getState() + ", " + org.getCountry());
            _binding.tvUrl.setText(org.getUrl());
            String urlLogo = org.getLogoUrl();
            // Loads the Logo with Glide into ivLogo
            GlideApp.with(_context)
                    .load(urlLogo)
                    .fitCenter()
                    .error(_context.getDrawable(R.drawable.ic_business))
                    .into(_binding.ivLogo);
        }
    }
}

