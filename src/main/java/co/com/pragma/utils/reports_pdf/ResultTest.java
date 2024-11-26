package co.com.pragma.utils.reports_pdf;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import lombok.Getter;

import java.util.ArrayList;

public class ResultTest {
    private String name = "";
    private int duration;
    private String startTime = "";
    private String result = "";
    private String scenario = "";
    private String driver = "";
    private ArrayList<Steps> steps;

    public ResultTest(String name, int duration, String startTime, String result, String scenario, String driver, ArrayList<Steps> steps) {
        this.name = name;
        this.duration = duration;
        this.startTime = startTime;
        this.result = result;
        this.scenario = scenario;
        this.driver = driver;
        this.steps = steps;
    }

//    public ResultTest(String name, int duration, String startTime, String result, String scenario, String driver, ArrayList<Steps> pasos) {
//    }

    public String getDriver() {
        return this.driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getResult() {
        return this.result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getScenario() {
        return this.scenario;
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    public ArrayList<Steps> getSteps() {
        return this.steps;
    }

    public void setSteps(ArrayList<Steps> steps) {
        this.steps = steps;
    }

    @Getter
    public static class Steps {
        private String step = "";
        private String description = "";
        private String screenshot = "";
        private String error = "";
        private String errorType = "";
        private String restQuery = "";

        public Steps(String step, String description, String screenshot, String error, String errorType, String restQuery) {
            this.step = step;
            this.description = description;
            this.screenshot = screenshot;
            this.error = error;
            this.errorType = errorType;
            this.restQuery = restQuery;
        }

        public void setStep(String step) {
            this.step = step;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setScreenshot(String screenshot) {
            this.screenshot = screenshot;
        }

        public void setError(String error) {
            this.error = error;
        }

        public void setErrorType(String errorType) {
            this.errorType = errorType;
        }

        public void setRestQuery(String restQuery) {
            this.restQuery = restQuery;
        }
    }
}

