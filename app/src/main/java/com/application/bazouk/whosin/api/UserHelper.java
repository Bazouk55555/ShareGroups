package com.application.bazouk.whosin.api;

import com.application.bazouk.whosin.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserHelper {

    private static final String COLLECTION_NAME = "users";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<DocumentReference> createUser(User userToCreate) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("username", userToCreate.getUsername());
        userMap.put("password", userToCreate.getPassword());
        userMap.put("name", userToCreate.getName());
        return getUsersCollection().add(userMap);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getUser(String uid){
        return UserHelper.getUsersCollection().document(uid).get();
    }

    // --- UPDATE ---

    public static Task<Void> updateUsername(String uid, String username, String name) {
        return UserHelper.getUsersCollection().document(uid).update("username", username,"name", name);
    }

    // --- DELETE ---

    public static Task<Void> deleteUser(String uid) {
        return UserHelper.getUsersCollection().document(uid).delete();
    }

}
