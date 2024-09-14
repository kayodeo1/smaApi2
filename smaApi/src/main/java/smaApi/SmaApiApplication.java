package smaApi;
import java.util.HashSet;
import java.util.Set;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
//import org.glassfish.jersey.media.multipart.MultiPartFeature;

@ApplicationPath("/api")
public class SmaApiApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> classes = new HashSet<>();

        // Add other resource classes here
        classes.add(StudentResource.class);

        return classes;
    }
}