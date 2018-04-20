package com.juliuskrah.quartz.web.rest;

import com.juliuskrah.quartz.helpers.ApiResponse;
import com.juliuskrah.quartz.model.MeetingJobDescriptor;
import com.juliuskrah.quartz.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequestMapping("/task/meeting")
public class MeetingTaskEndpoint {
    @Autowired
    private MeetingService meetingService;

    @RequestMapping(path = "/groups/{group}/jobs", method = RequestMethod.POST)
    public ApiResponse createJob(WebRequest request, @PathVariable String group, @RequestBody MeetingJobDescriptor descriptor) {
        descriptor.setGroup(group);
        MeetingJobDescriptor job = meetingService.createJob(group, descriptor);
        return ApiResponse.success(request, job);
    }

    @RequestMapping(path = "/groups/{group}/jobs/{name}", method = RequestMethod.GET)
    public ApiResponse findJob(WebRequest request, @PathVariable String group, @PathVariable String name){
        MeetingJobDescriptor job = meetingService.findJob(group, name);

        return ApiResponse.success(request,job);
    }
}
