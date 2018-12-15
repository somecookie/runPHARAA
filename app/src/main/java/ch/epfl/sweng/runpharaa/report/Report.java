package ch.epfl.sweng.runpharaa.report;

import ch.epfl.sweng.runpharaa.user.User;
import ch.epfl.sweng.runpharaa.utils.Required;

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

    public Report(User source, User target, String message) {
        Required.nonNull(source, "Source user of a report cannot be null");
        Required.nonNull(target, "Target user of a report cannot be null");
        Required.nonNull(message,"Message of the report cannot be null");

        this.source = source;
        this.target = target;
        this.message = message;
    }

    public User getSource() { return source; }

    public User getTarget() { return target; }

    public String getMessage() { return message; }

    public void sendReport(){

    }

    public String getReportMessage(String subject) {
        StringBuilder message = new StringBuilder();
        switch (subject) {
            case "Inappropriate Content" : message.append("This User's comments and profile contain inappropriate content.");
            case "Abusive Language" : message.append("This User's comments and profile contain abusive language.");
            case "Fake/Dangerous Information" : message.append("This User's comments and profile contain fake information that could be dangerous.");
        }
        return message.toString();
    }
}
