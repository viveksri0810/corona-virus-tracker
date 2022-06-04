package com.vivek.cvt.service;

import com.vivek.cvt.models.ModelStats;
import com.vivek.cvt.models.SummaryStats;
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
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CoronaVirusDataService {


    private static final String VIRUS_DATA_URL = uriBuilder();
    private Long totalConfirmed = 0L;
    private Long totalDeath = 0L;
    private Long totalRecovered = 0L;
    private Long totalActiveCases = 0L;
    private List<ModelStats> modelStatsList;

    private SummaryStats summaryStats;

    private static String uriBuilder() {

        String uriString = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_daily_reports/";
        String fileExtension = ".csv";

        Date date = new Date();
        String date2 = "";
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd-MM-yyyy");

        Date date1 = date;
        try {
            date1 = simpleDateFormat1.parse(date2);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println(date1);
        System.out.println(date);
        date1 = new Date(date1.getTime() - Duration.ofDays(1).toMillis());
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("MM-dd-yyyy");
        String date4 = simpleDateFormat2.format(date1);
        System.out.println(date4);


        // creating url

        return uriString +
                date4 +
                fileExtension;
    }

    public List<ModelStats> getModelStatsList() {
        return modelStatsList;
    }

    public SummaryStats getSummaryStats() {
        return summaryStats;
    }

    @PostConstruct
    @Scheduled(cron = "0 0 1 * * *")
    public void fetchVirusData() throws IOException, InterruptedException {

        List<ModelStats> newModelStatsList = new ArrayList<>();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(VIRUS_DATA_URL))
                .build();
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = httpResponse.body();

        //Reading csv file data

        StringReader in = new StringReader(responseBody);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);
        for (CSVRecord record : records) {
            String country_Region = record.get("Country_Region");
            String province_State = record.get("Province_State");
            String last_Update = record.get("Last_Update");
            String confirmed = record.get("Confirmed").isBlank() ? "0" : record.get("Confirmed");
            String deaths = record.get("Deaths").isBlank() ? "0" : record.get("Deaths");
            String recovered = record.get("Recovered").isBlank() ? "0" : record.get("Recovered");
            String active = record.get("Active").isBlank() ? "0" : record.get("Active");
            this.totalConfirmed += Long.parseLong(confirmed);
            this.totalRecovered += Long.parseLong(recovered);
            this.totalDeath += Long.parseLong(deaths);
            this.totalActiveCases += Long.parseLong(active);
            ModelStats stats = new ModelStats(country_Region, province_State, last_Update, confirmed, deaths, recovered, active);
            newModelStatsList.add(stats);
        }
        modelStatsList = newModelStatsList;
        summaryStats = new SummaryStats(totalConfirmed, totalRecovered, totalDeath, totalActiveCases);
    }
}
