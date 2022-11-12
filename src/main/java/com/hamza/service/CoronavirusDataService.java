package com.hamza.service;

import com.hamza.model.LatestStat;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class CoronavirusDataService {

    private List<LatestStat> allStats = new ArrayList<>();

    public List<LatestStat> getAllStats() {
        return allStats;
    }

    private static String DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    private void fetchData() throws IOException, InterruptedException {

        List<LatestStat> newStats = new ArrayList<>();

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(DATA_URL))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        StringReader csvReader = new StringReader(response.body());

        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvReader);

        for (CSVRecord record : records) {
            LatestStat stat = new LatestStat();
            stat.setState(record.get("Province/State"));
            stat.setCountry(record.get("Country/Region"));
            int latestRecord = Integer.parseInt(record.get(record.size() - 1));
            int previousDayRecord = Integer.parseInt(record.get(record.size() - 2));
            stat.setLatestCount(latestRecord);
            stat.setDeltaFromPreviousDay(latestRecord - previousDayRecord);
//            System.out.println(stat.toString());
            newStats.add(stat);
        }

        this.allStats = newStats;
    }


}
