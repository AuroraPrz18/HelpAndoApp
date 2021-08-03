package com.codepath.aurora.helpandoapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.aurora.helpandoapp.GlideApp;
import com.codepath.aurora.helpandoapp.R;
import com.codepath.aurora.helpandoapp.activities.DetailsOrgActivity;
import com.codepath.aurora.helpandoapp.databinding.ItemOrganizationBinding;
import com.codepath.aurora.helpandoapp.models.Organization;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

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
        private void hideMission(Organization org) {
            String missionObj = org.getMission();
            if (missionObj.length() > 200) { // If the Mission has not been cut before and it has more than 200 characters
                missionObj = missionObj.substring(0, 200) + "..."; // Cuts the Mission to don't show it complete
            }
            _binding.tvMission.setText(missionObj); // Displays it in the TextView
        }

        /**
         * Method to custom the data inside each item view and populate all their views
         *
         * @param org
         */
        public void bind(Organization org) {
            _binding.cvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDetailsActivity(org);
                    newClickInOrgWithId(org.getId());
                }
            });
            _binding.tvName.setText(org.getName());
            String themes = "<b>Themes: </b>";
            List<String> themesL = org.getThemes();
            for (int i = 0; i < themesL.size(); i++) {
                if (i > 0) themes += ", ";
                themes += themesL.get(i);
            }
            _binding.tvThemes.setText(Html.fromHtml(themes));
            if (!org.getMission().isEmpty()) {
                hideMission(org);
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

        /**
         * Start a new Activity that contain more information about an specific organization
         *
         * @param org
         */
        private void openDetailsActivity(Organization org) {
            Intent intent = new Intent(_context, DetailsOrgActivity.class);
            intent.putExtra("Org", Parcels.wrap(org));
            _context.startActivity(intent);
        }
    }

    /**
     * Save the click in the DB, it will help to now which Organization is more popular
     * @param id
     */
    private void newClickInOrgWithId(int id) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("GeneralClicksOrgs");
        query.whereEqualTo("idOrg", id+"");
        // Check if this organization already exists
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if(e==null){ //Row already exists -> Update it
                    object.put("clicks", (Integer)object.getNumber("clicks") + (Integer)1);
                    object.saveInBackground();
                }else{
                    if(e.getCode() == ParseException.OBJECT_NOT_FOUND){ // Row not exists -> create it
                        ParseObject newRowClicks = new ParseObject("GeneralClicksOrgs");
                        newRowClicks.put("clicks", 1);
                        newRowClicks.put("idOrg", id+"");
                        newRowClicks.saveInBackground();
                    }
                }
            }
        });
    }
}

