package com.example.starview;

import java.io.Serializable;
import java.util.Calendar;

public class Star implements Serializable {
        private String name; // название
        private String category;
        private String age;
       // private String date;  // столица
        private String photo; // ресурс флага

        public Star(String age){
            this.age = age;
        }

        public Star(String name, String photo, String age, String category){

            this.name=name;
            this.photo=photo;
            this.age = age;
            this.category = category;

        }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public String getAge() {
            String year = age.substring(age.length()-4);
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            int result = currentYear - Integer.parseInt(year);

            String old = "";
        int ageLastNumber = result % 10;
        boolean isExclusion = (result % 100 >= 11) && (result % 100 <= 14);

        if (ageLastNumber == 1)
            old = "год";
        else if(ageLastNumber == 0 || ageLastNumber >= 5 && ageLastNumber <= 9)
            old = "лет";
        else if(ageLastNumber >= 2 && ageLastNumber <= 4)
            old = "года";
        if (isExclusion)
            old = "лет";
        return  String.valueOf(result) + " " +old;
    }

    public String getAllAge(){
            return age;
    }
    public void setAge(String age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {

            return photo.trim();
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }




}