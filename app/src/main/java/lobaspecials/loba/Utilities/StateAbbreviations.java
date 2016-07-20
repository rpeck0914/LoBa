package lobaspecials.loba.Utilities;

/**
 * Created by Robert Peck on 11/30/2015.
 */
public class StateAbbreviations {
    private String mStateAbbrev;

    public StateAbbreviations(String state) {
        AbbreviateStateName(state);
    }

    private void AbbreviateStateName(String name) {

        switch (name) {
            case "Alabama":
                mStateAbbrev = "AL";
                break;
            case "Alaska":
                mStateAbbrev = "AK";
                break;
            case "Arizona":
                mStateAbbrev = "AR";
                break;
            case "Arkansas":
                mStateAbbrev = "AR";
                break;
            case "California":
                mStateAbbrev = "CA";
                break;
            case "Colorado":
                mStateAbbrev = "CO";
                break;
            case "Connecticut":
                mStateAbbrev = "CT";
                break;
            case "Delaware":
                mStateAbbrev = "DE";
                break;
            case "Florida":
                mStateAbbrev = "FL";
                break;
            case "Georgia":
                mStateAbbrev = "GA";
                break;
            case "Hawaii":
                mStateAbbrev = "HI";
                break;
            case "Idaho":
                mStateAbbrev = "ID";
                break;
            case "Illinois":
                mStateAbbrev = "IL";
                break;
            case "Indiana":
                mStateAbbrev = "IN";
                break;
            case "Iowa":
                mStateAbbrev = "IA";
                break;
            case "Kentucky":
                mStateAbbrev = "KY";
                break;
            case "Louisiana":
                mStateAbbrev = "LA";
                break;
            case "Maine":
                mStateAbbrev = "ME";
                break;
            case "Maryland":
                mStateAbbrev = "MD";
                break;
            case "Massachusetts":
                mStateAbbrev = "MA";
                break;
            case "Michigan":
                mStateAbbrev = "MI";
                break;
            case "Minnesota":
                mStateAbbrev = "MN";
                break;
            case "Mississippi":
                mStateAbbrev = "MS";
                break;
            case "Missouri":
                mStateAbbrev = "MO";
                break;
            case "Montana":
                mStateAbbrev = "MT";
                break;
            case "Nebraska":
                mStateAbbrev = "NE";
                break;
            case "Nevada":
                mStateAbbrev = "NV";
                break;
            case "New Hampshire":
                mStateAbbrev = "NH";
                break;
            case "New Jersey":
                mStateAbbrev = "NJ";
                break;
            case "New Mexico":
                mStateAbbrev = "NM";
                break;
            case "New York":
                mStateAbbrev = "NY";
                break;
            case "North Carolina":
                mStateAbbrev = "NC";
                break;
            case "North Dakota":
                mStateAbbrev = "ND";
                break;
            case "Ohio":
                mStateAbbrev = "OH";
                break;
            case "Oklahoma":
                mStateAbbrev = "OK";
                break;
            case "Oregon":
                mStateAbbrev = "OR";
                break;
            case "Pennsylvania":
                mStateAbbrev = "PA";
                break;
            case "Rhode Island":
                mStateAbbrev = "RI";
                break;
            case "South Carolina":
                mStateAbbrev = "SC";
                break;
            case "South Dakota":
                mStateAbbrev = "SD";
                break;
            case "Tennessee":
                mStateAbbrev = "TN";
                break;
            case "Texas":
                mStateAbbrev = "TX";
                break;
            case "Utah":
                mStateAbbrev = "UT";
                break;
            case "Vermont":
                mStateAbbrev = "VT";
                break;
            case "Virginia":
                mStateAbbrev = "VA";
                break;
            case "Washington":
                mStateAbbrev = "WA";
                break;
            case "West Virginia":
                mStateAbbrev = "WV";
                break;
            case "Wisconsin":
                mStateAbbrev = "WI";
                break;
            case "Wyoming":
                mStateAbbrev = "WY";
                break;
        }
    }

    public String getStateAbbrev() {
        return mStateAbbrev;
    }
}
