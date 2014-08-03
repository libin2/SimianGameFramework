package com.test;

public class Msg {
    
    private int id;

    public int getId() {
        return this.id;
    }

    public void setId(int aId) {
        this.id = aId;
    }

    @Override
    public String toString() {
        return "Msg [id=" + this.id + "]";
    }
    public static void main(String[] args) {
		Integer d=11;
		System.out.println(d.toString());
	}

}