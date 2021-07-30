package com.codepath.aurora.helpandoapp;

import android.util.Xml;

import com.codepath.aurora.helpandoapp.models.Organization;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class OrganizationsXmlParser {

    /**
     * Initialize a XmlPullParser and call other methods to retrieve a List of organizations
     */
    public List<Organization> parseXml(InputStream orgFileXML) throws XmlPullParserException, IOException {
        try {
            // Instantiate a new XmlPullParser
            XmlPullParser parser = Xml.newPullParser();
            // Define the input that will be parsed
            parser.setInput(orgFileXML, null);
            // Get the next tag
            parser.nextTag();
            return readOrganizations(parser);
        } finally {
            // Close the file
            orgFileXML.close();
        }
    }

    /**
     * Find each 'Organization' tag to parse all the data inside this tag and build an Organization object
     *
     * @param parser
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    private List readOrganizations(XmlPullParser parser) throws XmlPullParserException, IOException {
        List organizations = new ArrayList();
        // Define which started tag is needed
        parser.require(XmlPullParser.START_TAG, null, "organizations");
        // While the next tag is different than the end tag
        while (parser.next() != XmlPullParser.END_TAG) {
            // If the tag found is a start tag
            if (parser.getEventType() == XmlPullParser.START_TAG) {
                // Get the name of the tag
                String name = parser.getName();
                // If it is an organization' tag, it means a new object
                if (name.equals("organization")) {
                    // Add this object in the organizations array, but before it, parse its content
                    organizations.add(readOrganization(parser));
                } else { // If it is a tag not relevant for us, skipTag
                    skipTag(parser);
                }
            }
        }
        return organizations;
    }

    /**
     * Parses the XML content of an Organization.
     */
    private Object readOrganization(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "organization");
        Organization org = new Organization();
        // While the next tag is different than the end tag
        while (parser.next() != XmlPullParser.END_TAG) {
            // If the type of the current event is not an start tag, it continues looking for it (ignores the code below this)
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            // Get the name of the tag
            String name = parser.getName();
            // Look for what type of attribute of the organization this tag is
            // when It decide, add it attribute to the organization object (org)
            if (name.equals(Organization.KEY_ID)) {
                org.setId((int) Integer.valueOf(readField(parser, Organization.KEY_ID, 1)));
            } else if (name.equals(Organization.KEY_ACT_PROJ)) {
                org.setActiveProjects((int) Integer.valueOf(readField(parser, Organization.KEY_ACT_PROJ, 1)));
            } else if (name.equals(Organization.KEY_TOTAL_PROJ)) {
                org.setTotalProjects((int) Integer.valueOf(readField(parser, Organization.KEY_TOTAL_PROJ, 1)));
            } else if (name.equals(Organization.KEY_ADDRESS1)) {
                org.setAddressLine1(readField(parser, Organization.KEY_ADDRESS1, 0));
            } else if (name.equals(Organization.KEY_ADDRESS2)) {
                org.setAddressLine2(readField(parser, Organization.KEY_ADDRESS2, 0));
            } else if (name.equals(Organization.KEY_CITY)) {
                org.setCity(readField(parser, Organization.KEY_CITY, 0));
            } else if (name.equals(Organization.KEY_COUNTRY)) {
                org.setCountry(readField(parser, Organization.KEY_COUNTRY, 0));
            } else if (name.equals(Organization.KEY_EIN)) {
                org.setEin(readField(parser, Organization.KEY_EIN, 0));
            } else if (name.equals(Organization.KEY_LOGO)) {
                org.setLogoUrl(readField(parser, Organization.KEY_LOGO, 0));
            } else if (name.equals(Organization.KEY_MISSION)) {
                org.setMission(readField(parser, Organization.KEY_MISSION, 0));
            } else if (name.equals(Organization.KEY_NAME)) {
                org.setName(readField(parser, Organization.KEY_NAME, 0));
            } else if (name.equals(Organization.KEY_POSTAL)) {
                org.setPostal(readField(parser, Organization.KEY_POSTAL, 0));
            } else if (name.equals(Organization.KEY_STATE)) {
                org.setState(readField(parser, Organization.KEY_STATE, 0));
            } else if (name.equals(Organization.KEY_URL)) {
                org.setUrl(readField(parser, Organization.KEY_URL, 0));
            } else if (name.equals(Organization.KEY_COUNTRIES)) {
                org.setCountries(readCountries(parser));
            } else if (name.equals(Organization.KEY_THEMES)) {
                org.setThemes(readThemes(parser));
            }else {
                skipTag(parser);
            }
        }
        return org;
    }

    /**
     * Parses the XML content all the countries where the organization operates in.
     */
    private List<String> readCountries(XmlPullParser parser) throws IOException, XmlPullParserException {
        List<String> countries = new ArrayList<>();
        parser.require(XmlPullParser.START_TAG, null, Organization.KEY_COUNTRIES);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() == XmlPullParser.START_TAG) {
                String name = parser.getName();
                if (name.equals(Organization.KEY_COUNTRY)) {
                    countries.add(readCountry(parser));
                } else {
                    skipTag(parser);
                }
            }
        }
        return countries;
    }

    /**
     * Parses the XML all the themes of this organization
     */
    private List<String> readThemes(XmlPullParser parser) throws IOException, XmlPullParserException {
        List<String> themes = new ArrayList<>();
        parser.require(XmlPullParser.START_TAG, null, Organization.KEY_THEMES);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() == XmlPullParser.START_TAG) {
                String name = parser.getName();
                if (name.equals("theme")) {
                    themes.add(readTheme(parser));
                } else {
                    skipTag(parser);
                }
            }
        }
        return themes;
    }

    /**
     * Parses the XML content of each theme
     */
    private String readTheme(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "theme");
        String theme = "";
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() == XmlPullParser.START_TAG) {
                String name = parser.getName();
                if (name.equals(Organization.KEY_NAME)) {
                    theme = readField(parser, Organization.KEY_NAME, 0);
                } else {
                    skipTag(parser);
                }
            }
        }
        return theme;
    }

    /**
     * Parses the XML content of each country where the organization operates in.
     */
    private String readCountry(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, Organization.KEY_COUNTRY);
        String country = "";
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() == XmlPullParser.START_TAG) {
                String name = parser.getName();
                if (name.equals(Organization.KEY_NAME)) {
                    country = readField(parser, Organization.KEY_NAME, 0);
                } else {
                    skipTag(parser);
                }
            }
        }
        return country;
    }

    /**
     * Parse each XML content for an specific attribute of the organization and return is value as a String
     *
     * @param parser
     * @param tag    Name of the field
     * @param type   0 if the value is an int and 1 if it should be an String
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String readField(XmlPullParser parser, String tag, int type) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, tag);
        String value = "";
        // If the next in our file is the information for a specific tag
        if (parser.next() == XmlPullParser.TEXT) {
            value = parser.getText();
            parser.nextTag();
        }
        parser.require(XmlPullParser.END_TAG, null, tag);
        return value;
    }

    /**
     * Ignore tag that are not relevant for this app
     *
     * @param parser
     * @throws XmlPullParserException
     * @throws IOException
     */
    private void skipTag(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int openTags = 1; // It begins in one because we are ignoring a tag that is supposed to be open when we start to ignore it
        while (openTags != 0) { // Change the parse to the next until the content of this tag is all passed
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    openTags--;
                    break;
                case XmlPullParser.START_TAG:
                    openTags++;
                    break;
            }
        }
    }
}
