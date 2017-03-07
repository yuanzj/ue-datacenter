package net.rokyinfo.appcontroller.bean;

import java.util.List;
import java.util.Map;

public class HttpResponseEntity {

    private int state;

    private String message;

    private Map<String, String> data;

    private Map<String, List<String>> data2;
    
    private Map<String, Integer> data3;

    private List<Map<String, String>> data4;

    private List data5;

    private List content;

    public Map<String, Map<String, String>> getData6() {
        return data6;
    }

    public void setData6(Map<String, Map<String, String>> data6) {
        this.data6 = data6;
    }

    private Map<String, Map<String, String>> data6;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public Map<String, List<String>> getData2() {
        return data2;
    }

    public void setData2(Map<String, List<String>> data2) {
        this.data2 = data2;
    }

    public Map<String, Integer> getData3() {
        return data3;
    }

    public void setData3(Map<String, Integer> data3) {
        this.data3 = data3;
    }

    public List<Map<String, String>> getData4() {
        return data4;
    }

    public void setData4(List<Map<String, String>> data4) {
        this.data4 = data4;
    }

    public List getContent() {
        return content;
    }

    public void setContent(List content) {
        this.content = content;
    }

    public List getData5() {
        return data5;
    }

    public void setData5(List data5) {
        this.data5 = data5;
    }
}
