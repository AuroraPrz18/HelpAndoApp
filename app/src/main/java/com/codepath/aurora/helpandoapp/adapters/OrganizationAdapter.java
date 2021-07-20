package com.codepath.aurora.helpandoapp.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.aurora.helpandoapp.R;
import com.codepath.aurora.helpandoapp.databinding.ItemOrganizationBinding;
import com.codepath.aurora.helpandoapp.models.Organization;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OrganizationAdapter extends RecyclerView.Adapter<OrganizationAdapter.ViewHolder>{

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
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String mission = _binding.tvMission.getText().toString();
                    String end = mission.substring(mission.length()-3, mission.length());
                    if(mission.length()>3 && end.equals("...")){
                        _binding.tvMission.setText(_orgs.get(getAdapterPosition()).getMission());
                    }else{
                        if(mission.length()>200) {
                            mission = mission.substring(0, 200) + "...";
                        }
                        _binding.tvMission.setText(mission);
                    }
                }
            });
        }

        /**
         * Method to custom the data inside each item view
         *
         * @param org
         */

        public void bind(Organization org) {
            _binding.tvName.setText(org.getName());
            String mission = org.getMission();
            if(mission.length()>200) {
                mission = mission.substring(0, 200) + "...";
            }
            _binding.tvMission.setText(mission);
            String address = "<b>" + _context.getResources().getString(R.string.address) + "</b>"
                    + "<br>" + org.getAddressLine1() + "<br>" + org.getAddressLine2();
            _binding.tvAddress.setText(Html.fromHtml(address));
            _binding.tvLocation.setText(org.getCity() + ", " + org.getState() + ", " + org.getCountry());
            _binding.tvUrl.setText(org.getUrl());
            String urlLogo = org.getLogoUrl();
            /*if(!urlLogo.isEmpty()){
                //if(urlLogo.substring(0, 5)== "http:"){
                //    urlLogo = "https:" + urlLogo.substring(5);
                //}
                Log.d("DEBUG", urlLogo.substring(0, 5) + ""+ urlLogo);

                Glide.with(_context)
                        .load(urlLogo)
                        //.override(30, 30) // Resizing the image
                        //.error(_context.getDrawable(R.drawable.ic_business))
                        .into(_binding.ivLogo);
            }else{
                _binding.ivLogo.setImageDrawable(_context.getDrawable(R.drawable.ic_business));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    _binding.ivLogo.setBackgroundColor(_context.getColor(R.color.green_1));
                }
            }*/
        }
    }
}

