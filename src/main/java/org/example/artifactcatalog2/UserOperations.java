package org.example.artifactcatalog2;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public static boolean deleteArtifact(String ID){
        ArrayList<Artifact> artifacts= JSONOperations.readExistingList();
        int indexToRemove = -1;
        for (int i = 0; i < artifacts.size(); i++) {
            if (artifacts.get(i).getID().equals(ID)) { //equals is overridden so used like this
            indexToRemove = i;
            break;
            }
        }

        if (indexToRemove != -1) {
            artifacts.remove(indexToRemove);
            return JSONOperations.noCheckWriteJSON(JSONOperations.getDb(), artifacts); //no check because list is completely new
        } else {
            return false;
        }
    }

    public static boolean deleteArtifacts(ArrayList<Artifact> artifacts) {
        ArrayList<Artifact> currentList = JSONOperations.readExistingList();
        for (Artifact artifact : artifacts) {
            currentList.remove(artifact);
        }
        return JSONOperations.noCheckWriteJSON(JSONOperations.getDb(), currentList);


    }

    public static List<Artifact> list() {
        return JSONOperations.readExistingList();
    }

    public static void main(String[] args) {
       /* ArrayList<Artifact> list = JSONOperations.readExistingList();
        deleteArtifact("ID0");
        ArrayList<Artifact> list2 = JSONOperations.readExistingList();
        System.out.println(list2.getFirst().getID());*/
    }
}
