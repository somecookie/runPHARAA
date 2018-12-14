package ch.epfl.sweng.runpharaa.report;

import ch.epfl.sweng.runpharaa.user.User;

public class Report {
    private User source;
    private User target;
    private String message;

    //Add a Warning Triangle icon in TrackPropertiesActivity and in OtherUserProfileActivity:
    /*
    - on click: shows a check list -> choose one category
    - a string message associated with the category will be used to construct a report
    - the report is sent to the address/addresses of the developers from runPHARAA
     */

    //Track Reports
    /*
    - Inappropriate Content
    - Abusive Language
    - Fake/Dangerous Information
     */

    //User Reports
    /*
    - Inappropriate Content
    - Abusive Language
    - Fake/Dangerous Information
     */

    public Report(User source, User target, String message) {
        this.source = source;
        this.target = target;
        this.message = message;
    }

    public void sendReport(){
        
    }
}
