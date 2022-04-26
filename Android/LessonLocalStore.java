package com.ium.example.progetto.BookingTest;

import android.content.Context;
import android.content.SharedPreferences;

public class LessonLocalStore {

        public static final String SP_NAME = "lessonDetails";
        SharedPreferences userLessonDatabase;

        public  LessonLocalStore(Context context){
            userLessonDatabase = context.getSharedPreferences(SP_NAME,0);
        }

        public void storeLessonData(Lesson lesson){
            SharedPreferences.Editor spEditor = userLessonDatabase.edit();
            spEditor.putString("name", String.valueOf(lesson.name));
            spEditor.putString("corso", String.valueOf(lesson.corso));
            spEditor.commit();

        }

        public Lesson getLoggedLesson(){
            String name = userLessonDatabase.getString("name", "");
            String corso = userLessonDatabase.getString("corso", "");
            String ora = userLessonDatabase.getString("ora", "");
            String data = userLessonDatabase.getString("data", "");

            Lesson storedLesson = new Lesson(name,corso,ora,data);
            return storedLesson;
        }

        public void setLessonLoggedIn(boolean loggedIn){
            SharedPreferences.Editor spEditor = userLessonDatabase.edit();
            spEditor.putBoolean("loggedIn", loggedIn);
            spEditor.commit();
        }

        public boolean getLeesonLoggedIn(){
            if(userLessonDatabase.getBoolean("LoggedIn",false)){
                return true;
            } else {
                return false;
            }
        }

        public void clearLeesonData(){
            SharedPreferences.Editor spEditor = userLessonDatabase.edit();
            spEditor.clear();
            spEditor.commit();
        }
}
