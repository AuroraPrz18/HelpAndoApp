package com.codepath.aurora.helpandoapp;

import com.codepath.aurora.helpandoapp.models.Organization;
import com.codepath.aurora.helpandoapp.models.User;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class Filter {
    private final int POINTS_CITY = 6000;
    private final int POINTS_COUNTRY = 2000;
    private final int POINTS_PROJECTS_1 = 3000;
    private final int POINTS_PROJECTS_2 = 2000;
    private final int POINTS_PROJECTS_3 = 1000;
    private final int POINTS_PROJECTS_4 = 500;

    private List<Organization> _orgs;
    private String _city;
    private String _country;
    private Queue<Match> _priorityQueue;
    private String [] _topThreeThemes;
    private Map<String, Long> _mpPopularityOrg;

    public Filter(List<Organization> orgs) {
        _orgs = orgs;
        _city = User.userCity;
        _country = User.userCountry;
        _priorityQueue = new PriorityQueue<>(2, new MatchComparator());
        _topThreeThemes = User.getTop3();
        _mpPopularityOrg = Organization.getGeneralPopularity();
    }

    /**
     * Execute the filter
     */
    public List<Organization> execute(){
        findMatch(0);
        return _orgs;
    }

    private void findMatch(int position) {
        if(position == _orgs.size()) return;
        _priorityQueue.add(new Match(_orgs.get(position), getPunctuation(position)));
        findMatch(position +1);
        _orgs.set(position, _priorityQueue.poll().getOrganization());
    }

    private long getPunctuation(int position) {
        long pointsEarned =
                evaluateCountry(position) +
                evaluateCity(position) +
                evaluateThemes(position) +
                evaluateGeneralPopularity(position);
        return pointsEarned;
    }

    /**
     * Evaluate each organization based on its general popularity. Each click is an extra point.
     * @param position
     * @return
     */
    private long evaluateGeneralPopularity(int position) {
        // Look for how many clicks this Organization has received
        String idOrg = _orgs.get(position).getId() + "";
        long points =0;
        if(_mpPopularityOrg.containsKey(idOrg)){
            points = _mpPopularityOrg.get(idOrg);
        }
        return points;
    }

    /**
     * Evaluate the topics that the Organization support, looking if they match with the favorite topics of the user.
     * @param position
     * @return
     */
    private long evaluateThemes(int position) {
        List<String> themesProjectsOrg = _orgs.get(position).getThemes();
        long points=0;
        // Check how many themes of the organization match with  the user
        for(int i=0; i<themesProjectsOrg.size(); i++){
            String themeOrgProj = themesProjectsOrg.get(i);
            if(_topThreeThemes[0].equalsIgnoreCase(themeOrgProj)){
                points += POINTS_PROJECTS_1;
            }else if(_topThreeThemes[1].equalsIgnoreCase(themeOrgProj)){
                points += POINTS_PROJECTS_2;
            }else if(_topThreeThemes[2].equalsIgnoreCase(themeOrgProj)){
                points += POINTS_PROJECTS_3;
            }
        }
        return points;
    }

    private int evaluateCountry(int position) {
        //todo: check in the list
        Organization org = _orgs.get(position);
        if(org.getCountry().compareToIgnoreCase(_country) == 0){ // If the organization and the user are in the same country
            return POINTS_COUNTRY;
        }
        return 0;
    }

    private int evaluateCity(int position) {
        Organization org = _orgs.get(position);
        if(org.getCity().compareToIgnoreCase(_city) == 0){ // If the organization and the user are in the same city
            return POINTS_CITY;
        }
        //todo: evaluate distances if I have enough time
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
