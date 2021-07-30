package com.codepath.aurora.helpandoapp;

import android.util.Log;

import com.codepath.aurora.helpandoapp.models.Organization;
import com.codepath.aurora.helpandoapp.models.User;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class Filter {
    private List<Organization> _orgs;
    private String _city;
    private String _country;
    private Queue<Match> _priorityQueue;

    public Filter(List<Organization> orgs) {
        _orgs = orgs;
        _city = User.userCity;
        _country = User.userCountry;
        _priorityQueue = new PriorityQueue<>(2, new MatchComparator());
        Log.d("filter", orgs.toString());
    }

    /**
     * Execute the filter
     */
    public List<Organization> execute(){
        findMatch(0);
        Log.d("filter", _orgs.toString());
        return _orgs;
    }

    private void findMatch(int position) {
        if(position == _orgs.size()) return;
        _priorityQueue.add(new Match(_orgs.get(position), getPunctuation(position)));
        findMatch(position +1);
        _orgs.set(position, _priorityQueue.poll().getOrganization());
    }

    private long getPunctuation(long position) {
        return 0;
    }


    private class Match {
        private Organization _organization;
        private long _punctuation;

        public Match(Organization organization, long punctuation) {
            _organization = organization;
            _punctuation = punctuation;
        }

        public Organization getOrganization() {
            return _organization;
        }

        public void setOrganization(Organization organization) {
            this._organization = organization;
        }

        public long getPunctuation() {
            return _punctuation;
        }

        public void setPunctuation(long punctuation) {
            this._punctuation = punctuation;
        }


    }
    private static class MatchComparator implements Comparator<Match>{

        @Override
        public int compare(Match match1, Match match2) {
            // Compare first by points
            if(match1.getPunctuation() > match2.getPunctuation()){
                return 1;
            }else if(match1.getPunctuation() < match2.getPunctuation()){
                return -1;
            }
            // If has the same punctuation
            int result = match1.getOrganization().getName().compareTo(match2.getOrganization().getName());
            if(result < 0){
                return 1;
            }else {
                return -1;
            }
        }
    }
}
