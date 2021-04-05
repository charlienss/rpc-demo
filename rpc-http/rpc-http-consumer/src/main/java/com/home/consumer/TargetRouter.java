package com.home.consumer;

import com.home.core.Router;

import java.util.List;

public class TargetRouter implements Router {
    @Override
    public List<String> route(List<String> urls) {
        return urls;
    }
}