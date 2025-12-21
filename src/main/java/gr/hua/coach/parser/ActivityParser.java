package gr.hua.coach.parser;

import gr.hua.coach.model.Activity;

import java.io.File;
import java.util.List;

/**
 * Defines an interface for parsing activity files into Activity objects.
 *
 * Implementations of this interface are responsible for reading input files
 * and converting them into domain model objects.
 */
public interface ActivityParser {

    List<Activity> parse(File file) throws Exception;
}
