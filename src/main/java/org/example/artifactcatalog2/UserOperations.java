package org.example.artifactcatalog2;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserOperations {
    
    public static  boolean createArtifact(Artifact artifact){
        ArrayList<Artifact> currentList= JSONOperations.readExistingList();
        currentList.add(artifact);
        return JSONOperations.writeJSON(JSONOperations.getDb(), currentList);
    }

    public static boolean editArtifact(String ID, Artifact editedArtifact){
        ArrayList<Artifact> artifacts = JSONOperations.readExistingList();
        for(int i=0; i< artifacts.size(); i++){
            if (Objects.equals(artifacts.get(i).getID(), ID)) {
                artifacts.set(i, editedArtifact);
                return JSONOperations.writeJSON(JSONOperations.getDb(), artifacts);
            }
        }
        return false;
    }

    public static boolean deleteArtifact(String ID){
        ArrayList<Artifact> artifacts= JSONOperations.readExistingList();
        int indexToRemove = -1;
        for (int i = 0; i < artifacts.size(); i++) {
            if (Objects.equals(artifacts.get(i).getID(), ID)) {
            indexToRemove = i;
            break;
            }
        }

        if (indexToRemove != -1) {
            artifacts.remove(indexToRemove);
            return JSONOperations.writeJSON(JSONOperations.getDb(), artifacts);
        } else {
            return false;
        }
    }

    public static List<Artifact> list() {
        return JSONOperations.readExistingList();
    }
}
