package com.example.alarmapp;

public class ListViewItem {
    private int[] time = new int[2];

    public void setTime(int[] time){
        this.time[0] = time[0];
        this.time[1] = time[1];
    }
    public int[] getTime(){
        return time;
    }

}

