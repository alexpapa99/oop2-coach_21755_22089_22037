package gr.hua.coach.parser;

import gr.hua.coach.model.*;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser implementation for TCX (Training Center XML) files.
 *
 * This class reads TCX XML files and converts their contents into
 * Activity, Lap, Track and TrackPoint objects.
 */
public class TcxActivityParser implements ActivityParser {

    @Override
    public List<Activity> parse(File file) throws Exception {
        List<Activity> activities = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);

        NodeList activityNodes = doc.getElementsByTagName("Activity");

        for (int i = 0; i < activityNodes.getLength(); i++) {
            Element activityEl = (Element) activityNodes.item(i);

            ActivityType type = ActivityType.valueOf(
                    activityEl.getAttribute("Sport").toUpperCase()
            );

            String startTimeStr = activityEl
                    .getElementsByTagName("Id")
                    .item(0)
                    .getTextContent();

            LocalDateTime startTime = LocalDateTime.parse(
                    startTimeStr,
                    DateTimeFormatter.ISO_DATE_TIME
            );

            Activity activity = new Activity(type, startTime);

            NodeList lapNodes = activityEl.getElementsByTagName("Lap");
            for (int j = 0; j < lapNodes.getLength(); j++) {
                Element lapEl = (Element) lapNodes.item(j);
                Lap lap = new Lap();

                NodeList trackNodes = lapEl.getElementsByTagName("Track");
                for (int k = 0; k < trackNodes.getLength(); k++) {
                    Element trackEl = (Element) trackNodes.item(k);
                    Track track = new Track();

                    NodeList pointNodes = trackEl.getElementsByTagName("Trackpoint");
                    for (int p = 0; p < pointNodes.getLength(); p++) {
                        Element pointEl = (Element) pointNodes.item(p);

                        LocalDateTime time = LocalDateTime.parse(
                                pointEl.getElementsByTagName("Time")
                                        .item(0)
                                        .getTextContent(),
                                DateTimeFormatter.ISO_DATE_TIME
                        );

                        NodeList posNodes = pointEl.getElementsByTagName("Position");
                        if (posNodes.getLength() == 0) {
                            // we ignore trackpoint without gps
                            continue;
                        }

                        Element pos = (Element) posNodes.item(0);

                        double lat = Double.parseDouble(
                                pos.getElementsByTagName("LatitudeDegrees")
                                        .item(0)
                                        .getTextContent()
                        );

                        double lon = Double.parseDouble(
                                pos.getElementsByTagName("LongitudeDegrees")
                                        .item(0)
                                        .getTextContent()
                        );


                        double alt = Double.parseDouble(
                                pointEl.getElementsByTagName("AltitudeMeters")
                                        .item(0)
                                        .getTextContent()
                        );

                        Integer hr = null;
                        NodeList hrNodes = pointEl.getElementsByTagName("HeartRateBpm");
                        if (hrNodes.getLength() > 0) {
                            Element hrEl = (Element) hrNodes.item(0);
                            hr = Integer.parseInt(
                                    hrEl.getElementsByTagName("Value")
                                            .item(0)
                                            .getTextContent()
                            );
                        }

                        track.addPoint(new TrackPoint(time, lat, lon, alt, hr));
                    }

                    lap.addTrack(track);
                }

                activity.addLap(lap);
            }

            activities.add(activity);
        }

        return activities;
    }
}
