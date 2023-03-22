package scims.model.data.scoring;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ScoringFactory {

    private static final Map<String, Class<? extends EventScoring>> SCORING_MAP = new HashMap<>();

    static {
        try {
            // Replace "com.example.scoring" with the package name where your EventScoring implementations are located
            List<Class<?>> scoringClasses = new ArrayList<>(ClassFinder.getClassesImplementing(EventScoring.class));

            // Loop through each scoring class and create an instance of it using reflection
            for (Class<?> clazz : scoringClasses) {
                // Get the part of the class name before "Scoring"
                if(clazz != EventScoring.class) {
                    String scoreType = clazz.getSimpleName().replaceAll("Scoring$", "");

                    // Create a new instance of the scoring class using reflection
                    EventScoring<?> scoring = (EventScoring<?>) clazz.newInstance();

                    // Add the scoring object to the map
                    SCORING_MAP.put(scoreType, (Class<? extends EventScoring>) clazz);
                }
            }
        } catch (Exception e) {
            // Handle any exceptions that occur
        }
    }

    public static EventScoring<?> createScoring(String scoreType) {
        Class<? extends EventScoring> scoringClass = SCORING_MAP.get(scoreType);

        EventScoring<?> scoring;
        if (scoringClass == null) {
            scoring = new CustomEventScoring();
        } else {
            try {
                scoring = scoringClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                scoring = new CustomEventScoring();
            }
        }

        return scoring;
    }

    public static List<EventScoring<?>> createAllScorings()  {
        List<EventScoring<?>> retVal = new ArrayList<>();
        for(Class<? extends EventScoring> scoringClass : SCORING_MAP.values()) {
            EventScoring<?> scoring;
            try {
                scoring = scoringClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                scoring = new CustomEventScoring();
            }
            retVal.add(scoring);
        }
        return retVal;
    }

    private static class ClassFinder {
        private static final char DOT = '.';
        private static final char SLASH = '/';
        private static final String CLASS_SUFFIX = ".class";

        public static List<Class<?>> getClassesImplementing(Class<?> parent) throws IOException, ClassNotFoundException {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            assert classLoader != null;
            String packageName = parent.getPackage().getName();
            String path = packageName.replace(DOT, SLASH);
            Enumeration<URL> resources = classLoader.getResources(path);
            List<Class<?>> classes = new ArrayList<>();
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                classes.addAll(findClasses(resource, packageName, parent));
            }
            return classes;
        }

        private static List<Class<?>> findClasses(URL resource, String packageName, Class<?> parent) throws ClassNotFoundException {
            List<Class<?>> classes = new ArrayList<>();
            if (resource.getProtocol().equals("jar")) {
                String jarFileName = resource.getFile();
                jarFileName = jarFileName.substring(5, jarFileName.indexOf("!"));
                try (JarFile jf = new JarFile(jarFileName)) {
                    Enumeration<JarEntry> jarEntries = jf.entries();
                    while (jarEntries.hasMoreElements()) {
                        JarEntry jarEntry = jarEntries.nextElement();
                        String className = jarEntry.getName();
                        if (className.endsWith(CLASS_SUFFIX) && className.startsWith(packageName)) {
                            className = className.substring(0, className.length() - CLASS_SUFFIX.length()).replace(SLASH, DOT);
                            Class<?> clazz = Class.forName(className);
                            if (parent.isAssignableFrom(clazz)) {
                                classes.add(clazz);
                            }
                        }
                    }
                } catch (IOException e) {
                    throw new ClassNotFoundException("Error reading JAR file: " + jarFileName, e);
                }
            } else {
                try {
                    String filePath = resource.getFile();
                    filePath = URLDecoder.decode(filePath, "UTF-8");
                    File dir = new File(filePath);
                    for (File file : dir.listFiles()) {
                        if (file.isDirectory()) {
                            classes.addAll(findClasses(file.toURI().toURL(), packageName + DOT + file.getName(), parent));
                        } else if (file.getName().endsWith(CLASS_SUFFIX)) {
                            String className = packageName + DOT + file.getName().substring(0, file.getName().length() - CLASS_SUFFIX.length());
                            Class<?> clazz = Class.forName(className);
                            if (parent.isAssignableFrom(clazz)) {
                                classes.add(clazz);
                            }
                        }
                    }
                } catch (UnsupportedEncodingException | MalformedURLException e) {
                    throw new RuntimeException("Unsupported encoding UTF-8", e);
                }
            }
            return classes;
        }
    }
}
