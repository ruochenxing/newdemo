package com.exceldemo.entity;

import com.exceldemo.Parser;

public class OrderDetail {

    private String name;
    private double num;
    private String person;
    private String phone;
    private String area;
    private String address;
    private String subNumber;
    private String tuanId;
    private String rightAddress;
    private String lng;
    private String lat;
    private String key;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getNum() {
        return num;
    }

    public void setNum(double num) {
        this.num = num;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        this.setRightAddress(Parser.parseRightAddress(this.area, this.address));
    }

    public String getSubNumber() {
        return subNumber;
    }

    public void setSubNumber(String subNumber) {
        this.subNumber = subNumber;
    }

    public String getTuanId() {
        return tuanId;
    }

    public void setTuanId(String tuanId) {
        this.tuanId = tuanId;
    }

    public String getRightAddress() {
        return rightAddress;
    }

    public void setRightAddress(String rightAddress) {
        this.rightAddress = rightAddress;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "OrderDetail [name=" + name + ", num=" + num + ", person=" + person + ", phone=" + phone + ", area="
                + area + ", address=" + address + ", subNumber=" + subNumber + ", tuanId=" + tuanId + ", rightAddress="
                + rightAddress + ", lng=" + lng + ", lat=" + lat + ", key=" + key + "]";
    }
}
