package postmanresponse2csv;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import com.opencsv.CSVWriter;

public class PostmanResponse2Csv {

    public static void main(String[] args) throws Exception {
        InputStream fis = new FileInputStream(Constants.JSON_FILE);
        FileWriter outputfile = new FileWriter(Constants.CSV_FILE);

        JsonReader reader = Json.createReader(fis);
        CSVWriter writer = new CSVWriter(outputfile);

        JsonArray jsonArray = reader.readArray();
        reader.close();
        
        
        for (JsonValue jsonValue : jsonArray) {
            JsonObject jsonObject = jsonValue.asJsonObject();
            String mdo = jsonObject.getString("rootOrgName");
            JsonObject profileDetails = jsonObject.getJsonObject("profileDetails");
            if(profileDetails != null)
            {
            JsonArray userRoles = profileDetails.getJsonArray("userRoles");
            JsonObject personalDetails = profileDetails.getJsonObject("personalDetails");
            // 
            String designation = "";
            if (userRoles != null) {
                if (profileDetails.getJsonArray("professionalDetails") != null) {
                    JsonArray professionalDetails = profileDetails.getJsonArray("professionalDetails");
                    for(JsonValue profValue : professionalDetails)
                    {
                        JsonObject profObject = profValue.asJsonObject();
                        designation = profObject.getString("designation");
                    }
                    
                } else {
                    designation = "-";

                }
                String updatedOn = jsonObject.getString("updatedDate");
                String email="";
                if(personalDetails != null)
                {
                    email = personalDetails.getString("primaryEmail");
                }
                else
                {
                    email = "-";
                }
                 for (JsonValue userRole : userRoles) {
                    System.out.println(email);
                    JsonObject roleObject = userRole.asJsonObject();
                    JsonArray activitArray = roleObject.getJsonArray("activities");
                    if (roleObject.get("name") != null &&  activitArray != null) {
                    String role = roleObject.getString("name");

                        for (JsonValue activityValue : activitArray) {
                            JsonObject activityObject = activityValue.asJsonObject();
                            String activity = activityObject.getString("name");
                            String[] csvRow = { mdo, designation, role, activity, email, updatedOn };
                            writer.writeNext(csvRow);

                        }
                    
                }
                }

            } else
                continue;
        }
        }
        writer.close();
    }

}