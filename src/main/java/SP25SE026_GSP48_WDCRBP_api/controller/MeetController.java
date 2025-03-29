package SP25SE026_GSP48_WDCRBP_api.controller;

import SP25SE026_GSP48_WDCRBP_api.service.impl.GoogleCalendarService;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/api/meet")
public class MeetController {

    private final GoogleCalendarService calendarService;

    public MeetController(GoogleCalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @PostMapping("/create")
    public String createMeet(@RequestParam String title,
                             @RequestParam String startTime,
                             @RequestParam String endTime) throws Exception {
        // Định dạng thời gian truyền vào: yyyy-MM-dd HH:mm
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date start = formatter.parse(startTime);
        Date end = formatter.parse(endTime);

        return calendarService.createGoogleMeetEvent(start, end, title);
    }
}
