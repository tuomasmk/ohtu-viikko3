package ohtu;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.http.client.fluent.Request;

public class Main {
    
    static private int[] getCourseStats(String code) throws IOException {
        String courseOverviewUrl = "https://studies.cs.helsinki.fi/courses/%s/stats";
        String url = String.format(courseOverviewUrl, code);
        String statsResponse = Request.Get(url).execute().returnContent().asString();

        JsonParser parser = new JsonParser();
        JsonObject parsittuData = parser.parse(statsResponse).getAsJsonObject();
        
        int hour_total = 0;
        int students = 0;
        int exercise_total = 0;
        System.out.println("");
        for (String key : parsittuData.keySet()) {
            hour_total += parsittuData.get(key).getAsJsonObject().get("hour_total").getAsInt();
            students += parsittuData.get(key).getAsJsonObject().get("students").getAsInt();
            exercise_total += parsittuData.get(key).getAsJsonObject().get("exercise_total").getAsInt();
        }
        
        return new int[] {students, exercise_total, hour_total};
    }

    public static void main(String[] args) throws IOException {
        // ÄLÄ laita githubiin omaa opiskelijanumeroasi
        String studentNr = "012345678";
        if ( args.length>0) {
            studentNr = args[0];
        }

        String url = "https://studies.cs.helsinki.fi/courses/students/"+studentNr+"/submissions";
        
        String courseUrl = "https://studies.cs.helsinki.fi/courses/courseinfo";

        String bodyText = Request.Get(url).execute().returnContent().asString();

        /*System.out.println("json-muotoinen data (palautukset):");
        System.out.println( bodyText );*/

        Gson mapper = new Gson();
        Submission[] subs = mapper.fromJson(bodyText, Submission[].class);
        
        bodyText = Request.Get(courseUrl).execute().returnContent().asString();
        
        /*System.out.println("json-muotoinen data (kurssi):");
        System.out.println( bodyText );*/

        mapper = new Gson();
        Course[] courses = mapper.fromJson(bodyText, Course[].class);
        
        System.out.println("opiskelijanumero " + studentNr);
        System.out.println("");
        int doneExercises = 0;
        int usedHours = 0;
        List<String> courseNames = Arrays.stream(subs)
                .map(s -> s.getCourse())
                .collect(Collectors.toList());
        for(Course course : Arrays.stream(courses)
                .filter(c -> courseNames.contains(c.getName()))
                .collect(Collectors.toList())) {
            System.out.println(course.getFullName() + " " + course.getTerm() 
                    + " " + course.getYear());
            System.out.println("");
            for(Submission sub : Arrays.stream(subs)
                    .filter(s -> s.getCourse().equals(course.getName()))
                    .sorted((c, d) -> c.getWeek() - d.getWeek())
                    .collect(Collectors.toList())) {
                doneExercises += sub.getExercises().size();
                usedHours += sub.getHours();
                System.out.println("viikko " + sub.getWeek() + ":");
                System.out.println(" tehtyjä tehtäviä "
                                    + sub.getExercises().size() + "/" 
                                    + course.getExercises().get(sub.getWeek()) 
                                    + " aikaa kului " + sub.getHours() 
                                    + " tehdyt tehtävät " + sub.excercisesTostring());
            }
            System.out.println("");
            System.out.println("yhteensä: " + doneExercises + "/"
                + course.getExercises().stream().mapToInt(e -> e).sum()
                + " tehtävää " + usedHours + " tuntia");
            int[] stats = getCourseStats(course.getName());
            System.out.println("kurssilla yhteensä " + stats[0] + " palautusta"
                    + ", palautettujen tehtäviä " + stats[1] + " kpl"
                    + ", aikaa käytetty yhteensä " + stats[2] + " tuntia");
            doneExercises = 0;
            usedHours = 0;
            System.out.println("");
        }
    }
}