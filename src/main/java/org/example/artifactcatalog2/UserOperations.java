package org.example.artifactcatalog2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UserOperations {
    
    public static  boolean createArtifact(Artifact artifact){
        ArrayList<Artifact> currentList= JSONOperations.readExistingList();
        currentList.add(artifact);
        return JSONOperations.noCheckWriteJSON(JSONOperations.getDb(), currentList);        
    }

    public static boolean editArtifact(String ID, Artifact editedArtifact){
        ArrayList<Artifact> artifacts = JSONOperations.readExistingList();
        for(int i=0; i< artifacts.size(); i++){
            if (artifacts.get(i).getID().equals(ID)) {
                artifacts.set(i, editedArtifact);
                return JSONOperations.noCheckWriteJSON(JSONOperations.getDb(), artifacts);
            }
        }
        return false;
    }

    public static boolean deleteArtifact(String id) {
        ArrayList<Artifact> currentList = JSONOperations.readExistingList();
        boolean removed = false;
        Iterator<Artifact> iterator = currentList.iterator();
        while (iterator.hasNext()) {
            Artifact artifact = iterator.next();
            if (artifact.getID().equals(id)) {
                String relativePath = artifact.getImagePath();
                if (relativePath != null && !relativePath.isBlank()) {
                    Path imagePath = Paths.get(System.getProperty("user.dir")).resolve(relativePath);
                    try {
                        boolean deleted = Files.deleteIfExists(imagePath);
                    } catch (IOException e) {
                        return false;
                    }
                }
                iterator.remove();
                removed = true;
                break;
            }
        }
        if (removed) {
            boolean writeSuccess = JSONOperations.noCheckWriteJSON(JSONOperations.getDb(), currentList);
            return writeSuccess;
        } else {
            return false;
        }
    }
    
    public static boolean deleteArtifacts(List<Artifact> artifacts) {
        boolean allDeleted = true;
        for (Artifact artifact : artifacts) {
            boolean result = deleteArtifact(artifact.getID());
            if (!result) {
                allDeleted = false;
            }
        }
        return allDeleted;
    }

}
