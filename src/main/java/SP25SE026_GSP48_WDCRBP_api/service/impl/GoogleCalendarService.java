package SP25SE026_GSP48_WDCRBP_api.service.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.*;

import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class GoogleCalendarService {

    private static final String APPLICATION_NAME = "SpringBoot Google Meet Integration";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);

    private Calendar getCalendarService() throws Exception {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        var in = GoogleCalendarService.class.getResourceAsStream("/credentials.json");
        var clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        var flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES
        )
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();

        var receiver = new LocalServerReceiver.Builder()
                .setPort(8888)
                .setCallbackPath("/Callback")
                .build();

        var credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

        return new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }


    public String createGoogleMeetEvent(Date startTime, Date endTime, String summary) throws Exception {
        Calendar service = getCalendarService();

        Event event = new Event()
                .setSummary(summary)
                .setDescription("Cuộc họp được tạo bằng Google Calendar API")
                .setStart(new EventDateTime().setDateTime(new com.google.api.client.util.DateTime(startTime)).setTimeZone("Asia/Ho_Chi_Minh"))
                .setEnd(new EventDateTime().setDateTime(new com.google.api.client.util.DateTime(endTime)).setTimeZone("Asia/Ho_Chi_Minh"));

        // Thêm Google Meet
        ConferenceData conferenceData = new ConferenceData()
                .setCreateRequest(new CreateConferenceRequest()
                        .setRequestId("meet-" + System.currentTimeMillis())
                        .setConferenceSolutionKey(new ConferenceSolutionKey().setType("hangoutsMeet")));

        event.setConferenceData(conferenceData);

        event = service.events().insert("primary", event)
                .setConferenceDataVersion(1)
                .execute();

        return event.getHangoutLink(); // Trả về link Google Meet
    }
}