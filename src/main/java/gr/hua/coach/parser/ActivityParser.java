package gr.hua.coach.parser;

import gr.hua.coach.model.Activity;

import java.io.File;
import java.util.List;

public interface ActivityParser {

    List<Activity> parse(File file) throws Exception;
}
